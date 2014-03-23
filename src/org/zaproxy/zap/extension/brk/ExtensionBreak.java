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
package org.zaproxy.zap.extension.brk;

import java.awt.Component;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.control.Control.Mode;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.ExtensionHookView;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.api.API;
import org.zaproxy.zap.extension.brk.impl.http.HttpBreakpointMessage;
import org.zaproxy.zap.extension.brk.impl.http.HttpBreakpointMessage.Location;
import org.zaproxy.zap.extension.brk.impl.http.HttpBreakpointMessage.Match;
import org.zaproxy.zap.extension.brk.impl.http.HttpBreakpointsUiManagerInterface;
import org.zaproxy.zap.extension.brk.impl.http.ProxyListenerBreak;
import org.zaproxy.zap.extension.help.ExtensionHelp;
import org.zaproxy.zap.extension.httppanel.Message;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionBreak extends ExtensionAdaptor implements SessionChangedListener {

    public enum DialogType {NONE, ADD, EDIT, REMOVE};
    
    public static final String NAME = "ExtensionBreak";
    
	private static final Logger logger = Logger.getLogger(ExtensionBreak.class);
	
	private BreakPanel breakPanel = null;
	private ProxyListenerBreak proxyListener = null;

	private BreakpointsPanel breakpointsPanel = null;

    private PopupMenuEditBreak popupMenuEditBreak = null;
	private PopupMenuRemove popupMenuRemove = null;
	
	private BreakpointMessageHandler breakpointMessageHandler;
	
    private DialogType currentDialogType = DialogType.NONE;
	
    private Map<Class<? extends BreakpointMessageInterface>, BreakpointsUiManagerInterface> mapBreakpointUiManager;
    
    private Map<Class<? extends Message>, BreakpointsUiManagerInterface> mapMessageUiManager;
    
	private Mode mode = Control.getSingleton().getMode();
	
	private BreakpointsParam breakpointsParams;
	
	private BreakpointsOptionsPanel breakpointsOptionsPanel;
	
	private HttpBreakpointsUiManagerInterface httpBreakpoints;

    private ZapMenuItem menuBreakOnRequests = null;
    private ZapMenuItem menuBreakOnResponses = null;
    private ZapMenuItem menuStep = null;
    private ZapMenuItem menuContinue = null;
    private ZapMenuItem menuDrop = null;
    private ZapMenuItem menuHttpBreakpoint = null;

	private BreakAPI api = new BreakAPI(this);

	
    public ExtensionBreak() {
        super();
 		initialize();
    }

    
    public ExtensionBreak(String name) {
        super(name);
    }

	
	private void initialize() {
        this.setName(NAME);
        this.setOrder(24);
	}
	
	public BreakPanel getBreakPanel() {
		if (breakPanel == null) {
		    breakPanel = new BreakPanel(this, getOptionsParam());
		    breakPanel.setName(Constant.messages.getString("tab.break"));
		}
		return breakPanel;
	}
	
	@Override
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    
	    extensionHook.addOptionsParamSet(getOptionsParam());
	    
	    if (getView() != null) {
	        breakpointMessageHandler = new BreakpointMessageHandler(getBreakPanel());
	        breakpointMessageHandler.setEnabledBreakpoints(getBreakpointsModel().getBreakpointsEnabledList());
	        
	        ExtensionHookView pv = extensionHook.getHookView();
	        pv.addWorkPanel(getBreakPanel());
	        pv.addOptionPanel(getOptionsPanel());
	        
            extensionHook.getHookMenu().addAnalyseMenuItem(extensionHook.getHookMenu().getMenuSeparator());

	        extensionHook.getHookView().addStatusPanel(getBreakpointsPanel());

	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuEdit());
	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuDelete());
            
            mapBreakpointUiManager = new HashMap<>();
            mapMessageUiManager = new HashMap<>();
            
            httpBreakpoints = new HttpBreakpointsUiManagerInterface(extensionHook.getHookMenu(), this);
            
            addBreakpointsUiManager(httpBreakpoints);
            
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuToggleBreakOnRequests());
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuToggleBreakOnResponses());
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuStep());
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuContinue());
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuDrop());
	        extensionHook.getHookMenu().addToolsMenuItem(getMenuAddHttpBreakpoint());
            
            extensionHook.addProxyListener(getProxyListenerBreak());

            // APIs are usually loaded even if the view is null, as they are specifically for daemon mode
            // However in this case the API isnt really of any use unless the UI is available
    		API.getInstance().registerApiImplementor(api);

            extensionHook.addSessionListener(this);

	    	ExtensionHelp.enableHelpKey(getBreakPanel(), "ui.tabs.break");
	    	ExtensionHelp.enableHelpKey(getBreakpointsPanel(), "ui.tabs.breakpoints");
	    }
	}
	
	private BreakpointsParam getOptionsParam() {
		if (breakpointsParams == null) {
			breakpointsParams = new BreakpointsParam();
		}
		return breakpointsParams;
	}
	
	private BreakpointsOptionsPanel getOptionsPanel() {
		if (breakpointsOptionsPanel == null) {
			breakpointsOptionsPanel = new BreakpointsOptionsPanel();
		}
		return breakpointsOptionsPanel;
	}
	
	private BreakpointsPanel getBreakpointsPanel() {
		if (breakpointsPanel == null) {
			breakpointsPanel = new BreakpointsPanel(this);
		}
		return breakpointsPanel;
	}
	
	public void addBreakpoint(BreakpointMessageInterface breakpoint) {
		this.getBreakpointsPanel().addBreakpoint(breakpoint);
		// Switch to the panel for some visual feedback
		this.getBreakpointsPanel().setTabFocus();
	}

	public void editBreakpoint(BreakpointMessageInterface oldBreakpoint, BreakpointMessageInterface newBreakpoint) {
		this.getBreakpointsPanel().editBreakpoint(oldBreakpoint, newBreakpoint);
	}
	
	public void removeBreakpoint(BreakpointMessageInterface breakpoint) {
		this.getBreakpointsPanel().removeBreakpoint(breakpoint);
	}
    
    public List<BreakpointMessageInterface> getBreakpointsList() {
        return getBreakpointsModel().getBreakpointsList();
    }
    
    public BreakpointMessageInterface getUiSelectedBreakpoint() {
        return getBreakpointsPanel().getSelectedBreakpoint();
    }
    
    public void addBreakpointsUiManager(BreakpointsUiManagerInterface uiManager) {
        mapBreakpointUiManager.put(uiManager.getBreakpointClass(), uiManager);
        mapMessageUiManager.put(uiManager.getMessageClass(), uiManager);
    }


	public void removeBreakpointsUiManager(BreakpointsUiManagerInterface uiManager) {
        mapBreakpointUiManager.remove(uiManager.getBreakpointClass());
        mapMessageUiManager.remove(uiManager.getMessageClass());		
	}
	
	public void setBreakAllRequests(boolean brk) {
		this.getBreakPanel().setBreakAllRequests(brk);
	}
    
	public void setBreakAllResponses(boolean brk) {
		this.getBreakPanel().setBreakAllResponses(brk);
	}

	public void addHttpBreakpoint(String string, String location, String match, boolean inverse, boolean ignoreCase) {
		Location loc;
		Match mtch;
		
		try {
			loc = Location.valueOf(location);
		} catch (Exception e) {
			throw new InvalidParameterException("location must be one of " + Arrays.toString(Location.values()));
		}
		
		try {
			mtch = Match.valueOf(match);
		} catch (Exception e) {
			throw new InvalidParameterException("match must be one of " + Arrays.toString(Match.values()));
		}
		
		this.addBreakpoint(new HttpBreakpointMessage(string, loc, mtch, inverse, ignoreCase));
		
	}

	public void removeHttpBreakpoint(String string, String location, String match, boolean inverse, boolean ignoreCase) {
		Location loc;
		Match mtch;
		
		try {
			loc = Location.valueOf(location);
		} catch (Exception e) {
			throw new InvalidParameterException("location must be one of " + Arrays.toString(Location.values()));
		}
		
		try {
			mtch = Match.valueOf(match);
		} catch (Exception e) {
			throw new InvalidParameterException("match must be one of " + Arrays.toString(Match.values()));
		}
		
		this.removeBreakpoint(new HttpBreakpointMessage(string, loc, mtch, inverse, ignoreCase));
		
	}

    public void addUiBreakpoint(Message aMessage) {
        BreakpointsUiManagerInterface uiManager = getBreakpointUiManager(aMessage.getClass());
        if (uiManager != null) {
            uiManager.handleAddBreakpoint(aMessage);
        }
     }
     
    private BreakpointsUiManagerInterface getBreakpointUiManager(Class<?> clazz) {
        if (!Message.class.isAssignableFrom(clazz)) {
            return null;
        }

        BreakpointsUiManagerInterface uiManager = mapMessageUiManager.get(clazz);
        if (uiManager == null) {
            uiManager = getBreakpointUiManager(clazz.getSuperclass());
        }

        return uiManager;
    }
 
    public void editUiSelectedBreakpoint() {
        BreakpointMessageInterface breakpoint = getBreakpointsPanel().getSelectedBreakpoint();
        if (breakpoint != null) {
            BreakpointsUiManagerInterface uiManager = mapBreakpointUiManager.get(breakpoint.getClass());
            if (uiManager != null) {
                uiManager.handleEditBreakpoint(breakpoint);
            }
        }
    }
	
	public void removeUiSelectedBreakpoint() {
	    BreakpointMessageInterface breakpoint = getBreakpointsPanel().getSelectedBreakpoint();
        if (breakpoint != null) {
            BreakpointsUiManagerInterface uiManager = mapBreakpointUiManager.get(breakpoint.getClass());
            if (uiManager != null) {
                uiManager.handleRemoveBreakpoint(breakpoint);
            }
        }
	}
	
	private BreakpointsTableModel getBreakpointsModel() {
		return (BreakpointsTableModel)this.getBreakpointsPanel().getBreakpoints().getModel();
	}
	
	private ProxyListenerBreak getProxyListenerBreak() {
        if (proxyListener == null) {
            proxyListener = new ProxyListenerBreak(getModel(), this);
        }
        return proxyListener;
	}

	private PopupMenuEditBreak getPopupMenuEdit() {
		if (popupMenuEditBreak == null) {
			popupMenuEditBreak = new PopupMenuEditBreak();
			popupMenuEditBreak.setExtension(this);
		}
		return popupMenuEditBreak;
	}

	private PopupMenuRemove getPopupMenuDelete() {
		if (popupMenuRemove == null) {
			popupMenuRemove = new PopupMenuRemove();
			popupMenuRemove.setExtension(this);
		}
		return popupMenuRemove;
	}
	
    private ZapMenuItem getMenuToggleBreakOnRequests() {
        if (menuBreakOnRequests == null) {
            menuBreakOnRequests = new ZapMenuItem("menu.tools.brk.req",
            		KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK, false));
            menuBreakOnRequests.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	// Toggle break on requests
                	getBreakPanel().setBreakAllRequests(! getBreakPanel().isBreakRequest());
                }
            });
        }
        return menuBreakOnRequests;
    }

    private ZapMenuItem getMenuToggleBreakOnResponses() {
        if (menuBreakOnResponses == null) {
            menuBreakOnResponses = new ZapMenuItem("menu.tools.brk.resp",
            		KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK | Event.ALT_MASK, false));
            menuBreakOnResponses.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	// Toggle break on Responses
                	getBreakPanel().setBreakAllResponses(! getBreakPanel().isBreakResponse());
                }
            });
        }
        return menuBreakOnResponses;
    }

    private ZapMenuItem getMenuStep() {
        if (menuStep == null) {
            menuStep = new ZapMenuItem("menu.tools.brk.step",
            		KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, false));
            menuStep.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if (getBreakPanel().isHoldMessage()) {
                		// Menu currently always enabled, but dont do anything unless a message is being held
                		getBreakPanel().step();
                	}
                }
            });
        }
        return menuStep;
    }

    private ZapMenuItem getMenuContinue() {
        if (menuContinue == null) {
            menuContinue = new ZapMenuItem("menu.tools.brk.cont",
            		KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, false));
            menuContinue.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if (getBreakPanel().isHoldMessage()) {
                		// Menu currently always enabled, but dont do anything unless a message is being held
                		getBreakPanel().cont();
                	}
                }
            });
        }
        return menuContinue;
    }

    private ZapMenuItem getMenuDrop() {
        if (menuDrop == null) {
            menuDrop = new ZapMenuItem("menu.tools.brk.drop",
            		KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, false));
            menuDrop.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if (getBreakPanel().isHoldMessage()) {
                		// Menu currently always enabled, but dont do anything unless a message is being held
                		getBreakPanel().drop();
                	}
                }
            });
        }
        return menuDrop;
    }

    private ZapMenuItem getMenuAddHttpBreakpoint() {
        if (menuHttpBreakpoint == null) {
        	menuHttpBreakpoint = new ZapMenuItem("menu.tools.brk.custom",
            		KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK, false));
        	menuHttpBreakpoint.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	// Check to see if anything is selected in the main tabs
                	String url = "";
                	Component c = View.getSingleton().getMainFrame().getFocusOwner();
                	if (c != null) {
                		if (c instanceof JList) {
                			// Handles the history list and similar
                			@SuppressWarnings("rawtypes")
							Object sel = ((JList)c).getSelectedValue();
                			try {
								if (sel != null && sel instanceof HistoryReference &&
										((HistoryReference)sel).getURI() != null) {
									url = ((HistoryReference)sel).getURI().toString();
								}
							} catch (Exception e1) {
								// Ignore
							}
                		} else if (c instanceof JTree) {
                			// Handles the Sites tree
                			TreePath path = ((JTree)c).getSelectionPath();
                			try {
								if (path != null && path.getLastPathComponent() instanceof SiteNode) {
									url = ((SiteNode)path.getLastPathComponent()).getHistoryReference().getURI().toString();
								}
							} catch (Exception e1) {
								// Ignore
							}
                		}
                	}
                	httpBreakpoints.handleAddBreakpoint(url);
                }
            });
        }
        return menuHttpBreakpoint;
    }

	public boolean canAddBreakpoint() {
		return (currentDialogType == DialogType.NONE || currentDialogType == DialogType.ADD);
	}
    
	public boolean canEditBreakpoint() {
		return (currentDialogType == DialogType.NONE || currentDialogType == DialogType.EDIT);
	}
	
	public boolean canRemoveBreakpoint() {
		return (currentDialogType == DialogType.NONE || currentDialogType == DialogType.REMOVE);
	}
	
	public void dialogShown(DialogType type) {
	    currentDialogType = type;
	}
	
	public void dialogClosed() {
	    currentDialogType = DialogType.NONE;
	}
	
	@Override
	public String getAuthor() {
		return Constant.ZAP_TEAM;
	}

	@Override
	public String getDescription() {
		return Constant.messages.getString("brk.desc");
	}

	@Override
	public URL getURL() {
		try {
			return new URL(Constant.ZAP_HOMEPAGE);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void sessionAboutToChange(final Session session) {
		if (EventQueue.isDispatchThread()) {
			sessionAboutToChange();
	    } else {
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                @Override
	                public void run() {
	                	sessionAboutToChange();
	                }
	            });
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	    }
	}

	@Override
	public void sessionChanged(Session session) {
		getBreakPanel().init();
	}
	
	private void sessionAboutToChange() {
	    getBreakPanel().reset();
	}
	
	@Override
	public void sessionScopeChanged(Session session) {
	}

	@Override
	public void destroy() {
		if (breakPanel != null) {
			breakPanel.savePanels();
		}
	}
	
	public boolean messageReceivedFromClient(Message aMessage) {
		if (mode.equals(Mode.safe)) {
			return true;
		}
	    return breakpointMessageHandler.handleMessageReceivedFromClient(aMessage, mode.equals(Mode.protect));
	}
	
	public boolean messageReceivedFromServer(Message aMessage) {
		if (mode.equals(Mode.safe)) {
			return true;
		}
	    return breakpointMessageHandler.handleMessageReceivedFromServer(aMessage, mode.equals(Mode.protect));
	}

	/**
	 * Exposes list of enabled breakpoints.
	 * 
	 * @return list of enabled breakpoints
	 */
	public List<BreakpointMessageInterface> getBreakpointsEnabledList() {
		if (mode.equals(Mode.safe)) {
			return new ArrayList<>();
		}
		return getBreakpointsModel().getBreakpointsEnabledList();
	}
	
	@Override
	public void sessionModeChanged(Mode mode) {
		this.mode = mode;
		this.getBreakPanel().sessionModeChanged(mode);
	}


	public void setBreakOnId(String id, boolean enable) {
		logger.debug("setBreakOnId " + id + " " + enable);
		if (enable) {
			breakpointMessageHandler.getEnabledKeyBreakpoints().add(id);
		} else {
			breakpointMessageHandler.getEnabledKeyBreakpoints().remove(id);
		}
	}
}