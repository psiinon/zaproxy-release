/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2011 The ZAP Development team
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
package org.zaproxy.zap.extension.alert;

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.control.Control.Mode;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.db.RecordAlert;
import org.parosproxy.paros.db.RecordScan;
import org.parosproxy.paros.db.TableAlert;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.MainFooterPanel;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.XmlReporterExtension;
import org.zaproxy.zap.extension.help.ExtensionHelp;

public class ExtensionAlert extends ExtensionAdaptor implements SessionChangedListener, XmlReporterExtension {

    public static final String NAME = "ExtensionAlert";
    private Map<Integer, HistoryReference> hrefs = new HashMap<>();
    private AlertTreeModel treeModel = null;
    private AlertTreeModel filteredTreeModel = null;
    private AlertPanel alertPanel = null;
    private RecordScan recordScan = null;
    private PopupMenuAlertEdit popupMenuAlertEdit = null;
    private PopupMenuAlertDelete popupMenuAlertDelete = null;
    private PopupMenuAlertsRefresh popupMenuAlertsRefresh = null;
    private PopupMenuShowAlerts popupMenuShowAlerts = null;
    private Logger logger = Logger.getLogger(ExtensionAlert.class);

    /**
     *
     */
    public ExtensionAlert() {
        super();
        initialize();
    }

