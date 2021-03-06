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
package org.zaproxy.zap.extension.stdmenus;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionLoader;
import org.zaproxy.zap.extension.ascan.ExtensionActiveScan;
import org.zaproxy.zap.extension.spider.ExtensionSpider;
import org.zaproxy.zap.extension.users.ExtensionUserManagement;
import org.zaproxy.zap.view.popup.PopupMenuItemContextExclude;
import org.zaproxy.zap.view.popup.PopupMenuItemContextInclude;
import org.parosproxy.paros.extension.history.ExtensionHistory;

public class ExtensionStdMenus extends ExtensionAdaptor implements ClipboardOwner {

	public static final String NAME = "ExtensionStandardMenus";

    private PopupCopyMenu popupCopyMenu = null;
    private PopupPasteMenu popupPaste = null;
	private PopupMenuActiveScanScope popupMenuActiveScanScope = null;
	private PopupMenuActiveScanNode popupMenuActiveScanSubtree = null;
	private PopupMenuActiveScanSite popupMenuActiveScanSite = null;
	private PopupMenuActiveScanCustom popupMenuActiveScanCustom = null;
	private PopupMenuSpiderURL popupMenuSpiderURL = null;
	private PopupMenuSpiderScope popupMenuSpiderScope = null;
	private PopupMenuSpiderSite popupMenuSpiderSite = null;
	private PopupMenuSpiderContext popupMenuSpiderContext = null;
	private PopupMenuSpiderContextAsUser popupMenuSpiderContextAsUser =null;
	private PopupMenuSpiderSubtree popupMenuSpiderSubtree = null;
	private PopupMenuSpiderURLAsUser popupMenuSpiderURLAsUser = null;
	private PopupMenuActiveScanURL popupMenuActiveScanURL = null;
	private PopupExcludeFromProxyMenu popupExcludeFromProxyMenu = null;
	private PopupExcludeFromScanMenu popupExcludeFromScanMenu = null;
	private PopupExcludeFromSpiderMenu popupExcludeFromSpiderMenu = null;
	private PopupMenuResendMessage popupMenuResendMessage = null;
	private PopupMenuShowInHistory popupMenuShowInHistory = null;
	private PopupMenuShowInSites popupMenuShowInSites = null;
	private PopupMenuOpenUrlInBrowser popupMenuOpenUrlInBrowser = null;
	private PopupMenuItemContextInclude popupContextIncludeMenu = null;
	private PopupMenuItemContextExclude popupContextExcludeMenu = null;
	private PopupMenuCopyUrls popupMenuCopyUrls = null;

	// Still being developed
	// private PopupMenuShowResponseInBrowser popupMenuShowResponseInBrowser = null;
	private PopupMenuAlert popupMenuAlert = null;

    private static Logger log = Logger.getLogger(ExtensionStdMenus.class);

	public ExtensionStdMenus() {
		super();
		initialize();
	}

	private void initialize() {
		this.setName(NAME);
		this.setOrder(31);
	}

