/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2013 ZAP development team
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Category;
import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.core.scanner.VariantUserDefined;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.AbstractParamContainerPanel;
import org.zaproxy.zap.extension.users.ExtensionUserManagement;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.users.User;
import org.zaproxy.zap.utils.ZapTextArea;
import org.zaproxy.zap.view.LayoutHelper;
import org.zaproxy.zap.view.StandardFieldsDialog;

public class CustomScanDialog extends StandardFieldsDialog {

    private static final String FIELD_START = "ascan.custom.label.start";
    private static final String FIELD_CONTEXT = "ascan.custom.label.context";
    private static final String FIELD_USER = "ascan.custom.label.user";
    private static final String FIELD_RECURSE = "ascan.custom.label.recurse";
    private static final String FIELD_INSCOPE = "ascan.custom.label.inscope";

    private static final String FIELD_VARIANT_URL_QUERY = "variant.options.injectable.querystring.label";
    private static final String FIELD_VARIANT_URL_PATH = "variant.options.injectable.urlpath.label";
    private static final String FIELD_VARIANT_POST_DATA = "variant.options.injectable.postdata.label";
    private static final String FIELD_VARIANT_HEADERS = "variant.options.injectable.headers.label";
    private static final String FIELD_VARIANT_COOKIE = "variant.options.injectable.cookie.label";
    private static final String FIELD_VARIANT_MULTIPART = "variant.options.rpc.multipart.label";
    private static final String FIELD_VARIANT_XML = "variant.options.rpc.xml.label";
    private static final String FIELD_VARIANT_JSON = "variant.options.rpc.json.label";
    private static final String FIELD_VARIANT_GWT = "variant.options.rpc.gwt.label";
    private static final String FIELD_VARIANT_ODATA = "variant.options.rpc.odata.label";
    private static final String FIELD_VARIANT_CUSTOM = "variant.options.rpc.custom.label";

    private static Logger logger = Logger.getLogger(CustomScanDialog.class);

    private static final long serialVersionUID = 1L;

    private JButton[] extraButtons = null;

    private ExtensionActiveScan extension = null;
    
    private ExtensionUserManagement extUserMgmt = (ExtensionUserManagement) Control.getSingleton().getExtensionLoader()
			.getExtension(ExtensionUserManagement.NAME);
    
    private int headerLength = -1;
    // The index of the start of the URL path eg after https://www.example.com:1234/ - no point attacking this
    private int urlPathStart = -1;
    private SiteNode node = null;

    private ScannerParam scannerParam = null;
    private OptionsParam optionsParam = null;
    private PluginFactory pluginFactory = null;

    private JPanel customPanel = null;
    private ZapTextArea requestField = null;
    private JButton addCustomButton = null;
    private JButton removeCustomButton = null;
    private JList<Highlight> injectionPointList = null;
    private final DefaultListModel<Highlight> injectionPointModel = new DefaultListModel<Highlight>();
    private final JLabel customPanelStatus = new JLabel();

    public CustomScanDialog(ExtensionActiveScan ext, Frame owner, Dimension dim) {
        super(owner, "ascan.custom.title", dim, new String[]{
            "ascan.custom.tab.scope",
            "ascan.custom.tab.input",
            "ascan.custom.tab.custom",
            "ascan.custom.tab.policy"
        });
        this.extension = ext;

        // The first time init to the default options set, after that keep own copies
        reset(false);
    }

