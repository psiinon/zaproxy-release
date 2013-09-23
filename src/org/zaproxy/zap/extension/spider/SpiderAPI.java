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
package org.zaproxy.zap.extension.spider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.api.ApiAction;
import org.zaproxy.zap.extension.api.ApiException;
import org.zaproxy.zap.extension.api.ApiException.Type;
import org.zaproxy.zap.extension.api.ApiImplementor;
import org.zaproxy.zap.extension.api.ApiResponse;
import org.zaproxy.zap.extension.api.ApiResponseElement;
import org.zaproxy.zap.extension.api.ApiResponseList;
import org.zaproxy.zap.extension.api.ApiView;
import org.zaproxy.zap.extension.users.ExtensionUserManagement;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.model.ScanListenner;
import org.zaproxy.zap.spider.SpiderListener;
import org.zaproxy.zap.spider.filters.FetchFilter.FetchStatus;
import org.zaproxy.zap.users.User;
import org.zaproxy.zap.utils.ApiUtils;

public class SpiderAPI extends ApiImplementor implements ScanListenner, SpiderListener {

	private static final Logger log = Logger.getLogger(SpiderAPI.class);

	/** The Constant PREFIX defining the name/prefix of the api. */
	private static final String PREFIX = "spider";
	/** The Constant ACTION_START_SCAN that defines the action of starting a new scan. */
	private static final String ACTION_START_SCAN = "scan";
	private static final String ACTION_START_SCAN_AS_USER = "scanAsUser";

	/** The Constant ACTION_STOP_SCAN that defines the action of stopping a pending scan. */
	private static final String ACTION_STOP_SCAN = "stop";

	/**
	 * The Constant VIEW_STATUS that defines the view which describes the current status of the
	 * scan.
	 */
	private static final String VIEW_STATUS = "status";

	/**
	 * The Constant VIEW_RESULTS that defines the view which describes the urls found during the
	 * scan.
	 */
	private static final String VIEW_RESULTS = "results";

	/**
	 * The Constant PARAM_URL that defines the parameter defining the url of the scan.
	 */
	private static final String PARAM_URL = "url";
	private static final String PARAM_USER_ID = "userId";
	private static final String PARAM_CONTEXT_ID = "contextId";
	private static final String PARAM_REGEX = "regex";

	private static final String ACTION_EXCLUDE_FROM_SCAN = "excludeFromScan";
	private static final String ACTION_CLEAR_EXCLUDED_FROM_SCAN = "clearExcludedFromScan";

	private static final String VIEW_EXCLUDED_FROM_SCAN = "excludedFromScan";

	/** The spider extension. */
	private ExtensionSpider extension;

	/** The spider thread. */
	private SpiderThread spiderThread;

	/** The current progress of the spider. */
	private int progress;

	/** The URIs found during the scan. */
	private List<String> foundURIs;

	/**
	 * Instantiates a new spider API.
	 * 
	 * @param extension the extension
	 */
	public SpiderAPI(ExtensionSpider extension) {
		this.extension = extension;
		this.foundURIs = new ArrayList<>();
		// Register the actions
		this.addApiAction(new ApiAction(ACTION_START_SCAN, new String[] { PARAM_URL }));
		this.addApiAction(new ApiAction(ACTION_START_SCAN_AS_USER, new String[] { PARAM_URL,
				PARAM_CONTEXT_ID, PARAM_USER_ID }));
		this.addApiAction(new ApiAction(ACTION_STOP_SCAN));
		this.addApiAction(new ApiAction(ACTION_CLEAR_EXCLUDED_FROM_SCAN));
		this.addApiAction(new ApiAction(ACTION_EXCLUDE_FROM_SCAN, new String[] { PARAM_REGEX }));

		// Register the views
		this.addApiView(new ApiView(VIEW_STATUS));
		this.addApiView(new ApiView(VIEW_RESULTS));
		this.addApiView(new ApiView(VIEW_EXCLUDED_FROM_SCAN));

	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}

