/*
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2003-2004 Chinotec Technologies Company
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Clarified Artistic License
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Clarified Artistic License for more details.
 * 
 * You should have received a copy of the Clarified Artistic License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
// ZAP: 2011/04/16 Support for running ZAP as a daemon
// ZAP: 2011/05/09 Support for API
// ZAP: 2011/05/15 Support for exclusions
package org.parosproxy.paros.control;

import java.util.List;

import org.apache.log4j.Logger;
import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.extension.edit.ExtensionEdit;
import org.parosproxy.paros.extension.filter.ExtensionFilter;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.extension.manualrequest.ExtensionManualRequestEditor;
import org.parosproxy.paros.extension.option.ExtensionOption;
import org.parosproxy.paros.extension.report.ExtensionReport;
import org.parosproxy.paros.extension.state.ExtensionState;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.anticsrf.ExtensionAntiCSRF;
import org.zaproxy.zap.extension.api.ExtensionAPI;
import org.zaproxy.zap.extension.ascan.ExtensionActiveScan;
import org.zaproxy.zap.extension.autoupdate.ExtensionAutoUpdate;
import org.zaproxy.zap.extension.beanshell.ExtensionBeanShell;
import org.zaproxy.zap.extension.brk.ExtensionBreak;
import org.zaproxy.zap.extension.bruteforce.ExtensionBruteForce;
import org.zaproxy.zap.extension.compare.ExtensionCompare;
import org.zaproxy.zap.extension.copy.ExtensionCopy;
import org.zaproxy.zap.extension.dynssl.ExtensionDynSSL;
import org.zaproxy.zap.extension.encoder2.ExtensionEncoder2;
import org.zaproxy.zap.extension.fuzz.ExtensionFuzz;
import org.zaproxy.zap.extension.help.ExtensionHelp;
import org.zaproxy.zap.extension.invoke.ExtensionInvoke;
import org.zaproxy.zap.extension.log4j.ExtensionLog4j;
import org.zaproxy.zap.extension.portscan.ExtensionPortScan;
import org.zaproxy.zap.extension.pscan.ExtensionPassiveScan;
import org.zaproxy.zap.extension.reveal.ExtensionReveal;
import org.zaproxy.zap.extension.search.ExtensionSearch;
import org.zaproxy.zap.extension.spider.ExtensionSpider;



/**
 *
 * Overall control with interaction on model and view.
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Control extends AbstractControl {

    private static Logger log = Logger.getLogger(Control.class);

    private static Control control = null;
    private Proxy proxy = null;
    private MenuFileControl menuFileControl = null;
    private MenuToolsControl menuToolsControl = null;
    
    private Control(Model model, View view) {
        super(model, view);
        init();
    }
    
    private void init() {
        
        PluginFactory.loadAllPlugin(model.getOptionsParam().getConfig());
        		
		// start plugin loading
		loadExtension();
		
		// ZAP: Start proxy even if no view
	    getProxy();
	    getExtensionLoader().hookProxyListener(getProxy());
		
		if (view != null) {
		    // ZAP: Add site map listeners
		    getExtensionLoader().hookSiteMapListener(view.getSiteTreePanel());
		}
		
		getProxy().startServer();
    }
    
    public Proxy getProxy() {
        if (proxy == null) {
            proxy = new Proxy(model);
        }
        
        return proxy;
    }
    
    protected void addExtension() {

        // should be the first ext to load
        getExtensionLoader().addExtension(new ExtensionOption());
        getExtensionLoader().addExtension(new ExtensionEdit());
        getExtensionLoader().addExtension(new ExtensionCopy());

        getExtensionLoader().addExtension(new ExtensionFilter());
        
        // ZAP: Lots of changes here!
        getExtensionLoader().addExtension(new ExtensionAPI());

        // Replaced extensions
        //getExtensionLoader().addExtension(new ExtensionPatternSearch());
        //getExtensionLoader().addExtension(new ExtensionTrap());
        //getExtensionLoader().addExtension(new ExtensionEncoder());
        //getExtensionLoader().addExtension(new ExtensionScanner());

        getExtensionLoader().addExtension(new ExtensionState());

        // Moved report extension above history so Generate Report is above the Exports 
        getExtensionLoader().addExtension(new ExtensionReport());
        getExtensionLoader().addExtension(new ExtensionHistory());

        getExtensionLoader().addExtension(new ExtensionReveal());

        getExtensionLoader().addExtension(new ExtensionSearch());
        getExtensionLoader().addExtension(new ExtensionEncoder2());
        getExtensionLoader().addExtension(new ExtensionBreak());
        
        getExtensionLoader().addExtension(new ExtensionActiveScan());
        getExtensionLoader().addExtension(new ExtensionPassiveScan());
        // This is now the ZAP spider extension
        getExtensionLoader().addExtension(new ExtensionSpider());
        getExtensionLoader().addExtension(new ExtensionBruteForce());
        getExtensionLoader().addExtension(new ExtensionPortScan());
        
        getExtensionLoader().addExtension(new ExtensionManualRequestEditor());
        getExtensionLoader().addExtension(new ExtensionBeanShell());
        // This is now the ZAP autoupdate extension
        getExtensionLoader().addExtension(new ExtensionAutoUpdate());

        getExtensionLoader().addExtension(new ExtensionHelp());
        getExtensionLoader().addExtension(new ExtensionCompare());
        getExtensionLoader().addExtension(new ExtensionInvoke());
//        getExtensionLoader().addExtension(new ExtensionHexView());
        getExtensionLoader().addExtension(new ExtensionFuzz());
        getExtensionLoader().addExtension(new ExtensionAntiCSRF());
        
        // ZAP: adding connection SSL options right after regular ones
        getExtensionLoader().addExtension(new ExtensionDynSSL());

        getExtensionLoader().addExtension(new ExtensionLog4j());

        //getExtensionLoader().addExtension(new ExtensionDiff());

    }
    
    public MenuFileControl getMenuFileControl() {
        if (menuFileControl == null) {
            menuFileControl = new MenuFileControl(model, view, this);
        }

        return menuFileControl;
    }
    

    public MenuToolsControl getMenuToolsControl() {
        if (menuToolsControl == null) {
            menuToolsControl = new MenuToolsControl(model, view, this);                
        }
        return menuToolsControl;
    }

    /**
     * Override inherited shutdown to add stopping proxy servers.
     */
    public void shutdown(boolean compact) {
        getProxy().stopServer();
        super.shutdown(compact);
    }
    
    public static Control getSingleton() {

        return control;
    }

    public static void initSingletonWithView() {
        control = new Control(Model.getSingleton(), View.getSingleton());
    }
    
    public static void initSingletonWithoutView() {
        control = new Control(Model.getSingleton(), null);
    }

    
    public void runCommandLineNewSession(String fileName) throws Exception {
		
		Session session = new Session(model);
	    log.info("new session file created");
	    model.setSession(session);
		control.getExtensionLoader().sessionChangedAllPlugin(session);

		if (!fileName.endsWith(".session")) {
		    fileName += ".session";
		}
		session.save(fileName);
		getExtensionLoader().runCommandLine();
	}
    
    public void runCommandLineOpenSession(String fileName) {
		
		Session session = new Session(model);
	    log.info("new session file created");
	    model.setSession(session);
		control.getExtensionLoader().sessionChangedAllPlugin(session);
		
    }

    public void setExcludeFromProxyUrls(List<String> urls) {
		this.getProxy().setIgnoreList(urls);
    }

}
