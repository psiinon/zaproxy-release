/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.spider;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.ConnectionParam;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpSender;
import org.zaproxy.zap.spider.filters.DefaultFetchFilter;
import org.zaproxy.zap.spider.filters.DefaultParseFilter;
import org.zaproxy.zap.spider.filters.FetchFilter;
import org.zaproxy.zap.spider.filters.FetchFilter.FetchStatus;
import org.zaproxy.zap.spider.filters.ParseFilter;

/**
 * The Class Spider.
 */
public class Spider {

	/** The spider parameters. */
	private SpiderParam spiderParam;

	/** The connection parameters. */
	private ConnectionParam connectionParam;

	/** The model. */
	private Model model;

	/** The listeners for Spider related events. */
	private LinkedList<SpiderListener> listeners;

	/** If the spider is currently paused. */
	private boolean paused;

	/** The the spider is currently stopped. */
	private boolean stopped;

	/** The pause lock, used for locking access to the "paused" variable. */
	private ReentrantLock pauseLock = new ReentrantLock();

	/** The controller that manages the spidering process. */
	private SpiderController controller;

	/**
	 * The condition that is used for the threads in the pool to wait on, when the Spider crawling
	 * is paused. When the Spider is resumed, all the waiting threads are awakened.
	 */
	private Condition pausedCondition = pauseLock.newCondition();

	/** The thread pool for spider workers. */
	ExecutorService threadPool;

	/** The default fetch filter. */
	DefaultFetchFilter defaultFetchFilter;

	/** The seed list. */
	ArrayList<URI> seedList;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(Spider.class);

	/** The HTTP sender used to effectively send the data. */
	private HttpSender httpSender;

	/** The count of the tasks finished. */
	private AtomicInteger tasksDoneCount;

	/** The total count of all the submitted tasks. */
	private AtomicInteger tasksTotalCount;

	/** The cookie manager. */
	private CookieManager cookieManager;

	/**
	 * The initialized marks if the spidering process is completely started. It solves the problem
	 * when the first task is processed and the process is finished before the other seeds are
	 * added.
	 */
	private boolean initialized;

	/**
	 * Instantiates a new spider.
	 * 
	 * @param spiderParam the spider param
	 * @param connectionParam the connection param
	 * @param model the model
	 */
	public Spider(SpiderParam spiderParam, ConnectionParam connectionParam, Model model) {
		super();
		log.info("Spider initializing...");
		this.spiderParam = spiderParam;
		this.connectionParam = connectionParam;
		this.model = model;
		this.controller = new SpiderController(this);
		this.listeners = new LinkedList<SpiderListener>();
		this.seedList = new ArrayList<URI>();
		this.cookieManager = new CookieManager();

		init();
	}

	/**
	 * Initialize the spider.
	 */
	private void init() {
		this.paused = false;
		this.stopped = true;
		this.tasksDoneCount = new AtomicInteger(0);
		this.tasksTotalCount = new AtomicInteger(0);
		this.initialized = false;

		// Add a default fetch filter
		defaultFetchFilter = new DefaultFetchFilter();
		this.addFetchFilter(defaultFetchFilter);
		// Add domains always in scope
		String scope = spiderParam.getScope();
		if (scope != null && !scope.trim().isEmpty()) {
			defaultFetchFilter.addScopeRegex(scope);
		}
		// Add a default parse filter
		this.addParseFilter(new DefaultParseFilter());
	}

	/* SPIDER Related */
	/**
	 * Adds a new seed for the Spider.
	 * 
	 * @param msg the message used for seed. The request URI is used from the Request Header
	 */
	public void addSeed(HttpMessage msg) {
		URI uri = msg.getRequestHeader().getURI();
		addSeed(uri);
	}

	/**
	 * Adds a new seed for the Spider.
	 * 
	 * @param uri the uri
	 */
	public void addSeed(URI uri) {
		// Update the scope of the spidering process
		String host = null;

		try {
			host = uri.getHost();
			defaultFetchFilter.addScopeRegex(host);
		} catch (URIException e) {
			log.error("There was an error while adding seed value: " + uri, e);
			return;
		}
		// Add the seed to the list -- it will be added to the task list only when the spider is
		// started
		this.seedList.add(uri);
		// Add the appropriate 'robots.txt' as a seed
		if (getSpiderParam().isParseRobotsTxt()) {
			try {
				// Build the URI of the robots.txt file
				URI robotsUri;
				// If the port is not 80 or 443, add it to the URI
				if (uri.getPort() == 80 || uri.getPort() == 443)
					robotsUri = new URI(uri.getScheme() + "://" + host + "/robots.txt", true);
				else
					robotsUri = new URI(uri.getScheme() + "://" + host + ":" + uri.getPort() + "/robots.txt", true);
				this.seedList.add(robotsUri);
			} catch (Exception e) {
				log.warn("Error while creating URI for robots.txt file for site " + uri, e);
			}
		}

	}