	@Override
	public ApiResponse handleApiAction(String name, JSONObject params) throws ApiException {
		log.debug("Request for handleApiAction: " + name + " (params: " + params.toString() + ")");

		switch (name) {
		case ACTION_START_SCAN:
			// The action is to start a new Scan
			String url = ApiUtils.getNonEmptyStringParam(params, PARAM_URL);
			scanURL(url, null);
			break;
		case ACTION_START_SCAN_AS_USER:
			// The action is to start a new Scan from the perspective of a user
			String urlUserScan = ApiUtils.getNonEmptyStringParam(params, PARAM_URL);
			int userID = ApiUtils.getIntParam(params, PARAM_USER_ID);
			ExtensionUserManagement usersExtension = (ExtensionUserManagement) Control.getSingleton()
					.getExtensionLoader().getExtension(ExtensionUserManagement.NAME);
			if (usersExtension == null)
				throw new ApiException(Type.NO_IMPLEMENTOR, ExtensionUserManagement.NAME);
			Context context = ApiUtils.getContextByParamId(params, PARAM_CONTEXT_ID);
			if (!context.isIncluded(urlUserScan))
				throw new ApiException(Type.URL_NOT_IN_CONTEXT, PARAM_CONTEXT_ID);
			User user = usersExtension.getContextUserAuthManager(context.getIndex()).getUserById(userID);
			if (user == null)
				throw new ApiException(Type.USER_NOT_FOUND, PARAM_USER_ID);
			scanURL(urlUserScan, user);
			break;

		case ACTION_STOP_SCAN:
			// The action is to stop a pending scan
			if (spiderThread != null) {
				spiderThread.stopScan();
			}
			break;
		case ACTION_CLEAR_EXCLUDED_FROM_SCAN:
			try {
				Session session = Model.getSingleton().getSession();
				session.setExcludeFromSpiderRegexs(new ArrayList<String>());
			} catch (SQLException e) {
				throw new ApiException(ApiException.Type.INTERNAL_ERROR, e.getMessage());
			}
			break;
		case ACTION_EXCLUDE_FROM_SCAN:
			String regex = params.getString(PARAM_REGEX);
			try {
				Session session = Model.getSingleton().getSession();
				session.addExcludeFromSpiderRegex(regex);
			} catch (Exception e) {
				throw new ApiException(ApiException.Type.BAD_FORMAT, PARAM_REGEX);
			}
			break;
		default:
			throw new ApiException(ApiException.Type.BAD_ACTION);
		}
		return ApiResponseElement.OK;
	}

	private boolean scanInProgress() {
		return spiderThread != null && !spiderThread.isStopped();
	}

	/**
	 * Start the scan of an url.
	 * 
	 * @param url the url to scan
	 * @param scanUser the user to scan as, or null if the scan is done without the perspective of
	 *            any user
	 * @throws ApiException the api exception
	 */
	private void scanURL(String url, User scanUser) throws ApiException {
		log.debug("API Spider scanning url: " + url);
		if (scanInProgress()) {
			throw new ApiException(ApiException.Type.SCAN_IN_PROGRESS);
		}

		// Try to build uri
		try {
			URI startURI = new URI(url, true);
			SiteNode startNode = Model.getSingleton().getSession().getSiteTree().findNode(startURI);
			String scheme = startURI.getScheme();
			if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
				throw new ApiException(ApiException.Type.BAD_FORMAT);
			}

			spiderThread = new SpiderThread(extension, "API", this);

			if (startNode != null) {
				spiderThread.setStartNode(startNode);
			} else {
				spiderThread.setStartURI(startURI);
			}

			if (scanUser != null)
				spiderThread.setScanAsUser(scanUser);

			// Start the scan
			this.foundURIs.clear();
			this.progress = 0;
			spiderThread.addSpiderListener(this);
			spiderThread.start();

		} catch (URIException e) {
			throw new ApiException(ApiException.Type.BAD_FORMAT);
		}
	}

	@Override
	public ApiResponse handleApiView(String name, JSONObject params) throws ApiException {
		ApiResponse result;
		if (VIEW_STATUS.equals(name)) {
			result = new ApiResponseElement(name, Integer.toString(progress));
		} else if (VIEW_RESULTS.equals(name)) {
			result = new ApiResponseList(name);
			for (String s : foundURIs) {
				((ApiResponseList) result).addItem(new ApiResponseElement("url", s));
			}
		} else if (VIEW_EXCLUDED_FROM_SCAN.equals(name)) {
			result = new ApiResponseList(name);
			Session session = Model.getSingleton().getSession();
			List<String> regexs = session.getExcludeFromSpiderRegexs();
			for (String regex : regexs) {
				((ApiResponseList) result).addItem(new ApiResponseElement("regex", regex));
			}
		} else {
			throw new ApiException(ApiException.Type.BAD_VIEW);
		}
		return result;
	}

	@Override
	public void scanFinshed(String host) {
		// Do nothing
		// TODO: should remove scan listener
	}

	@Override
	public void scanProgress(String host, int progress, int maximum) {
		// Do nothing
	}

	@Override
	public void spiderProgress(int percentageComplete, int numberCrawled, int numberToCrawl) {
		this.progress = percentageComplete;
	}

	@Override
	public void foundURI(String uri, String method, FetchStatus status) {
		if (status.equals(FetchStatus.VALID) || status.equals(FetchStatus.SEED))
			foundURIs.add(uri);
	}

	@Override
	public void readURI(HttpMessage msg) {
		// Ignore
	}

	@Override
	public void spiderComplete(boolean successful) {
		this.progress = 100;
	}

}
