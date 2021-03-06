/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2011 The Zed Attack Proxy team
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
package org.zaproxy.zap.extension.pscan;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.Plugin.AlertThreshold;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.zaproxy.zap.extension.ascan.PolicyAllCategoryPanel;

public class PolicyPassiveScanPanel extends AbstractParamPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableTest = null;
    private JScrollPane jScrollPane = null;
    private PolicyPassiveScanTableModel passiveScanTableModel = null;

    /**
     *
     */
    public PolicyPassiveScanPanel() {
        super();
        initialize();
    }

    /**
     * Set the all category panel thta should be updated when config changed
     * @param allPanel 
     */
    public void setAllCategoryPanel(PolicyAllCategoryPanel allPanel) {
        this.getPassiveScanTableModel().setAllCategoryPanel(allPanel);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        if (Model.getSingleton().getOptionsParam().getViewParam().getWmUiHandlingOption() == 0) {
            this.setSize(375, 204);
        }
        
        this.setName(Constant.messages.getString("pscan.policy.title"));
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.weighty = 1.0;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 1;
        gridBagConstraints11.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(getJScrollPane(), gridBagConstraints11);
    }
    
    private static final int[] width = {300, 60, 100};

    /**
     * This method initializes tableTest
     *
     * @return javax.swing.JTable
     */
    private JTable getTableTest() {
        if (tableTest == null) {
            tableTest = new JTable();
            tableTest.setModel(getPassiveScanTableModel());
            tableTest.setRowHeight(18);
            tableTest.setIntercellSpacing(new java.awt.Dimension(1, 1));
            tableTest.setAutoCreateRowSorter(true);
            
            //Default sort by name (column 0)
            List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(1);
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            tableTest.getRowSorter().setSortKeys(sortKeys);
            
            for (int i = 0; i < tableTest.getColumnCount()-1; i++) {
                TableColumn column = tableTest.getColumnModel().getColumn(i);
                column.setPreferredWidth(width[i]);
            }
            
            JComboBox<String> jcb1 = new JComboBox<>();
            for (AlertThreshold level : AlertThreshold.values()) {
                jcb1.addItem(Constant.messages.getString("ascan.policy.level." + level.name().toLowerCase()));
            }
            
            tableTest.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcb1));
        }
        
        return tableTest;
    }

    @Override
    public void initParam(Object obj) {
    }

    @Override
    public void validateParam(Object obj) throws Exception {
    }

    @Override
    public void saveParam(Object obj) throws Exception {
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTableTest());
            jScrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        }
        
        return jScrollPane;
    }

    /**
     * This method initializes categoryTableModel
     *
     * @return org.parosproxy.paros.plugin.scanner.CategoryTableModel
     */
    public PolicyPassiveScanTableModel getPassiveScanTableModel() {
        if (passiveScanTableModel == null) {
            passiveScanTableModel = new PolicyPassiveScanTableModel();
        }

        return passiveScanTableModel;
    }

    public void setPassiveScanTableModel(PolicyPassiveScanTableModel categoryTableModel) {
        this.passiveScanTableModel = categoryTableModel;
    }

    @Override
    public String getHelpIndex() {
        return "ui.dialogs.options.pscan";
    }
}