	/**
	 * Sets the exclude list which contains a List of strings, defining the uris that should be
	 * excluded.
	 * 
	 * @param excludeList the new exclude list
	 */
	public void setExcludeList(List<String> excludeList) {
		log.debug("New Exclude list: " + excludeList);
		defaultFetchFilter.setExcludeRegexes(excludeList);
	}

	/**
	 * Adds a new fetch filter to the spider.
	 * 
	 * @param filter the filter
	 */
	public void addFetchFilter(FetchFilter filter) {
		controller.addFetchFilter(filter);
	}

	/**
	 * Adds a new parse filter to the spider.
	 * 
	 * @param filter the filter
	 */
	public void addParseFilter(ParseFilter filter) {
		controller.addParseFilter(filter);
	}

	/**
	 * Gets the http sender. Can be called from the SpiderTask.
	 * 
	 * @return the http sender
	 */
	protected HttpSender getHttpSender() {
		return httpSender;
	}

	/**
	 * Gets the spider parameters. Can be called from the SpiderTask.
	 * 
	 * @return the spider parameters
	 */
	protected SpiderParam getSpiderParam() {
		return spiderParam;
	}

	/**
	 * Gets the controller.
	 * 
	 * @return the controller
	 */
	protected SpiderController getController() {
		return controller;
	}

