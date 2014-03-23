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
package org.zaproxy.zap.extension.ascan;

import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.core.scanner.HostProcess;
import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.core.scanner.ScannerListener;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.alert.ExtensionAlert;
import org.zaproxy.zap.extension.httppanel.HttpPanel;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.model.GenericScanner;
import org.zaproxy.zap.model.ScanListenner;
import org.zaproxy.zap.users.User;
import org.zaproxy.zap.view.ScanPanel;

public class ActiveScanPanel extends ScanPanel implements ScanListenner, ScannerListener {
	
	private static final long serialVersionUID = 1L;

	public static final String PANEL_NAME = "ascan";

    private static final ListModel<HistoryReference> EMPTY_RESULTS_MODEL = new DefaultListModel<>();
	
	private JScrollPane jScrollPane = null;
    private ActiveScanPanelCellRenderer activeScanPanelCellRenderer = null;
	private static JList<HistoryReference> messageList = null;
    private List<String> excludeUrls = null;
    
	private HttpPanel requestPanel = null;
	private HttpPanel responsePanel = null;

	private JButton optionsButton = null;
	private JButton progressButton;
	private JLabel numRequests;

    private static Logger logger = Logger.getLogger(ActiveScanPanel.class);
    
    /**
     * @param extension
     */
    public ActiveScanPanel(ExtensionActiveScan extension) {
    	// 'fire' icon
        super("ascan", new ImageIcon(ActiveScanPanel.class.getResource("/resource/icon/16/093.png")), extension, null);
		this.setDefaultAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, Event.CTRL_MASK | Event.ALT_MASK | Event.SHIFT_MASK, false));
		this.setMnemonic(Constant.messages.getChar("ascan.panel.mnemonic"));
    }

	@Override
	protected int addToolBarElements(JToolBar panelToolbar, Location loc, int x) {
		// Override to add elements into the toolbar
		if (Location.beforeButtons.equals(loc)) {
			panelToolbar.add(getOptionsButton(), getGBC(x++,0));
		}
		if (Location.beforeProgressBar.equals(loc)) {
			panelToolbar.add(getProgressButton(), getGBC(x++,0));
		}
		if (Location.afterProgressBar.equals(loc)) {
			panelToolbar.add(new JLabel(Constant.messages.getString("ascan.toolbar.requests.label")), getGBC(x++,0));
			panelToolbar.add(getNumRequests(), getGBC(x++,0));
		}
		return x;
	}
	
	private JButton getOptionsButton() {
		if (optionsButton == null) {
			optionsButton = new JButton();
			optionsButton.setToolTipText(Constant.messages.getString("menu.analyse.scanPolicy"));
			optionsButton.setIcon(new ImageIcon(ActiveScanPanel.class.getResource("/resource/icon/fugue/equalizer.png")));
			optionsButton.addActionListener(new ActionListener () {
				@Override
				public void actionPerformed(ActionEvent e) {
					((ExtensionActiveScan)getExtension()).showPolicyDialog();
				}
			});
		}
		return optionsButton;
	}

	private JButton getProgressButton() {
		if (progressButton == null) {
			progressButton = new JButton();
			progressButton.setEnabled(false);
			progressButton.setToolTipText(Constant.messages.getString("ascan.toolbar.button.progress"));
			progressButton.setIcon(new ImageIcon(ActiveScanPanel.class.getResource("/resource/icon/fugue/system-monitor.png")));
			progressButton.addActionListener(new ActionListener () {
				@Override
				public void actionPerformed(ActionEvent e) {
					showScanProgressDialog();
				}
			});
		}
		return progressButton;
	}
	
	private JLabel getNumRequests() {
		if (numRequests == null) {
			numRequests = new JLabel();
		}
		return numRequests;
	}
	
	private void showScanProgressDialog() {
		ActiveScan scan = (ActiveScan) getScanThread(getCurrentSite());
		if (scan != null) {
			ScanProgressDialog spp = new ScanProgressDialog(View.getSingleton().getMainFrame(), getCurrentSite());
			spp.setActiveScan(scan);
			spp.setVisible(true);
		}
	}

	@Override
	protected JScrollPane getWorkPanel() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getMessageList());
			jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane;
	}

	private void resetMessageList() {
		getMessageList().setModel(EMPTY_RESULTS_MODEL);
	}

	private synchronized JList<HistoryReference> getMessageList() {
		if (messageList == null) {
			messageList = new JList<>(EMPTY_RESULTS_MODEL);
			messageList.setDoubleBuffered(true);
			messageList.setCellRenderer(getActiveScanPanelCellRenderer());
			messageList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			messageList.setName(PANEL_NAME);
			messageList.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
			
			messageList.setFixedCellHeight(16);	// Significantly speeds up rendering
			
			messageList.addMouseListener(new java.awt.event.MouseAdapter() { 
				@Override
				public void mousePressed(java.awt.event.MouseEvent e) {    
					showPopupMenuIfTriggered(e);
				}
				@Override
				public void mouseReleased(java.awt.event.MouseEvent e) {
					showPopupMenuIfTriggered(e);
				}
				private void showPopupMenuIfTriggered(java.awt.event.MouseEvent e) {
					if (e.isPopupTrigger()) {
						// Select list item on right click
					    int Idx = messageList.locationToIndex( e.getPoint() );
					    if ( Idx >= 0 ) {
					    	Rectangle Rect = messageList.getCellBounds( Idx, Idx );
					    	Idx = Rect.contains( e.getPoint().x, e.getPoint().y ) ? Idx : -1;
					    }
					    if ( Idx < 0 || !messageList.getSelectionModel().isSelectedIndex( Idx ) ) {
					    	messageList.getSelectionModel().clearSelection();
					    	if ( Idx >= 0 ) {
					    		messageList.getSelectionModel().setSelectionInterval( Idx, Idx );
					    	}
					    }
				        View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				    }	
				}
			});

			messageList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
				    if (!e.getValueIsAdjusting()) {
    	                HistoryReference hRef = messageList.getSelectedValue();
    			        if (hRef == null) {
                            return;
                        }
    			        
    				    try {
    				        displayMessage(hRef.getHttpMessage());
    				    } catch (HttpMalformedHeaderException ex) {
    				        logger.error(ex.getMessage(), ex);
                        } catch (SQLException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
				    }
				}});
		}
		return messageList;
	}
	
    private void displayMessage(HttpMessage msg) {
    	if (msg == null) {
    		return;
    	}
    	if (msg.getRequestHeader() != null) {
    		logger.debug("displayMessage " + msg.getRequestHeader().getURI());
    	} else {
    		logger.debug("displayMessage null header");
    	}
    	
        if (msg.getRequestHeader() != null && msg.getRequestHeader().isEmpty()) {
            requestPanel.clearView(true);
        } else {
            requestPanel.setMessage(msg);
        }
        
        if (msg.getResponseHeader() != null && msg.getResponseHeader().isEmpty()) {
            responsePanel.clearView(false);
        } else {
            responsePanel.setMessage(msg, true);
        }
    }



	private ListCellRenderer<HistoryReference> getActiveScanPanelCellRenderer() {
        if (activeScanPanelCellRenderer == null) {
            activeScanPanelCellRenderer = new ActiveScanPanelCellRenderer();
            activeScanPanelCellRenderer.setSize(new java.awt.Dimension(328,21));
            activeScanPanelCellRenderer.setBackground(java.awt.Color.white);
            activeScanPanelCellRenderer.setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.PLAIN, 12));
        }
        return activeScanPanelCellRenderer;
	}

	@Override
	protected GenericScanner newScanThread(String site, AbstractParam params) {
		ActiveScan as = new ActiveScan(site, ((ExtensionActiveScan)this.getExtension()).getScannerParam(), 
				this.getExtension().getModel().getOptionsParam().getConnectionParam(), this,
				Control.getSingleton().getPluginFactory().clone());
		as.setExcludeList(this.excludeUrls);
		return as;
	}


	@Override
	protected void switchView(String site) {
		if ("".equals(site)) {
			resetMessageList();
			return;
		}

		GenericScanner thread = this.getScanThread(site);
		if (thread != null) {
			getMessageList().setModel(((ActiveScan)thread).getList());
		}
	}


	@Override
	public void alertFound(Alert alert) {
		ExtensionAlert extAlert = (ExtensionAlert) Control.getSingleton().getExtensionLoader().getExtension(ExtensionAlert.NAME);
		if (extAlert != null) {
			extAlert.alertFound(alert, alert.getHistoryRef());
		}
	}


	@Override
	public void hostComplete(String hostAndPort) {
		this.scanFinshed(cleanSiteName(hostAndPort, true));
		
	}


	@Override
	public void hostNewScan(String hostAndPort, HostProcess hostThread) {
	}


	@Override
	public void hostProgress(String hostAndPort, String msg, int percentage) {
		this.scanProgress(cleanSiteName(hostAndPort, true), percentage, 100);
		updateRequestCount();
	}

	@Override
	public void scannerComplete() {
	}
	
	private void updateRequestCount() {
		GenericScanner gs = this.getScanThread(this.getCurrentSite());
		if (gs != null && gs instanceof ActiveScan) {
			this.getNumRequests().setText(Integer.toString(((ActiveScan) gs).getTotalRequests()));
		}
	}

	@Override
	public void notifyNewMessage(HttpMessage msg) {
	}

    public void setDisplayPanel(HttpPanel requestPanel, HttpPanel responsePanel) {
        this.requestPanel = requestPanel;
        this.responsePanel = responsePanel;

    }
    
	@Override
	public void reset() {
		super.reset();
		this.resetMessageList();
		this.getProgressButton().setEnabled(false);
	}

	public void setExcludeList(List<String> urls) {
		this.excludeUrls = urls;
		Map<String, GenericScanner> threads = getScanThreads();
		Iterator<GenericScanner> iter = threads.values().iterator();
		while (iter.hasNext()) {
			GenericScanner scanner = iter.next();
			((ActiveScan)scanner).setExcludeList(urls);
		}
	}

	@Override
	protected void siteSelected(String site, boolean forceRefresh) {
		super.siteSelected(site, forceRefresh);
		// Clear the number of requests - will set below if applicable
		this.getNumRequests().setText("");

		GenericScanner gs = this.getScanThread(this.getCurrentSite());
		if (gs != null && (gs.isRunning() || gs.isStopped())) {
			this.getProgressButton().setEnabled(true);
			if (gs instanceof ActiveScan) {
				this.getNumRequests().setText(Integer.toString(((ActiveScan) gs).getTotalRequests()));
			}
		} else {
			this.getProgressButton().setEnabled(false);
		}
	}
	
	@Override
	protected void handleContextSpecificObject(GenericScanner scanThread, Object[] contextSpecificObjects) {
		ActiveScan ascan = (ActiveScan) scanThread;
		for (Object obj : contextSpecificObjects) {
			if (obj instanceof ScannerParam) {
				logger.debug("Setting custom scanner params");
				ascan.setScannerParam((ScannerParam)obj);
			} else if (obj instanceof PluginFactory) {
				ascan.setPluginFactory((PluginFactory)obj);
			} else {
				logger.error("Unexpected contextSpecificObject: " + obj.getClass().getCanonicalName());
			}
				
		}
	}

	@Override
	public void startScan(SiteNode startNode, boolean justScanInScope, boolean scanChildren, 
			Context scanContext, User user, Object[] contextSpecificObjects) {
		super.startScan(startNode, justScanInScope, scanChildren, scanContext, user, contextSpecificObjects);
		this.getProgressButton().setEnabled(true);
	}

}