    /**
     * @param name
     */
    public ExtensionAlert(String name) {
        super(name);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setName(NAME);
        this.setOrder(27);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);
        if (getView() != null) {
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuAlertEdit());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuAlertDelete());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuAlertsRefresh());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuShowAlerts());

            extensionHook.getHookView().addStatusPanel(getAlertPanel());

            ExtensionHelp.enableHelpKey(getAlertPanel(), "ui.tabs.alerts");
        }
        extensionHook.addSessionListener(this);

    }

    public void alertFound(Alert alert, HistoryReference ref) {
        try {
            logger.debug("alertFound " + alert.getAlert() + " " + alert.getUri());
            if (ref == null) {
                ref = alert.getHistoryRef();
            }
            if (ref == null) {
                ref = new HistoryReference(getModel().getSession(), HistoryReference.TYPE_SCANNER, alert.getMessage());
                alert.setHistoryRef(ref);
            }

            hrefs.put(Integer.valueOf(ref.getHistoryId()), ref);

            writeAlertToDB(alert, ref);
            addAlertToTree(alert, ref, alert.getMessage());

            // The node node may have a new alert flag...
            this.siteNodeChanged(ref.getSiteNode());

            // Clear the message so that it can be GC'ed
            alert.setMessage(null);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void siteNodeChanged(final TreeNode node) {
        if (EventQueue.isDispatchThread()) {
        	siteNodeChangedEventHandler(this.getModel().getSession().getSiteTree(), node);
        } else {
            try {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	siteNodeChangedEventHandler(getModel().getSession().getSiteTree(), node);
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void siteNodeChangedEventHandler(SiteMap siteTree, TreeNode node) {
        if (node == null) {
            return;
        }
        siteTree.nodeChanged(node);
        siteNodeChangedEventHandler(siteTree, node.getParent());
    }

    private void addAlertToTree(final Alert alert, final HistoryReference ref, final HttpMessage msg) {
        if (EventQueue.isDispatchThread()) {
            addAlertToTreeEventHandler(alert, ref, msg);

        } else {

            try {
                // Changed from invokeAndWait due to the number of interrupt exceptions
                // And its likely to always to run from a background thread anyway
                //EventQueue.invokeAndWait(new Runnable() {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        addAlertToTreeEventHandler(alert, ref, msg);
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    private boolean isInFilter(Alert alert) {
    	// Just support scope for now
    	return this.getModel().getSession().isInScope(alert.getHistoryRef());
    }

    private void addAlertToTreeEventHandler(Alert alert, HistoryReference ref, HttpMessage msg) {

        synchronized (this.getTreeModel()) {
        	this.getTreeModel().addPath(alert);
        	if (isInFilter(alert)) {
	        	this.getFilteredTreeModel().addPath(alert);
        	}
            if (getView() != null) {
                getAlertPanel().expandRoot();
                this.recalcAlerts();
            }
        }

        SiteMap siteTree = this.getModel().getSession().getSiteTree();
        SiteNode node = siteTree.findNode(alert.getMsgUri(), alert.getMethod(), alert.getPostData());
        if (ref != null && (node == null || !node.hasAlert(alert))) {
            // Add new alerts to the site tree
        	if (msg != null) {
        		// Saves a db read, which is always well worth it!
        		siteTree.addPath(ref, msg);
        	} else {
        		siteTree.addPath(ref);
        	}
            ref.addAlert(alert);
        }
    }

    /**
     * This method initializes alertPanel
     *
     * @return org.parosproxy.paros.extension.scanner.AlertPanel
     */
    AlertPanel getAlertPanel() {
        if (alertPanel == null) {
            alertPanel = new AlertPanel(this);
            alertPanel.setView(getView());
            alertPanel.setSize(345, 122);
            setMainTreeModel();
        }

        return alertPanel;
    }

    @Override
    public void initView(ViewDelegate view) {
        super.initView(view);
        getAlertPanel().setView(view);
    }

    AlertTreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new AlertTreeModel();
        }
        return treeModel;
    }

    private AlertTreeModel getFilteredTreeModel() {
        if (filteredTreeModel == null) {
        	filteredTreeModel = new AlertTreeModel();
        }
        return filteredTreeModel;
    }

    private void writeAlertToDB(Alert alert, HistoryReference ref) throws HttpMalformedHeaderException, SQLException {

        TableAlert tableAlert = getModel().getDb().getTableAlert();
        int scanId = 0;
        if (recordScan != null) {
            scanId = recordScan.getScanId();
        }
        RecordAlert recordAlert = tableAlert.write(
                scanId, alert.getPluginId(), alert.getAlert(), alert.getRisk(), alert.getReliability(),
                alert.getDescription(), alert.getUri(), alert.getParam(), alert.getAttack(),
                alert.getOtherInfo(), alert.getSolution(), alert.getReference(),
                alert.getEvidence(), alert.getCweId(), alert.getWascId(),
                ref.getHistoryId(), alert.getSourceHistoryId());

        alert.setAlertId(recordAlert.getAlertId());

    }

    public void updateAlert(Alert alert) throws HttpMalformedHeaderException, SQLException {
        logger.debug("updateAlert " + alert.getAlert() + " " + alert.getUri());
        updateAlertInDB(alert);
        if (alert.getHistoryRef() != null) {
            this.siteNodeChanged(alert.getHistoryRef().getSiteNode());
        }
    }

    private void updateAlertInDB(Alert alert) throws HttpMalformedHeaderException, SQLException {

        TableAlert tableAlert = getModel().getDb().getTableAlert();
        tableAlert.update(alert.getAlertId(), alert.getAlert(), alert.getRisk(),
                alert.getReliability(), alert.getDescription(), alert.getUri(),
                alert.getParam(), alert.getAttack(), alert.getOtherInfo(), alert.getSolution(), alert.getReference(), 
                alert.getEvidence(), alert.getCweId(), alert.getWascId(), alert.getSourceHistoryId());
    }

    public void displayAlert(Alert alert) {
        logger.debug("displayAlert " + alert.getAlert() + " " + alert.getUri());
        this.alertPanel.getAlertViewPanel().displayAlert(alert);
    }

    public void updateAlertInTree(Alert originalAlert, Alert alert) {
        this.getTreeModel().updatePath(originalAlert, alert);
    	if (isInFilter(alert)) {
    		this.getFilteredTreeModel().updatePath(originalAlert, alert);
    	}
    	this.recalcAlerts();
    }

    @Override
    public void sessionChanged(final Session session) {
        if (EventQueue.isDispatchThread()) {
            sessionChangedEventHandler(session);

        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        sessionChangedEventHandler(session);
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void sessionChangedEventHandler(Session session) {
        setTreeModel(new AlertTreeModel());
        
        treeModel = null;
        filteredTreeModel = null;
        hrefs = new HashMap<>();

    	if (session == null) {
    		// Null session indicated we're sutting down
    		return;
    	}

        try {
            refreshAlert(session);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        setTreeModel(getTreeModel());
    }

    private void refreshAlert(Session session) throws SQLException {
        SiteMap siteTree = this.getModel().getSession().getSiteTree();

        TableAlert tableAlert = getModel().getDb().getTableAlert();
        Vector<Integer> v = tableAlert.getAlertList();

        final ExtensionHistory extensionHistory = (ExtensionHistory) Control.getSingleton()
                .getExtensionLoader()
                .getExtension(ExtensionHistory.NAME);

        for (int i = 0; i < v.size(); i++) {
            int alertId = v.get(i).intValue();
            RecordAlert recAlert = tableAlert.read(alertId);
            int historyId = recAlert.getHistoryId();
            HistoryReference historyReference = null;
            if (extensionHistory != null) {
                historyReference = extensionHistory.getHistoryReference(historyId);
            }

            if (historyReference == null) {
                historyReference = this.hrefs.get(Integer.valueOf(historyId));
            }

            Alert alert;
            if (historyReference != null) {
                alert = new Alert(recAlert, historyReference);
            } else {
                alert = new Alert(recAlert);
            }
            historyReference = alert.getHistoryRef();
            if (historyReference != null) {
                // The ref can be null if hrefs are purged
                addAlertToTree(alert, historyReference, null);
                Integer key = Integer.valueOf(historyId);
                if (!hrefs.containsKey(key)) {
                    this.hrefs.put(key, alert.getHistoryRef());
                }
            }
        }
        siteTree.nodeStructureChanged((SiteNode) siteTree.getRoot());
    }

    private PopupMenuAlertEdit getPopupMenuAlertEdit() {
        if (popupMenuAlertEdit == null) {
            popupMenuAlertEdit = new PopupMenuAlertEdit();
            popupMenuAlertEdit.setExtension(this);
        }
        return popupMenuAlertEdit;
    }

    private PopupMenuAlertDelete getPopupMenuAlertDelete() {
        if (popupMenuAlertDelete == null) {
            popupMenuAlertDelete = new PopupMenuAlertDelete();
            popupMenuAlertDelete.setExtension(this);
        }
        return popupMenuAlertDelete;
    }

    private PopupMenuAlertsRefresh getPopupMenuAlertsRefresh() {
        if (popupMenuAlertsRefresh == null) {
            popupMenuAlertsRefresh = new PopupMenuAlertsRefresh();
            popupMenuAlertsRefresh.setExtension(this);
        }
        return popupMenuAlertsRefresh;
    }

    private PopupMenuShowAlerts getPopupMenuShowAlerts() {
        if (popupMenuShowAlerts == null) {
            popupMenuShowAlerts = new PopupMenuShowAlerts(Constant.messages.getString("alerts.view.popup"));
        }
        return popupMenuShowAlerts;
    }

    public void deleteAlert(Alert alert) {
        logger.debug("deleteAlert " + alert.getAlert() + " " + alert.getUri());

        try {
            getModel().getDb().getTableAlert().deleteAlert(alert.getAlertId());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        deleteAlertFromDisplay(alert);

    }

    private void deleteAlertFromDisplay(final Alert alert) {
        if (getView() == null) {
            // Running as a daemon
            return;
        }
        if (EventQueue.isDispatchThread()) {
            deleteAlertFromDisplayEventHandler(alert);

        } else {

            try {
                EventQueue.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        deleteAlertFromDisplayEventHandler(alert);
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void deleteAlertFromDisplayEventHandler(Alert alert) {
        // Note - tried doing this in a SwingWorker but it too a LOT longer to run
        SiteMap siteTree = this.getModel().getSession().getSiteTree();
        SiteNode node = siteTree.findNode(alert.getMsgUri(), alert.getMethod(), alert.getPostData());
        if (node != null && node.hasAlert(alert)) {
            siteNodeChanged(node);
        }

        synchronized (this.getTreeModel()) {
        	this.getTreeModel().deletePath(alert);
        	this.getFilteredTreeModel().deletePath(alert);
            List<HistoryReference> toDelete = new ArrayList<>();
            for (HistoryReference href : hrefs.values()) {
                if (href.getAlerts().contains(alert)) {
                    href.deleteAlert(alert);
                    node = siteTree.findNode(alert.getMsgUri(), alert.getMethod(), alert.getPostData());
                    if (node != null) {
                        node.deleteAlert(alert);
                        siteNodeChanged(node);
                    }
                    if (href.getAlerts().size() == 0) {
                        toDelete.add(href);
                    }
                }
            }
            for (HistoryReference href : toDelete) {
                hrefs.remove(Integer.valueOf(href.getHistoryId()));
            }
        }

        this.recalcAlerts();
    }
    
    /**
     * Recalculates the total number of alerts by alert's risk contained in the "Alerts" tree and updates the alert's risks
     * footer status labels with the new values.
     * <p>
     * The method has no effect if the view is not initialised.
     * </p>
     * 
     * @see AlertPanel#getTreeAlert()
     * @see MainFooterPanel#setAlertHigh(int)
     * @see MainFooterPanel#setAlertInfo(int)
     * @see MainFooterPanel#setAlertLow(int)
     * @see MainFooterPanel#setAlertMedium(int)
     * @see View#isInitialised()
     */
    void recalcAlerts() {
        if (!View.isInitialised()) {
            return;
    	}
    	// Must only be called when View is initialised
    	int totalInfo = 0;
    	int totalLow = 0;
    	int totalMedium = 0;
    	int totalHigh = 0;

    	AlertNode parent = (AlertNode) getAlertPanel().getTreeAlert().getModel().getRoot();
    	if (parent != null) {
            for (int i=0; i<parent.getChildCount(); i++) {
                AlertNode child = parent.getChildAt(i);
            	switch (child.getRisk()) {
            	case Alert.RISK_INFO:	totalInfo++;	break;
            	case Alert.RISK_LOW:	totalLow++;		break;
            	case Alert.RISK_MEDIUM:	totalMedium++;	break;
            	case Alert.RISK_HIGH:	totalHigh++;	break;
            	}
            }
    	}
    	MainFooterPanel footer = View.getSingleton().getMainFrame().getMainFooterPanel();
        footer.setAlertInfo(totalInfo);
        footer.setAlertLow(totalLow);
        footer.setAlertMedium(totalMedium);
        footer.setAlertHigh(totalHigh);
    }

    public List<Alert> getAllAlerts() {
        List<Alert> allAlerts = new ArrayList<>();

        TableAlert tableAlert = getModel().getDb().getTableAlert();
        Vector<Integer> v;
        try {
            v = tableAlert.getAlertList();

            for (int i = 0; i < v.size(); i++) {
                int alertId = v.get(i).intValue();
                RecordAlert recAlert = tableAlert.read(alertId);
                Alert alert = new Alert(recAlert);
                if (!allAlerts.contains(alert)) {
                    allAlerts.add(alert);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return allAlerts;
    }

    @Override
    public String getXml(SiteNode site) {
        StringBuilder xml = new StringBuilder();
        xml.append("<alerts>");
        List<Alert> alerts = site.getAlerts();
        for (Alert alert : alerts) {
            if (alert.getReliability() != Alert.FALSE_POSITIVE) {
                String urlParamXML = alert.getUrlParamXML();
                xml.append(alert.toPluginXML(urlParamXML));
            }
        }
        xml.append("</alerts>");
        return xml.toString();
    }

    @Override
	public void sessionAboutToChange(Session session) {
	}

	@Override
	public String getAuthor() {
		return Constant.ZAP_TEAM;
	}

	@Override
	public String getDescription() {
		return Constant.messages.getString("alerts.desc");
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
	public void sessionScopeChanged(Session session) {
		// Have to recheck all alerts to see if they are in scope
		synchronized (this.getTreeModel()) {
			((AlertNode)this.getFilteredTreeModel().getRoot()).removeAllChildren();
			AlertNode root = (AlertNode)this.getTreeModel().getRoot();
			filterTree(root);
			this.getFilteredTreeModel().nodeStructureChanged(root);
		}
		
		this.recalcAlerts();
	}
	
	private void filterTree(AlertNode node) {
		if (node.getUserObject() != null) {
			Alert alert = node.getUserObject();
			if (this.isInFilter(alert)) {
				this.getFilteredTreeModel().addPath(alert);
			}
		}
		for (int i=0; i < node.getChildCount(); i++) {
			this.filterTree(node.getChildAt(i));
		}
	}

	@Override
	public void sessionModeChanged(Mode mode) {
		// Ignore
	}

	public void setAlertTabFocus() {
		this.getAlertPanel().setTabFocus();
	}

    /**
     * Sets whether the "Alerts" tree is filtered, or not based on current session scope.
     * <p>
     * If {@code inScope} is {@code true} only the alerts that are in the current session scope will be shown.
     * </p>
     * <p>
     * Calling this method removes the filter "Link with Sites selection", if enabled, as they are mutually exclusive.
     * </p>
     * 
     * @param inScope {@code true} if the "Alerts" tree should be filtered based on current session scope, {@code false}
     *            otherwise.
     * @see #setLinkWithSitesTreeSelection(boolean)
     * @see Session
     */
	public void setShowJustInScope(boolean inScope) {
		if (inScope) {
			setLinkWithSitesTreeSelection(false);
			setTreeModel(this.getFilteredTreeModel());
		} else {
			setMainTreeModel();
		}
	}
	
    /**
     * Sets the main tree model to the "Alerts" tree, by calling the method {@code setTreeModel(AlertTreeModel)} with the model
     * returned by the method {@code getTreeModel()} as parameter.
     * 
     * @see #getTreeModel()
     * @see #setTreeModel(AlertTreeModel)
     */
    void setMainTreeModel() {
        setTreeModel(getTreeModel());
    }

    /**
     * Sets the given {@code alertTreeModel} to the "Alerts" tree and recalculates the number of alerts by calling the method
     * {@code recalcAlerts()}.
     * 
     * @param alertTreeModel the model that will be set to the tree.
     * @see #recalcAlerts()
     * @see AlertPanel#getTreeAlert()
     */
    private void setTreeModel(AlertTreeModel alertTreeModel) {
        getAlertPanel().getTreeAlert().setModel(alertTreeModel);
        recalcAlerts();
    }

    /**
     * Sets whether the "Alerts" tree is filtered, or not based on a selected "Sites" tree node.
     * <p>
     * If {@code enabled} is {@code true} only the alerts of the selected "Sites" tree node will be shown.
     * </p>
     * <p>
     * Calling this method removes the filter "Just in Scope", if enabled, as they are mutually exclusive.
     * </p>
     * 
     * @param enabled {@code true} if the "Alerts" tree should be filtered based on a selected "Sites" tree node, {@code false}
     *            otherwise.
     * @see #setShowJustInScope(boolean)
     * @see View#getSiteTreePanel()
     */
    public void setLinkWithSitesTreeSelection(boolean enabled) {
        getAlertPanel().setLinkWithSitesTreeSelection(enabled);
    }
}