	/**
	 * Gets the cookie manager.
	 * 
	 * @return the cookie manager
	 */
	protected CookieManager getCookieManager() {
		return cookieManager;
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	protected Model getModel() {
		return this.model;
	}

	/**
	 * Submit a new task to the spidering task pool.
	 * 
	 * @param task the task
	 */
	protected synchronized void submitTask(SpiderTask task) {
		if (isStopped()) {
			log.debug("Submitting task skipped (" + task + ") as the Spider process is stopped.");
			return;
		}
		if (isTerminated()) {
			log.debug("Submitting task skipped (" + task + ") as the Spider process is terminated.");
			return;
		}
		this.tasksTotalCount.incrementAndGet();
		this.threadPool.execute(task);
	}

	/* SPIDER PROCESS maintenance - pause, resume, shutdown, etc. */

	/**
	 * Starts the Spider crawling.
	 */
	public void start() {

		log.info("Starting spider...");

		// Check if seeds are available, otherwise the Spider will start, but will not have any
		// seeds and will not stop.
		if (seedList == null || seedList.isEmpty()) {
			log.warn("No seeds available for the Spider. Cancelling scan...");
			notifyListenersSpiderComplete(false);
			return;
		}

		this.controller.reset();
		this.stopped = false;
		this.paused = false;
		this.initialized = false;

		// Initialize the thread pool
		this.threadPool = Executors.newFixedThreadPool(spiderParam.getThreadCount());

		// Initialize the HTTP sender
		httpSender = new HttpSender(connectionParam, true, HttpSender.SPIDER_INITIATOR);
		// Do not follow redirections because the request is not updated, the redirections will be
		// handled manually.
		httpSender.setFollowRedirect(false);

		// Add the seeds
		for (URI uri : seedList) {
			if (log.isInfoEnabled())
				log.info("Adding seed for spider: " + uri);
			controller.addSeed(uri, HttpRequestHeader.GET);
		}
		// Mark the process as completely initialized
		initialized = true;
	}

	/**
	 * Stops the Spider crawling. Must not be called from any of the threads in the thread pool.
	 */
	public void stop() {
		log.info("Stopping spidering process by request.");
		// Issue the shutdown command
		if (this.stopped == false) {
			this.threadPool.shutdownNow();
		}
		this.stopped = true;
		if (httpSender != null) {
			this.getHttpSender().shutdown();
			httpSender = null;
		}

		// Notify the controller to clean up memory
		controller.reset();
		this.threadPool = null;

		// Notify the listeners -- in the meanwhile
		notifyListenersSpiderComplete(false);
	}

	/**
	 * The Spidering process is complete.
	 */
	private void complete() {
		log.info("Spidering process is complete. Shutting down...");
		this.stopped = true;
		if (httpSender != null) {
			this.getHttpSender().shutdown();
			httpSender = null;
		}

		// Notify the controller to clean up memory
		controller.reset();

		// Issue the shutdown command on a separate thread, as the current thread is most likely one
		// from the pool
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadPool.shutdownNow();
				// Notify the listeners -- in the meanwhile
				notifyListenersSpiderComplete(true);
				controller.reset();
				threadPool = null;
			}
		}).start();
	}

	/**
	 * Pauses the Spider crawling.
	 */
	public void pause() {
		pauseLock.lock();
		try {
			paused = true;
		} finally {
			pauseLock.unlock();
		}
	}

	/**
	 * Resumes the Spider crawling.
	 */
	public void resume() {
		pauseLock.lock();
		try {
			paused = false;
			// Wake up all threads that are currently paused
			pausedCondition.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}

	/**
	 * This method is run by each thread in the Thread Pool before the task execution. Particularly,
	 * it checks if the Spidering process is paused and, if it is, it waits on the corresponding
	 * condition for the process to be resumed. Called from the SpiderTask.
	 */
	protected void preTaskExecution() {
		pauseLock.lock();
		try {
			while (paused)
				pausedCondition.await();
		} catch (InterruptedException e) {
		} finally {
			pauseLock.unlock();
		}
	}

	/**
	 * This method is run by each thread in the Thread Pool before the task execution. Particularly,
	 * it notifies the listeners of the progress and checks if the scan is complete. Called from the
	 * SpiderTask.
	 */
	protected void postTaskExecution() {
		int done = this.tasksDoneCount.incrementAndGet();
		int total = this.tasksTotalCount.get();

		// Compute the progress and notify the listeners
		this.notifyListenersSpiderProgress(done / total, done, total - done);

		// Check for ending conditions
		if (done == total && initialized == true)
			this.complete();
	}

	/**
	 * Checks if is paused.
	 * 
	 * @return true, if is paused
	 */
	public boolean isPaused() {
		return this.paused;
	}

	/**
	 * Checks if is stopped, i.e. a shutdown was issued or it is not running.
	 * 
	 * @return true, if is stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * Checks if is terminated.
	 * 
	 * @return true, if is terminated
	 */
	public boolean isTerminated() {
		return threadPool.isTerminated();
	}

	/* LISTENERS SECTION */

	/**
	 * Adds a new spider listener.
	 * 
	 * @param listener the listener
	 */
	public void addSpiderListener(SpiderListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Removes a spider listener.
	 * 
	 * @param listener the listener
	 */
	public void removeSpiderListener(SpiderListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Notifies all the listeners regarding the spider progress.
	 * 
	 * @param percentageComplete the percentage complete
	 * @param numberCrawled the number of pages crawled
	 * @param numberToCrawl the number of pages left to crawl
	 */
	protected synchronized void notifyListenersSpiderProgress(int percentageComplete, int numberCrawled,
			int numberToCrawl) {
		for (SpiderListener l : listeners)
			l.spiderProgress(percentageComplete, numberCrawled, numberToCrawl);
	}

	/**
	 * Notifies the listeners regarding a found uri.
	 * 
	 * @param uri the uri
	 * @param method the method used for fetching the resource
	 * @param status the {@link FetchStatus} stating if this uri will be processed, and, if not,
	 *            stating the reason of the filtering
	 */
	protected synchronized void notifyListenersFoundURI(String uri, String method, FetchStatus status) {
		for (SpiderListener l : listeners)
			l.foundURI(uri, method, status);
	}

	/**
	 * Notifies the listeners regarding a read uri.
	 * 
	 * @param msg the msg
	 */
	protected synchronized void notifyListenersReadURI(HttpMessage msg) {
		for (SpiderListener l : listeners)
			l.readURI(msg);
	}

	/**
	 * Notifies the listeners that the spider is complete.
	 */
	protected synchronized void notifyListenersSpiderComplete(boolean successful) {
		for (SpiderListener l : listeners)
			l.spiderComplete(successful);
	}

}