    public void init(SiteNode node) {
        logger.debug("init " + node);
        this.node = node;

        this.removeAllFields();
        this.injectionPointModel.clear();
        this.headerLength = -1;
        this.urlPathStart = -1;;

        this.addNodeSelectField(0, FIELD_START, node, false, false);
        this.addComboField(0, FIELD_CONTEXT, new String[] {}, "");
        this.addComboField(0, FIELD_USER, new String[] {}, "");
        this.addCheckBoxField(0, FIELD_RECURSE, true);
        this.addCheckBoxField(0, FIELD_INSCOPE, false);
        this.addPadding(0);

        // Default to Recurse, so always set the warning
        customPanelStatus.setText(Constant.messages.getString("ascan.custom.status.recurse"));

        this.addFieldListener(FIELD_CONTEXT, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUsers();
            }
        });
        this.addFieldListener(FIELD_RECURSE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFieldStates();
            }
        });

        int targets = scannerParam.getTargetParamsInjectable();
        this.addReadOnlyField(1, "variant.options.injectable.label",
                Constant.messages.getString("variant.options.injectable.label"), true);
        
        this.addCheckBoxField(1, FIELD_VARIANT_URL_QUERY, (targets & ScannerParam.TARGET_QUERYSTRING) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_URL_PATH, (targets & ScannerParam.TARGET_URLPATH) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_POST_DATA, (targets & ScannerParam.TARGET_POSTDATA) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_HEADERS, (targets & ScannerParam.TARGET_HTTPHEADERS) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_COOKIE, (targets & ScannerParam.TARGET_COOKIE) != 0);

        int rpcEnabled = scannerParam.getTargetParamsEnabledRPC();
        this.addPadding(1);
        this.addReadOnlyField(1, "variant.options.rpc.label",
                Constant.messages.getString("variant.options.rpc.label"), true);
        
        this.addCheckBoxField(1, FIELD_VARIANT_MULTIPART, (rpcEnabled & ScannerParam.RPC_MULTIPART) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_XML, (rpcEnabled & ScannerParam.RPC_XML) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_JSON, (rpcEnabled & ScannerParam.RPC_JSON) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_GWT, (rpcEnabled & ScannerParam.RPC_GWT) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_ODATA, (rpcEnabled & ScannerParam.RPC_ODATA) != 0);
        this.addCheckBoxField(1, FIELD_VARIANT_CUSTOM, (rpcEnabled & ScannerParam.RPC_CUSTOM) != 0);
        this.addPadding(1);

        // Custom vectors panel
        this.setCustomTabPanel(2, getCustomPanel());
        populateRequestField(node);

        // Policy panel
        AbstractParamContainerPanel policyPanel
                = new AbstractParamContainerPanel(Constant.messages.getString("ascan.custom.tab.policy"));
        
        String[] ROOT = {};

        PolicyAllCategoryPanel policyAllCategoryPanel
                = new PolicyAllCategoryPanel(optionsParam, scannerParam, this.pluginFactory, null);
        
        policyAllCategoryPanel.setName(Constant.messages.getString("ascan.custom.tab.policy"));

        policyPanel.addParamPanel(null, policyAllCategoryPanel, false);

        for (int i = 0; i < Category.getAllNames().length; i++) {
            policyPanel.addParamPanel(ROOT, Category.getName(i),
                    new PolicyCategoryPanel(i, this.pluginFactory.getAllPlugin()), true);
        }
        
        policyPanel.showDialog(true);

        this.setCustomTabPanel(3, policyPanel);
        // Set up the fields correctly
        this.siteNodeSelected(FIELD_START, node);	
        this.pack();
    }

    private void populateRequestField(SiteNode node) {
        try {
            if (node == null || node.getHistoryReference() == null || node.getHistoryReference().getHttpMessage() == null) {
                this.getRequestField().setText("");
                
            } else {
                // Populate the custom vectors http pane
                HttpMessage msg = node.getHistoryReference().getHttpMessage();
                String header = msg.getRequestHeader().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(header);
                this.headerLength = header.length();
                this.urlPathStart = header.indexOf("/", header.indexOf("://") + 2) + 1;	// Ignore <METHOD> http(s)://host:port/
                sb.append(msg.getRequestBody().toString());
                this.getRequestField().setText(sb.toString());

                // Only set the recurse option if the node has children, and disable it otherwise
                JCheckBox recurseChk = (JCheckBox) this.getField(FIELD_RECURSE);
                recurseChk.setEnabled(node.getChildCount() > 0);
                recurseChk.setSelected(node.getChildCount() > 0);
            }
            
            this.setFieldStates();
            
        } catch (Exception e) {
            // 
            this.getRequestField().setText("");
        }

    }

    @Override
    public void siteNodeSelected(String field, SiteNode node) {
        List<String> ctxNames = new ArrayList<String>();
        if (node != null) {
            // The user has selected a new node
            this.node = node;
            populateRequestField(node);
            
            Session session = Model.getSingleton().getSession();
            List<Context> contexts = session.getContextsForNode(node);
            for (Context context : contexts) {
            	ctxNames.add(context.getName());
            }
        }
        this.setComboFields(FIELD_CONTEXT, ctxNames, "");
    }
    
    private Context getSelectedContext() {
    	String ctxName = this.getStringValue(FIELD_CONTEXT);
    	if (this.extUserMgmt != null && ctxName.length() > 0) {
            Session session = Model.getSingleton().getSession();
            return session.getContext(ctxName);
    	}
    	return null;
    }

    private User getSelectedUser() {
    	Context context = this.getSelectedContext();
    	if (context != null) {
        	String userName = this.getStringValue(FIELD_USER);
        	List<User> users = this.extUserMgmt.getContextUserAuthManager(context.getIndex()).getUsers();
        	for (User user : users) {
        		if (userName.equals(user.getName())) {
        			return user;
        		}
            }
    	}
    	return null;
    }

    private void setUsers() {
    	Context context = this.getSelectedContext();
        List<String> userNames = new ArrayList<String>();
    	if (context != null) {
        	List<User> users = this.extUserMgmt.getContextUserAuthManager(context.getIndex()).getUsers();
        	userNames.add("");	// The default should always be 'not specified'
        	for (User user : users) {
        		userNames.add(user.getName());
            }
    	}
        this.setComboFields(FIELD_USER, userNames, "");
    }


    private ZapTextArea getRequestField() {
        if (requestField == null) {
            requestField = new ZapTextArea();
            requestField.setEditable(false);
            requestField.setLineWrap(true);
            requestField.getCaret().setVisible(true);
        }
        return requestField;
    }

    private JPanel getCustomPanel() {
        if (customPanel == null) {
            customPanel = new JPanel(new GridBagLayout());

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setViewportView(getRequestField());

            JPanel buttonPanel = new JPanel(new GridBagLayout());

            getRequestField().addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent event) {
                    setFieldStates();
                }
            });

            buttonPanel.add(new JLabel(""), LayoutHelper.getGBC(0, 0, 1, 0.5));	// Spacer
            buttonPanel.add(getAddCustomButton(), LayoutHelper.getGBC(1, 0, 1, 1, 0.0D, 0.0D,
                    GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, new Insets(5, 5, 5, 5)));
            
            buttonPanel.add(new JLabel(""), LayoutHelper.getGBC(2, 0, 1, 0.5));	// Spacer

            buttonPanel.add(new JLabel(""), LayoutHelper.getGBC(0, 1, 1, 0.5));	// Spacer
            buttonPanel.add(getRemoveCustomButton(), LayoutHelper.getGBC(1, 1, 1, 1, 0.0D, 0.0D,
                    GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, new Insets(5, 5, 5, 5)));
            
            buttonPanel.add(new JLabel(""), LayoutHelper.getGBC(2, 1, 1, 0.5));	// Spacer

            JScrollPane scrollPane2 = new JScrollPane(getInjectionPointList());
            scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            buttonPanel.add(new JLabel(Constant.messages.getString("ascan.custom.label.vectors")),
                    LayoutHelper.getGBC(0, 2, 3, 0.0D, 0.0D));
            
            buttonPanel.add(scrollPane2, LayoutHelper.getGBC(0, 3, 3, 1.0D, 1.0D));

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, buttonPanel);
            splitPane.setDividerLocation(550);
            customPanel.add(splitPane, LayoutHelper.getGBC(0, 0, 1, 1, 1.0D, 1.0D));
            customPanel.add(customPanelStatus, LayoutHelper.getGBC(0, 1, 1, 1, 1.0D, 0.0D));
        }
        
        return customPanel;
    }

    private JButton getAddCustomButton() {
        if (addCustomButton == null) {
            addCustomButton = new JButton(Constant.messages.getString("ascan.custom.button.pt.add"));
            addCustomButton.setEnabled(false);

            addCustomButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // Add the selected injection point
                    int userDefStart = getRequestField().getSelectionStart();
                    if (userDefStart >= 0) {
                        int userDefEnd = getRequestField().getSelectionEnd();
                        Highlighter hl = getRequestField().getHighlighter();
                        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                        try {
                            Highlight hlt = (Highlight) hl.addHighlight(userDefStart, userDefEnd, painter);
                            injectionPointModel.addElement(hlt);
                            // Unselect the text
                            getRequestField().setSelectionStart(userDefEnd);
                            getRequestField().setSelectionEnd(userDefEnd);
                            getRequestField().getCaret().setVisible(true);
                            
                        } catch (BadLocationException e1) {
                            logger.error(e1.getMessage(), e1);
                        }
                    }

                }
            });

        }
        return addCustomButton;
    }

    private JButton getRemoveCustomButton() {
        if (removeCustomButton == null) {
            removeCustomButton = new JButton(Constant.messages.getString("ascan.custom.button.pt.rem"));
            removeCustomButton.setEnabled(false);

            removeCustomButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // Remove any selected injection points
                    int userDefStart = getRequestField().getSelectionStart();
                    if (userDefStart >= 0) {
                        int userDefEnd = getRequestField().getSelectionEnd();
                        Highlighter hltr = getRequestField().getHighlighter();
                        Highlight[] hls = hltr.getHighlights();

                        if (hls != null && hls.length > 0) {
                            for (Highlight hl : hls) {
                                if (selectionIncludesHighlight(userDefStart, userDefEnd, hl)) {
                                    hltr.removeHighlight(hl);
                                    injectionPointModel.removeElement(hl);
                                }
                            }
                        }
                        
                        // Unselect the text
                        getRequestField().setSelectionStart(userDefEnd);
                        getRequestField().setSelectionEnd(userDefEnd);
                        getRequestField().getCaret().setVisible(true);
                    }
                }
            });
        }
        
        return removeCustomButton;
    }

    private void setFieldStates() {
        int userDefStart = getRequestField().getSelectionStart();

        if (getBoolValue(FIELD_RECURSE)) {
            // Dont support custom vectors when recursing
            customPanelStatus.setText(Constant.messages.getString("ascan.custom.status.recurse"));
            getAddCustomButton().setEnabled(false);
            getRemoveCustomButton().setEnabled(false);
        
        } else {
            customPanelStatus.setText(Constant.messages.getString("ascan.custom.status.highlight"));
            if (userDefStart >= 0) {
                int userDefEnd = getRequestField().getSelectionEnd();
                if (selectionIncludesHighlight(userDefStart, userDefEnd,
                        getRequestField().getHighlighter().getHighlights())) {
                    getAddCustomButton().setEnabled(false);
                    getRemoveCustomButton().setEnabled(true);
                
                } else if (userDefStart < urlPathStart) {
                    // No point attacking the method, hostname or port 
                    getAddCustomButton().setEnabled(false);
                
                } else if (userDefStart < headerLength && userDefEnd > headerLength) {
                    // The users selection cross the header / body boundry - thats never going to work well
                    getAddCustomButton().setEnabled(false);
                    getRemoveCustomButton().setEnabled(false);
                
                } else {
                    getAddCustomButton().setEnabled(true);
                    getRemoveCustomButton().setEnabled(false);
                }
                
            } else {
                // Nothing selected
                getAddCustomButton().setEnabled(false);
                getRemoveCustomButton().setEnabled(false);
            }
        }
        
        getRequestField().getCaret().setVisible(true);
    }

    private JList<Highlight> getInjectionPointList() {
        if (injectionPointList == null) {
            injectionPointList = new JList<Highlight>(injectionPointModel);
            injectionPointList.setCellRenderer(new ListCellRenderer<Highlight>() {
                @Override
                public Component getListCellRendererComponent(
                        JList<? extends Highlight> list, Highlight hlt,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    String str = "";
                    try {
                        str = getRequestField().getText(hlt.getStartOffset(), hlt.getEndOffset() - hlt.getStartOffset());
                        if (str.length() > 8) {
                            // just show first 8 chrs (arbitrary limit;)
                            str = str.substring(0, 8) + "..";
                        }
                    } catch (BadLocationException e) {
                        // Ignore
                    }
                    
                    return new JLabel("[" + hlt.getStartOffset() + "," + hlt.getEndOffset() + "]: " + str);
                }
            });
        }
        return injectionPointList;
    }

    private boolean selectionIncludesHighlight(int start, int end, Highlight hl) {
        if (hl.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
            DefaultHighlighter.DefaultHighlightPainter ptr = (DefaultHighlighter.DefaultHighlightPainter) hl.getPainter();
            if (ptr.getColor() != null && ptr.getColor().equals(Color.RED)) {
                // Test for 'RED' needed to prevent matching the users selection
                return start < hl.getEndOffset() && end > hl.getStartOffset();
            }
        }
        return false;
    }

    private boolean selectionIncludesHighlight(int start, int end, Highlight[] hls) {
        for (Highlight hl : hls) {
            if (this.selectionIncludesHighlight(start, end, hl)) {
                return true;
            }
        }
        return false;
    }

    private void reset(boolean refreshUi) {
        FileConfiguration fileConfig = new XMLConfiguration();
        ConfigurationUtils.copy(extension.getScannerParam().getConfig(), fileConfig);

        scannerParam = new ScannerParam();
        scannerParam.load(fileConfig);

        optionsParam = new OptionsParam();
        optionsParam.load(fileConfig);

        pluginFactory = Control.getSingleton().getPluginFactory().clone();

        if (refreshUi) {
            init(node);
            repaint();
        }
    }

    @Override
    public String getSaveButtonText() {
        return Constant.messages.getString("ascan.custom.button.scan");
    }

    @Override
    public JButton[] getExtraButtons() {
        if (extraButtons == null) {
            JButton resetButton = new JButton(Constant.messages.getString("ascan.custom.button.reset"));
            resetButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reset(true);
                }
            });

            extraButtons = new JButton[]{resetButton};
        }
        return extraButtons;
    }

    @Override
    public void save() {
        // Set Injectable Targets
        int targets = 0;
        if (this.getBoolValue(FIELD_VARIANT_URL_QUERY)) {
            targets |= ScannerParam.TARGET_QUERYSTRING;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_URL_PATH)) {
            targets |= ScannerParam.TARGET_URLPATH;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_POST_DATA)) {
            targets |= ScannerParam.TARGET_POSTDATA;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_HEADERS)) {
            targets |= ScannerParam.TARGET_HTTPHEADERS;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_COOKIE)) {
            targets |= ScannerParam.TARGET_COOKIE;
        }
        
        this.scannerParam.setTargetParamsInjectable(targets);

        // Set Enabled RPC schemas
        int enabledRpc = 0;
        if (this.getBoolValue(FIELD_VARIANT_MULTIPART)) {
            enabledRpc |= ScannerParam.RPC_MULTIPART;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_XML)) {
            enabledRpc |= ScannerParam.RPC_XML;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_JSON)) {
            enabledRpc |= ScannerParam.RPC_JSON;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_GWT)) {
            enabledRpc |= ScannerParam.RPC_GWT;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_ODATA)) {
            enabledRpc |= ScannerParam.RPC_ODATA;
        }
        
        if (this.getBoolValue(FIELD_VARIANT_CUSTOM)) {
            enabledRpc |= ScannerParam.RPC_CUSTOM;
        }
        
        if (!getBoolValue(FIELD_RECURSE) && injectionPointModel.getSize() > 0) {
            int[][] injPoints = new int[injectionPointModel.getSize()][];
            for (int i = 0; i < injectionPointModel.getSize(); i++) {
                Highlight hl = injectionPointModel.elementAt(i);
                injPoints[i] = new int[2];
                injPoints[i][0] = hl.getStartOffset();
                injPoints[i][1] = hl.getEndOffset();
            }

            try {
                VariantUserDefined.setInjectionPoints(
                        this.node.getHistoryReference().getURI().toString(),
                        injPoints);
                
                enabledRpc |= ScannerParam.RPC_USERDEF;
                
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        scannerParam.setTargetParamsEnabledRPC(enabledRpc);

        Object[] contextSpecificObjects = new Object[]{
            scannerParam,
            pluginFactory.clone()
        };

        this.extension.startScanCustom(
                node,
                this.getBoolValue(FIELD_INSCOPE),
                this.getBoolValue(FIELD_RECURSE),
                getSelectedContext(), 
                getSelectedUser(), 
                contextSpecificObjects);
    }

    @Override
    public String validateFields() {
        if (this.node == null) {
            return Constant.messages.getString("ascan.custom.nostart.error");
        }
        
        return null;
    }
}
