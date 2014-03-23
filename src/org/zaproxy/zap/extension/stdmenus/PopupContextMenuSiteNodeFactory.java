/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2012 The ZAP Development Team
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
package org.zaproxy.zap.extension.stdmenus;

import java.util.ArrayList;
import java.util.List;

import org.parosproxy.paros.extension.ExtensionPopupMenuItem;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.view.PopupMenuSiteNode;

/**
 * @deprecated (2.3.0) Superseded by {@link org.zaproxy.zap.view.popup.PopupMenuItemSiteNodeContextMenuFactory}. It will be removed in a future release.
 */
@Deprecated
public abstract class PopupContextMenuSiteNodeFactory extends PopupMenuSiteNode {

	private static final long serialVersionUID = 2282358266003940700L;

    private List<ExtensionPopupMenuItem> subMenus = new ArrayList<>();
    private String parentMenu;

	/**
	 * This method initializes 
	 * 
	 */
	public PopupContextMenuSiteNodeFactory(String parentMenu) {
		super("ContextMenuFactory", true);
		this.parentMenu = parentMenu;
	}
	/**/
    @Override
    public String getParentMenuName() {
    	return parentMenu;
    }
    
    @Override
    public boolean isSubMenu() {
    	return true;
    }
    @Override
    public boolean isDummyItem () {
    	return true;
    }
	    
	@Override
	public void performAction(SiteNode sn) throws Exception {
		// Do nothing
	}

	@Override
    public void performActions (List<HistoryReference> hrefs) throws Exception {
	}

	@Override
	public boolean isEnableForInvoker(Invoker invoker) {
		return true;
	}

	@Override
    public boolean isEnabledForSiteNode (SiteNode sn) {
		for (ExtensionPopupMenuItem menu : subMenus) {
			View.getSingleton().getPopupMenu().removeMenu(menu);
			
		}
		subMenus.clear();
		
		// Add the existing contexts
        Session session = Model.getSingleton().getSession();
        List<Context> contexts = session.getContexts();
        for (Context context : contexts) {
			ExtensionPopupMenuItem piicm = getContextMenu(context, this.parentMenu);
        	piicm.setMenuIndex(this.getMenuIndex());
			View.getSingleton().getPopupMenu().addMenu(piicm);
			this.subMenus.add(piicm);
        }
    	return false;
    }
	
	public abstract ExtensionPopupMenuItem getContextMenu(Context context, String parentMenu);

    @Override
    public boolean isSafe() {
    	return true;
    }
}
