/*
* Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2010 psiinon@gmail.com
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
package org.zaproxy.zap.extension.help;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.view.ZapMenuItem;

/**
 * Loads the core help files and provides GUI elements to access them.
 */
public class ExtensionHelp extends ExtensionAdaptor {

	private static final String HELP_SET_FILE_NAME = "helpset";

	private ZapMenuItem menuHelpZap = null;
	private JButton helpButton = null;

	private static HelpSet hs = null;
	private static HelpBroker hb = null;

	private static final Logger logger = Logger.getLogger(ExtensionHelp.class);
	
    public ExtensionHelp() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionHelp(String name) {
        super(name);
    }

	/**
	 * This method initializes this
	 */
	private void initialize() {
        this.setName("ExtensionHelp");
        this.setOrder(10000);	// Set to a huge value so the help button is always on the far right of the toolbar 
	}
	
	@Override
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);

	    if (getView() != null) {	        
	        extensionHook.getHookMenu().addHelpMenuItem(getMenuHelpZapUserGuide());

	        View.getSingleton().addMainToolbarSeparator();
	        View.getSingleton().addMainToolbarButton(this.getHelpButton());

	    }

	}
	
	public static HelpBroker getHelpBroker() {
		if (hb == null) {
			createHelpBroker();
		}
		return hb;
	}
	
	private static synchronized void createHelpBroker() {
		if (hb == null) {
			try {
				ClassLoader cl = HelpBroker.class.getClassLoader();  
				URL hsUrl = HelpSet.findHelpSet( cl, HELP_SET_FILE_NAME, Constant.getLocale());
				if (hsUrl != null) {
					hs = new HelpSet(cl, hsUrl);
					hb = hs.createHelpBroker();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public static void enableHelpKey (Component component, String key) {
		if (getHelpBroker() != null) {
			hb.enableHelp(component, key, hs);
		}
	}

	/*
	public static void enablePopupHelpKey (Component component, String key) {
		if (getHelpBroker() != null) {
			hb.enableHelpKey(component, 
					"zap.intro", hs, "javax.help.SecondaryWindow", null);
			hb.enableHelp(component, key, hs);

		}
	}
	*/

	/**
	 * @see #showHelp(String)
	 */
	public static void showHelp() {
		showHelp("zap.intro");
	}
	
	/**
	 * Shows a specific help topic
	 * 
	 * @param helpindex
	 */
	public static void showHelp(String helpindex) {
		try {
			getHelpBroker().showID(helpindex, "javax.help.SecondaryWindow", null);
		} catch (Exception e) {
			logger.error("error loading help with index: " + helpindex, e);
		}
	}

	private ZapMenuItem getMenuHelpZapUserGuide() {
		if (menuHelpZap == null) {
			menuHelpZap = new ZapMenuItem("help.menu.guide",
					KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false));
			
			if (getHelpBroker() != null) {

				// Set up the help menu item
				menuHelpZap.addActionListener(
						new CSH.DisplayHelpFromFocus(hb));

				// Enable the top level F1 help key
				hb.enableHelpKey(this.getView().getMainFrame().getRootPane(), 
						"zap.intro", hs, "javax.help.SecondaryWindow", null);

				// Register all of the main tabs

				hb.enableHelp(this.getView().getSiteTreePanel(), 
						"ui.tabs.sites", hs);
				
				hb.enableHelp(this.getView().getRequestPanel(), 
						"ui.tabs.request", hs);
				hb.enableHelp(this.getView().getResponsePanel(), 
						"ui.tabs.response", hs);

			} else {
				logger.error("Failed to get helpset url");
			}
		}
		return menuHelpZap;
	}
	
	private JButton getHelpButton() {
		if (helpButton == null) {
			helpButton = new JButton();
			helpButton.setIcon(new ImageIcon(ExtensionHelp.class.getResource("/resource/icon/16/201.png")));
			helpButton.setToolTipText(Constant.messages.getString("help.button.tooltip"));
			helpButton.addActionListener(new java.awt.event.ActionListener() { 
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showHelp();
				}
			});
		}
		return helpButton;
	}

	@Override
	public String getAuthor() {
		return Constant.ZAP_TEAM;
	}

	@Override
	public String getDescription() {
		return Constant.messages.getString("help.desc");
	}

	@Override
	public URL getURL() {
		try {
			return new URL(Constant.ZAP_HOMEPAGE);
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