	@Override
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    
		if (getView() != null) {
	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuCopy());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuPaste());

			final ExtensionLoader extensionLoader = Control.getSingleton().getExtensionLoader();
			boolean isExtensionHistoryEnabled = extensionLoader.isExtensionEnabled(ExtensionHistory.NAME);
			boolean isExtensionActiveScanEnabled = extensionLoader.isExtensionEnabled(ExtensionActiveScan.NAME);
			boolean isExtensionSpiderEnabled = extensionLoader.isExtensionEnabled(ExtensionSpider.NAME);
			// Be careful when changing the menu indexes (and order above) - its easy to get unexpected
			// results!
			extensionHook.getHookMenu().addPopupMenuItem(getPopupExcludeFromProxyMenu(0));
			if (isExtensionActiveScanEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupExcludeFromScanMenu(0));
			}
			if (isExtensionSpiderEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupExcludeFromSpiderMenu(0));
			}
			extensionHook.getHookMenu().addPopupMenuItem(getPopupContextIncludeMenu(1));
			extensionHook.getHookMenu().addPopupMenuItem(getPopupContextExcludeMenu(2));

			if (isExtensionActiveScanEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanScope(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanSite(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanSubtree(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanURL(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanCustom(3));
			}

			if (isExtensionSpiderEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderContext(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderScope(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderSite(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderSubtree(3));
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderURL(3));
				//Enable some popup menus only if some extensions are enabled 
				if(extensionLoader.isExtensionEnabled(ExtensionUserManagement.NAME)){
					extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderURLAsUser(3));
					extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuSpiderContextAsUser(3));
				}
			}

			if (isExtensionHistoryEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuResendMessage(4));
			}

			extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuAlert(5));

			if (isExtensionHistoryEnabled) {
				extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuShowInHistory(6)); // Both are index 6
			}
			extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuShowInSites(6)); // on purpose ;)
			extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuOpenUrlInBrowser(7));
			extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuCopyUrls(8));
			// extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuShowResponseInBrowser(7));

		}
	}

    private PopupCopyMenu getPopupMenuCopy() {
        if (popupCopyMenu== null) {
            popupCopyMenu = new PopupCopyMenu();
            popupCopyMenu.setText(Constant.messages.getString("copy.copy.popup"));
            popupCopyMenu.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setClipboardContents(popupCopyMenu.getLastInvoker().getSelectedText());
                }
            });
        }
        return popupCopyMenu;
    }
    
    private PopupPasteMenu getPopupMenuPaste() {
        if (popupPaste == null) {
            popupPaste = new PopupPasteMenu();
            popupPaste.setText(Constant.messages.getString("paste.paste.popup"));
            popupPaste.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    popupPaste.getLastInvoker().setText(popupPaste.getLastInvoker().getText() + getClipboardContents());


                }
            });
        }
        return popupPaste;
    }

    private String getClipboardContents() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                log.error("Unable to get data from clipboard");
            }
        }

        return "";
    }

	private void setClipboardContents (String str) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents( new StringSelection(str), this );
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// Ignore
	}

	private PopupMenuSpiderURL getPopupMenuSpiderURL(int menuIndex) {
		if (popupMenuSpiderURL == null) {
			popupMenuSpiderURL = new PopupMenuSpiderURL(Constant.messages.getString("spider.url.popup"));
			popupMenuSpiderURL.setMenuIndex(menuIndex);
		}
		return popupMenuSpiderURL;
	}

	private PopupMenuSpiderContext getPopupMenuSpiderContext(int menuIndex) {
		if (popupMenuSpiderContext == null) {
			popupMenuSpiderContext = new PopupMenuSpiderContext(Constant.messages.getString("spider.context.popup"));
		}
		return popupMenuSpiderContext;
	}
	
	private PopupMenuSpiderURLAsUser getPopupMenuSpiderURLAsUser(int menuIndex) {
		if (popupMenuSpiderURLAsUser == null) {
			popupMenuSpiderURLAsUser = new PopupMenuSpiderURLAsUser(Constant.messages.getString("spider.url.user.popup"));
		}
		return popupMenuSpiderURLAsUser;
	}
	
	private PopupMenuSpiderContextAsUser getPopupMenuSpiderContextAsUser(int menuIndex) {
		if (popupMenuSpiderContextAsUser == null) {
			popupMenuSpiderContextAsUser = new PopupMenuSpiderContextAsUser(Constant.messages.getString("spider.context.user.popup"));
		}
		return popupMenuSpiderContextAsUser;
	}

	private PopupMenuSpiderSite getPopupMenuSpiderSite(int menuIndex) {
		if (popupMenuSpiderSite == null) {
			popupMenuSpiderSite = new PopupMenuSpiderSite(Constant.messages.getString("spider.site.popup"));
		}
		return popupMenuSpiderSite;
	}

	private PopupMenuSpiderScope getPopupMenuSpiderScope(int menuIndex) {
		if (popupMenuSpiderScope == null) {
			popupMenuSpiderScope = new PopupMenuSpiderScope(Constant.messages.getString("spider.scope.popup"));
		}
		return popupMenuSpiderScope;
	}

	private PopupMenuSpiderSubtree getPopupMenuSpiderSubtree(int menuIndex) {
		if (popupMenuSpiderSubtree == null) {
			popupMenuSpiderSubtree = new PopupMenuSpiderSubtree(Constant.messages.getString("spider.subtree.popup"));
		}
		return popupMenuSpiderSubtree;
	}

	private PopupMenuActiveScanScope getPopupMenuActiveScanScope(int menuIndex) {
		if (popupMenuActiveScanScope == null) {
			popupMenuActiveScanScope = new PopupMenuActiveScanScope(Constant.messages.getString("ascan.scope.popup"));
		}
		return popupMenuActiveScanScope;
	}

	private PopupMenuActiveScanSite getPopupMenuActiveScanSite(int menuIndex) {
		if (popupMenuActiveScanSite == null) {
			popupMenuActiveScanSite = new PopupMenuActiveScanSite(Constant.messages.getString("ascan.site.popup"));
		}
		return popupMenuActiveScanSite;
	}

	private PopupMenuActiveScanNode getPopupMenuActiveScanSubtree(int menuIndex) {
		if (popupMenuActiveScanSubtree == null) {
			popupMenuActiveScanSubtree = new PopupMenuActiveScanNode(Constant.messages.getString("ascan.subtree.popup"));
		}
		return popupMenuActiveScanSubtree;
	}

	private PopupMenuActiveScanCustom getPopupMenuActiveScanCustom(int menuIndex) {
		if (popupMenuActiveScanCustom == null) {
			popupMenuActiveScanCustom = new PopupMenuActiveScanCustom(Constant.messages.getString("ascan.custom.popup"));
		}
		return popupMenuActiveScanCustom;
	}

	private PopupMenuActiveScanURL getPopupMenuActiveScanURL(int menuIndex) {
		if (popupMenuActiveScanURL == null) {
			popupMenuActiveScanURL = new PopupMenuActiveScanURL(Constant.messages.getString("ascan.url.popup"));
		}
		return popupMenuActiveScanURL;
	}

	private PopupMenuOpenUrlInBrowser getPopupMenuOpenUrlInBrowser(int menuIndex) {
		if (popupMenuOpenUrlInBrowser == null) {
			popupMenuOpenUrlInBrowser = new PopupMenuOpenUrlInBrowser(
					Constant.messages.getString("history.browser.popup"));
			popupMenuOpenUrlInBrowser.setMenuIndex(menuIndex);
		}
		return popupMenuOpenUrlInBrowser;
	}
	
	private PopupMenuCopyUrls getPopupMenuCopyUrls(int menuIndex) {
		if (popupMenuCopyUrls == null) {
			popupMenuCopyUrls = new PopupMenuCopyUrls(
					Constant.messages.getString("stdexts.copyurls.popup"));
			popupMenuCopyUrls.setMenuIndex(menuIndex);
		}
		return popupMenuCopyUrls;
	}

	/*
	 * private PopupMenuShowResponseInBrowser getPopupMenuShowResponseInBrowser(int menuIndex) { if
	 * (popupMenuShowResponseInBrowser == null) { // TODO! popupMenuShowResponseInBrowser = new
	 * PopupMenuShowResponseInBrowser(Constant.messages.getString("history.showresponse.popup"));
	 * popupMenuShowResponseInBrowser.setMenuIndex(menuIndex); } return popupMenuShowResponseInBrowser; }
	 */

	private PopupExcludeFromProxyMenu getPopupExcludeFromProxyMenu(int menuIndex) {
		if (popupExcludeFromProxyMenu == null) {
			popupExcludeFromProxyMenu = new PopupExcludeFromProxyMenu();
			popupExcludeFromProxyMenu.setMenuIndex(menuIndex);
		}
		return popupExcludeFromProxyMenu;
	}

	private PopupExcludeFromScanMenu getPopupExcludeFromScanMenu(int menuIndex) {
		if (popupExcludeFromScanMenu == null) {
			popupExcludeFromScanMenu = new PopupExcludeFromScanMenu();
			popupExcludeFromScanMenu.setMenuIndex(menuIndex);
		}
		return popupExcludeFromScanMenu;
	}

	private PopupExcludeFromSpiderMenu getPopupExcludeFromSpiderMenu(int menuIndex) {
		if (popupExcludeFromSpiderMenu == null) {
			popupExcludeFromSpiderMenu = new PopupExcludeFromSpiderMenu();
			popupExcludeFromSpiderMenu.setMenuIndex(menuIndex);
		}
		return popupExcludeFromSpiderMenu;
	}

	private PopupMenuResendMessage getPopupMenuResendMessage(int menuIndex) {
		if (popupMenuResendMessage == null) {
			popupMenuResendMessage = new PopupMenuResendMessage(
					Constant.messages.getString("history.resend.popup"),
					(ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension(ExtensionHistory.NAME));
			popupMenuResendMessage.setMenuIndex(menuIndex);
		}
		return popupMenuResendMessage;
	}

	private PopupMenuShowInSites getPopupMenuShowInSites(int menuIndex) {
		if (popupMenuShowInSites == null) {
			popupMenuShowInSites = new PopupMenuShowInSites(Constant.messages.getString("sites.showinsites.popup"));
			popupMenuShowInSites.setMenuIndex(menuIndex);
		}
		return popupMenuShowInSites;
	}

	private PopupMenuShowInHistory getPopupMenuShowInHistory(int menuIndex) {
		if (popupMenuShowInHistory == null) {
			popupMenuShowInHistory = new PopupMenuShowInHistory(
					Constant.messages.getString("history.showinhistory.popup"),
					(ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension(ExtensionHistory.NAME));
			popupMenuShowInHistory.setMenuIndex(menuIndex);
		}
		return popupMenuShowInHistory;
	}

	private PopupMenuAlert getPopupMenuAlert(int menuIndex) {
		if (popupMenuAlert == null) {
			popupMenuAlert = new PopupMenuAlert(Constant.messages.getString("history.alert.popup"));
			popupMenuAlert.setMenuIndex(menuIndex);
		}
		return popupMenuAlert;
	}

	private PopupMenuItemContextInclude getPopupContextIncludeMenu(int menuIndex) {
		if (popupContextIncludeMenu == null) {
			popupContextIncludeMenu = new PopupMenuItemContextInclude();
			popupContextIncludeMenu.setParentMenuIndex(menuIndex);
		}
		return popupContextIncludeMenu;
	}

	private PopupMenuItemContextExclude getPopupContextExcludeMenu(int menuIndex) {
		if (popupContextExcludeMenu == null) {
			popupContextExcludeMenu = new PopupMenuItemContextExclude();
			popupContextExcludeMenu.setParentMenuIndex(menuIndex);
		}
		return popupContextExcludeMenu;
	}

	@Override
	public String getAuthor() {
		return Constant.ZAP_TEAM;
	}

	@Override
	public String getDescription() {
		return Constant.messages.getString("stdexts.desc");
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
