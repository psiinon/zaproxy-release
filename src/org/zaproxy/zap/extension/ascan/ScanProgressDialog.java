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
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;
import org.zaproxy.zap.view.LayoutHelper;

/**
 * Dialog reviewed for a new lifestyle...
 * @author yhawke (2014)
 */
public class ScanProgressDialog extends AbstractDialog {

    private static final long serialVersionUID = 1L;

    // Default table background color
    //private static final Color JTABLE_ALTERNATE_BACKGROUND = UIManager.getColor("Table.alternateRowColor");
    private transient Color JTABLE_ALTERNATE_BACKGROUND = (Color)LookAndFeel.getDesktopPropertyValue("Table.alternateRowColor", new Color(0xf2f2f2));
    
    private JScrollPane jScrollPane = null;
    private JTable table;
    private ScanProgressTableModel model;
    
    private String site = null;
    private ActiveScan scan = null;
    private boolean stopThread = false;

    /**
     * 
     * @param owner
     * @param site 
     */
    public ScanProgressDialog(Frame owner, String site) {
        super(owner, false);
        this.site = site;
        this.initialize();
    }

    /**
     * 
     */
    private void initialize() {
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(560, 504));

        if (site != null) {
            this.setTitle(MessageFormat.format(
                    Constant.messages.getString("ascan.progress.title"), site));
        }

        this.add(getJScrollPane(), LayoutHelper.getGBC(0, 0, 1, 1.0D, 1.0D));

        //  Handle escape key to close the dialog    
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        AbstractAction escapeAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                stopThread = true;
                ScanProgressDialog.this.setVisible(false);
                ScanProgressDialog.this.dispose();
            }
        };
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        // Stop the updating thread when the window is closed
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                // Ignore
            }

            @Override
            public void windowClosing(WindowEvent e) {
                // Ignore
            }

            @Override
            public void windowClosed(WindowEvent e) {
                stopThread = true;
            }

            @Override
            public void windowIconified(WindowEvent e) {
                // Ignore
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // Ignore
            }

            @Override
            public void windowActivated(WindowEvent e) {
                // Ignore
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // Ignore
            }
        });
    }

    /**
     * Get the dialog scroll panel
     * @return the panel
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getMainPanel());
            jScrollPane.setName("ScanProgressScrollPane");
            jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
        }
        
        return jScrollPane;
    }

    /**
     * Get the main content panel of the dialog
     * @return the main panel
     */
    private JTable getMainPanel() {
        if (table == null) {            
            model = new ScanProgressTableModel();
            
            table = new JTable();
            table.setModel(model);
            table.setRowSelectionAllowed(false);
            table.setColumnSelectionAllowed(false);
            table.setDoubleBuffered(true);

            // First column is for plugin's name
            table.getColumnModel().getColumn(0).setPreferredWidth(256);
            table.getColumnModel().getColumn(1).setPreferredWidth(80);

            // Second column is for plugin's status
            table.getColumnModel().getColumn(2).setPreferredWidth(80);
            table.getColumnModel().getColumn(2).setCellRenderer(new ScanProgressBarRenderer());

            // Third column is for plugin's elapsed time
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(3).setPreferredWidth(80);                  
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            
            // Fourth column is for plugin's completion and actions
            table.getColumnModel().getColumn(4).setPreferredWidth(40);                  
            table.getColumnModel().getColumn(4).setCellRenderer(new ScanProgressActionRenderer());

            ScanProgressActionListener listener = new ScanProgressActionListener(table);
            table.addMouseListener(listener);
            table.addMouseMotionListener(listener);
        }
        
        return table;
    }
    
    /**
     * Update the current Scan progress and
     * prepare actions and scan summary
     */
    private void showProgress() {
        // Start panel data settings
        if (scan.getHostProcesses() != null) {

            // Update the main table entries
            model.updateValues(scan);

            if (model.isAllPluginsCompleted()) {
                this.stopThread = true;
            }
        }
    }

    /**
     * Set the scan that need to be used for this dialog
     * @param scan the active scan
     */
    public void setActiveScan(ActiveScan scan) {
        this.scan = scan;

        if (scan == null) {
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!stopThread) {
                    showProgress();
                    
                    try {
                        sleep(200);
                        
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
            }
        };
        
        thread.start();
    }

    /**
     * --------------------------------------------------
     * Custom Renderer for the progress bar plugin column
     * -------------------------------------------------- 
     */
    private class ScanProgressBarRenderer implements TableCellRenderer {

        /**
         *
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent result;
            if (value != null) {
                ScanProgressItem item = (ScanProgressItem)value;                
                JProgressBar bar = new JProgressBar();
                bar.setMaximum(100);
                bar.setValue(item.getProgressPercentage());                
                result = bar;
                
            } else {
                result = (JComponent)Box.createGlue();
            }
            
            // Set all general configurations
            result.setOpaque(true);
            result.setBackground(JTABLE_ALTERNATE_BACKGROUND);
            
            return result;
        }
    }    
    
    /**
     * --------------------------------------------------
     * Custom Renderer for the actions column (skipping)
     * --------------------------------------------------
     */
    private class ScanProgressActionRenderer implements TableCellRenderer {
        
        /**
         *
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent result;
            if (value != null) {
                ScanProgressActionIcon action = (ScanProgressActionIcon)value;                
                if (action == model.getFocusedAction()) {
                    action.setOver();
                    
                } else {
                    action.setNormal();
                }
                
                result = action;
                
            } else {
                result = (JComponent)Box.createGlue();
            }
         
            // Set all general configurations
            result.setOpaque(true);
            result.setBackground(JTABLE_ALTERNATE_BACKGROUND);
            
            return result;
        }
    }

    /**
     * --------------------------------------------------
     * Listener for all Action's management (skipping for now)
     * --------------------------------------------------
     */
    private class ScanProgressActionListener implements MouseListener, MouseMotionListener {

        private JTable table;

        /**
         * 
         * @param table 
         */
        public ScanProgressActionListener(JTable table) {
            this.table = table;
        }

        /**
         * 
         * @param e 
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            ScanProgressActionIcon action = getScanProgressAction(e);
            if (action != null) {
                action.invokeAction();
            }            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        /**
         * 
         * @param e 
         */
        @Override
        public void mousePressed(MouseEvent e) {
            ScanProgressActionIcon action = getScanProgressAction(e);
            if (action != null) {
                action.setPressed();
                action.repaint();
            }
        }

        /**
         * 
         * @param e 
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            ScanProgressActionIcon action = getScanProgressAction(e);
            if (action != null) {
                action.setReleased();
                action.repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent me) {
        }

        /**
         * 
         * @param me 
         */
        @Override
        public void mouseMoved(MouseEvent me) {
            ScanProgressActionIcon action = getScanProgressAction(me);
            if (action != null) {
                model.setFocusedAction(action);
                action.repaint();
                
            } else if (model.getFocusedAction() != null) {
                model.setFocusedAction(action);
                table.repaint();
            }
        }

        /**
         * 
         * @param e
         * @return 
         */
        private ScanProgressActionIcon getScanProgressAction(MouseEvent e) {
            TableColumnModel columnModel = table.getColumnModel();
            int column = columnModel.getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();

            if ((row < table.getRowCount()) && (row >= 0) && 
                (column < table.getColumnCount()) && (column >= 0)) {

                Object value = table.getValueAt(row, column);
                if (value instanceof ScanProgressActionIcon) {
                    return (ScanProgressActionIcon)value;
                }
            }
            
            return null;
        }        
    }    
}
