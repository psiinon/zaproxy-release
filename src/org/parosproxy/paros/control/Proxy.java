/*
 * Created on May 30, 2004
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
// ZAP: 2011/05/15 Support for exclusions
// ZAP: 2012/11/01 Issue 411: Allow proxy port to be specified on the command line
// ZAP: 2012/12/27 Added method addPersistentConnectionListener(...)
// ZAP: 2013/01/25 Added method removeProxyListener()
// ZAP: 2013/08/30 Issue 775: Allow host to be set via the command line
package org.parosproxy.paros.control;
 
import java.util.List;

import org.parosproxy.paros.core.proxy.CacheProcessingItem;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.core.proxy.ProxyServer;
import org.parosproxy.paros.core.proxy.ProxyServerSSL;
import org.parosproxy.paros.model.Model;
import org.zaproxy.zap.PersistentConnectionListener;
import org.zaproxy.zap.control.ControlOverrides;

public class Proxy {
    
	private Model model = null;
	private ProxyServer proxyServer = null;
	private ProxyServerSSL proxyServerSSL = null;
	private boolean reverseProxy = false;
	private String reverseProxyHost = "";
	private ControlOverrides overrides = null;
    
	public Proxy(Model model, ControlOverrides overrides) {
		
	    this.model = model;
	    this.overrides = overrides;
	    
		proxyServer = new ProxyServer();
		proxyServerSSL = new ProxyServerSSL();

	}
	
	public void startServer() {

		// setProxyParam put in here so restart can reread param.
		proxyServer.setProxyParam(model.getOptionsParam().getProxyParam());
		proxyServer.setConnectionParam(model.getOptionsParam().getConnectionParam());

		proxyServerSSL.setProxyParam(model.getOptionsParam().getProxyParam());
		proxyServerSSL.setConnectionParam(model.getOptionsParam().getConnectionParam());

		if (model.getOptionsParam().getProxyParam().isUseReverseProxy()) {

//		    int sslPort = proxyServerSSL.startServer(model.getOptionsParam().getProxyParam().getReverseProxyIp(), model.getOptionsParam().getProxyParam().getReverseProxyHttpsPort(), false);
		    proxyServerSSL.startServer(model.getOptionsParam().getProxyParam().getReverseProxyIp(), model.getOptionsParam().getProxyParam().getReverseProxyHttpsPort(), false);

//			proxyServer.setForwardPort(sslPort);
		    
			proxyServer.startServer(model.getOptionsParam().getProxyParam().getReverseProxyIp(), model.getOptionsParam().getProxyParam().getReverseProxyHttpPort(), false);
		    
		} else {

//		    int sslPort = proxyServerSSL.startServer(model.getOptionsParam().getProxyParam().getProxyIp(), model.getOptionsParam().getProxyParam().getProxySSLPort(), true);
//			proxyServer.setForwardPort(sslPort);

			String proxyHost = null;
		    int proxyPort = -1;
		    if (this.overrides != null) {
		    	proxyHost = this.overrides.getProxyHost();
		    	proxyPort = this.overrides.getProxyPort();
		    }
		    if (proxyHost != null) {
		    	// Save the override in the configs
		    	model.getOptionsParam().getProxyParam().setProxyIp(proxyHost);
		    } else {
                        // ZAP: get the proxy IP as set without any check for nullable
		    	proxyHost = model.getOptionsParam().getProxyParam().getRawProxyIP();
		    }
		    if (proxyPort > 0) {
		    	// Save the override in the configs
		    	model.getOptionsParam().getProxyParam().setProxyPort(proxyPort);
		    } else {
		    	proxyPort = model.getOptionsParam().getProxyParam().getProxyPort();
		    }

			proxyServer.startServer(proxyHost, proxyPort, false);
		    
		}
	}

	public void stopServer() {
        if (model.getOptionsParam().getProxyParam().isUseReverseProxy()) {
            proxyServerSSL.stopServer();
            proxyServer.stopServer();

        } else {
            proxyServer.stopServer();
        }
	}
	
	public void setSerialize(boolean serialize) {
	    proxyServer.setSerialize(serialize);
	    proxyServerSSL.setSerialize(serialize);
	}
	
	public void addProxyListener(ProxyListener listener) {
	    proxyServer.addProxyListener(listener);
	    proxyServerSSL.addProxyListener(listener);
	}
	
	public void removeProxyListener(ProxyListener listener) {
	    proxyServer.removeProxyListener(listener);
	    proxyServerSSL.removeProxyListener(listener);
	}
	
	public void addPersistentConnectionListener(PersistentConnectionListener listener) {
	    proxyServer.addPersistentConnectionListener(listener);
	    proxyServerSSL.addPersistentConnectionListener(listener);
	}

	public void removePersistentConnectionListener(PersistentConnectionListener listener) {
	    proxyServer.removePersistentConnectionListener(listener);
	    proxyServerSSL.removePersistentConnectionListener(listener);
	}

    /**
     * @return Returns the reverseProxy.
     */
    public boolean isReverseProxy() {
        return reverseProxy;
    }
    /**
     * @param reverseProxy The reverseProxy to set.
     */
    public void setReverseProxy(boolean reverseProxy) {
        this.reverseProxy = reverseProxy;
    }
    /**
     * @return Returns the reverseProxyHost.
     */
    public String getReverseProxyHost() {
        return reverseProxyHost;
    }
    /**
     * @param reverseProxyHost The reverseProxyHost to set.
     */
    public void setReverseProxyHost(String reverseProxyHost) {
        this.reverseProxyHost = reverseProxyHost;
    }

    /**
     * @param enableCacheProcessing The enableCacheProcessing to set.
     */
    public void setEnableCacheProcessing(boolean enableCacheProcessing) {
        if (proxyServer != null) {
            proxyServer.setEnableCacheProcessing(enableCacheProcessing);
        }
        
        if (proxyServerSSL != null) {
            proxyServerSSL.setEnableCacheProcessing(enableCacheProcessing);
        }

    }
    
    public void addCacheProcessingList(CacheProcessingItem item) {
        if (proxyServer != null) {
            proxyServer.addCacheProcessingList(item);
        }

        if (proxyServerSSL != null) {
            proxyServerSSL.addCacheProcessingList(item);
        }

        
    }

	public void setIgnoreList(List<String> urls) {
        if (proxyServer != null) {
            proxyServer.setExcludeList(urls);
        }

        if (proxyServerSSL != null) {
            proxyServerSSL.setExcludeList(urls);
        }
		
	}
}
