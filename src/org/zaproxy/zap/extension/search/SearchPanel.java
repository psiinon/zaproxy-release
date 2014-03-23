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
package org.zaproxy.zap.extension.search;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.httppanel.HttpPanelRequest;
import org.zaproxy.zap.extension.httppanel.HttpPanelResponse;
import org.zaproxy.zap.utils.ZapTextField;
import org.zaproxy.zap.view.ZapToggleButton;


public class SearchPanel extends AbstractPanel implements SearchListenner {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @deprecated (2.3.0) Replaced by {@link #HTTP_MESSAGE_CONTAINER_NAME}.
	 */
	@Deprecated
	public static final String PANEL_NAME = "search";

	/**
	 * The name of the search results HTTP messages container.
	 * 
	 * @see org.zaproxy.zap.view.messagecontainer.http.HttpMessageContainer
	 */
	public static final String HTTP_MESSAGE_CONTAINER_NAME = "SearchHttpMessageContainer";

	private ExtensionSearch extension;
	
	private javax.swing.JPanel panelCommand = null;
	private javax.swing.JToolBar panelToolbar = null;
	private JScrollPane jScrollPane = null;

	private ZapToggleButton scopeButton = null;
	private ZapTextField regEx = null;
	private JButton btnSearch = null;
	private JComboBox<String> searchType = null;
	private JButton btnNext = null;
	private JButton btnPrev = null;
	private JCheckBox chkInverse = null;
	private JLabel numberOfMatchesLabel;
	private JButton optionsButton;
	
	private MessageFormat numberOfMatchesFormat;

	private SearchResultsTable resultsTable;
	private SearchResultsTableModel resultsModel;

	private HttpPanelRequest requestPanel = null;
	private HttpPanelResponse responsePanel = null;

    //private static Logger log = Logger.getLogger(SearchPanel.class);

    /**
     * 
     */
    public SearchPanel() {
        super();
 		initialize();
    }

	public ExtensionSearch getExtension() {
		return extension;
	}

	public void setExtension(ExtensionSearch extension) {
		this.extension = extension;
	}

	/**
	 * This method initializes this
	 */
	private  void initialize() {
		resultsModel = new SearchResultsTableModel();
		resultsTable = new SearchResultsTable(resultsModel);
		resultsTable.setName(HTTP_MESSAGE_CONTAINER_NAME);

		resultsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					SearchResult searchResult = resultsTable.getSelectedSearchResult();
					if (searchResult == null) {
						return;
					}

					displayMessage(resultsTable.getSelectedSearchResult());

					// Get the focus back so that the arrow keys work
					resultsTable.requestFocusInWindow();
				}
			}
		});

        this.setLayout(new CardLayout());
        //this.setSize(474, 251);
        this.setName(Constant.messages.getString("search.panel.title"));
		this.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/16/049.png")));	// 'magnifying glass' icon
        this.add(getPanelCommand(), getPanelCommand().getName());
	}
	
	/**
	 * This method initializes panelCommand	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	/**/
	private javax.swing.JPanel getPanelCommand() {
		if (panelCommand == null) {

			panelCommand = new javax.swing.JPanel();
			panelCommand.setLayout(new java.awt.GridBagLayout());
			panelCommand.setName("Search Panel");
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			
			panelCommand.add(this.getPanelToolbar(), gridBagConstraints1);
			panelCommand.add(getJScrollPane(), gridBagConstraints2);

		}
		return panelCommand;
	}
	/**/

	private GridBagConstraints newGBC (int gridx) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		return gridBagConstraints;
	}
	
	private javax.swing.JToolBar getPanelToolbar() {
		if (panelToolbar == null) {
			
			panelToolbar = new javax.swing.JToolBar();
			panelToolbar.setLayout(new java.awt.GridBagLayout());
			panelToolbar.setEnabled(true);
			panelToolbar.setFloatable(false);
			panelToolbar.setRollover(true);
			panelToolbar.setPreferredSize(new java.awt.Dimension(800,30));
			panelToolbar.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			panelToolbar.setName("Search Toolbar");
			
			GridBagConstraints gridBagConstraintsX = new GridBagConstraints();
			gridBagConstraintsX.gridx = 20;
			gridBagConstraintsX.gridy = 0;
			gridBagConstraintsX.weightx = 1.0;
			gridBagConstraintsX.weighty = 1.0;
			gridBagConstraintsX.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraintsX.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraintsX.fill = java.awt.GridBagConstraints.HORIZONTAL;

			GridBagConstraints optionsGridBag = new GridBagConstraints();
			optionsGridBag.gridx = gridBagConstraintsX.gridx +1;
			optionsGridBag.gridy = 0;
			optionsGridBag.insets = new java.awt.Insets(0, 0, 0, 0);
			optionsGridBag.anchor = java.awt.GridBagConstraints.EAST;

			JLabel t1 = new JLabel();
			JLabel inverseTooltip = new JLabel(Constant.messages.getString("search.toolbar.label.inverse"));
			inverseTooltip.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.inverse"));

			panelToolbar.add(getScopeButton(), newGBC(0));
			panelToolbar.add(getRegExField(), newGBC(1));
			panelToolbar.add(getSearchType(), newGBC(2));
			panelToolbar.add(inverseTooltip, newGBC(3));
			panelToolbar.add(getChkInverse(), newGBC(4));
			panelToolbar.add(getBtnSearch(), newGBC(5));
			panelToolbar.add(getBtnNext(), newGBC(6));
			panelToolbar.add(getBtnPrev(), newGBC(7));
			panelToolbar.add(new JToolBar.Separator(), newGBC(8));
			panelToolbar.add(getNumberOfMatchesLabel(), newGBC(9));
			panelToolbar.add(t1, gridBagConstraintsX);
			panelToolbar.add(getOptionsButton(), optionsGridBag);
		}
		return panelToolbar;
	}
	
	private JToggleButton getScopeButton() {
		if (scopeButton == null) {
			scopeButton = new ZapToggleButton();
			scopeButton.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/fugue/target-grey.png")));
			scopeButton.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.scope.unselected"));
			scopeButton.setSelectedIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/fugue/target.png")));
			scopeButton.setSelectedToolTipText(Constant.messages.getString("search.toolbar.tooltip.scope.selected"));

			scopeButton.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					extension.setSearchJustInScope(scopeButton.isSelected());
				}
			});
		}
		return scopeButton;
	}

	
	private JCheckBox getChkInverse () {
		if (chkInverse == null) {
			chkInverse = new JCheckBox();
			chkInverse.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.inverse"));
		}
		return chkInverse;
	}
	
	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton();
			btnSearch.setText(Constant.messages.getString("search.toolbar.label.search"));
			btnSearch.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/16/049.png")));	// 'magnifying glass' icon
			btnSearch.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.search"));

			btnSearch.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Pattern pattern;
					try {
						pattern = Pattern.compile(regEx.getText());
					} catch (IllegalArgumentException e) {
						regEx.requestFocusInWindow();
						View.getSingleton()
								.showWarningDialog(Constant.messages.getString("search.toolbar.error.invalid.regex"));
						return;
					}

					if (pattern.matcher("").find()) {
						int option = JOptionPane.showOptionDialog(
								View.getSingleton().getMainFrame(),
								Constant.messages.getString("search.toolbar.warn.regex.match.empty.string.text"),
								Constant.messages.getString("search.toolbar.warn.regex.match.empty.string.title"),
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								new String[] {
										Constant.messages.getString("search.toolbar.warn.regex.match.empty.string.button.search"),
										Constant.messages.getString("search.toolbar.warn.regex.match.empty.string.button.cancel") },
								null);
						if (option != JOptionPane.OK_OPTION) {
							regEx.requestFocusInWindow();
							return;
						}
					}

					doSearch();
				}
			});
		}
		return btnSearch;
	}

	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton();
			btnNext.setText(Constant.messages.getString("search.toolbar.label.next"));
			btnNext.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/16/107.png")));	// 'arrow down' icon
			btnNext.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.next"));

			btnNext.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					highlightNextResult();
				}
			});
		}
		return btnNext;
	}

	private JButton getBtnPrev() {
		if (btnPrev == null) {
			btnPrev = new JButton();
			btnPrev.setText(Constant.messages.getString("search.toolbar.label.previous"));
			btnPrev.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/16/108.png")));	// 'arrow up' icon
			btnPrev.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.previous"));

			btnPrev.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					highlightPrevResult();
				}
			});
		}
		return btnPrev;
	}

	protected ZapTextField getRegExField () {
		if (regEx == null) {
			regEx = new ZapTextField();
			regEx.setHorizontalAlignment(ZapTextField.LEFT);
			regEx.setAlignmentX(0.0F);
			regEx.setPreferredSize(new java.awt.Dimension(250,25));
			regEx.setText("");
			regEx.setToolTipText(Constant.messages.getString("search.toolbar.tooltip.regex"));
			regEx.setMinimumSize(new java.awt.Dimension(250,25));
			
			regEx.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSearch();
				}
			});

		}
		return regEx;
	}

	private JLabel getNumberOfMatchesLabel() {
		if (numberOfMatchesLabel == null) {
			numberOfMatchesLabel = new JLabel();
			numberOfMatchesFormat = new MessageFormat(Constant.messages.getString("search.toolbar.label.number.of.matches"));
			setNumberOfMatches(0);
		}
		return numberOfMatchesLabel;
	}

	private void setNumberOfMatches(int numberOfMatches) {
		numberOfMatchesLabel.setText(numberOfMatchesFormat.format(new Object[] { Integer.valueOf(numberOfMatches) }));
	}

	private JButton getOptionsButton() {
		if (optionsButton == null) {
			optionsButton = new JButton();
			optionsButton.setToolTipText(Constant.messages.getString("search.toolbar.button.options"));
			optionsButton.setIcon(new ImageIcon(SearchPanel.class.getResource("/resource/icon/16/041.png")));
			optionsButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Control.getSingleton()
							.getMenuToolsControl()
							.options(Constant.messages.getString("search.optionspanel.name"));
				}
			});
		}
		return optionsButton;
	}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setViewportView(resultsTable);
		}
		return jScrollPane;
	}

	public void resetSearchResults() {
		setNumberOfMatches(0);
		resultsModel.clear();
	}

	@Override
	public void addSearchResult(final SearchResult str) {
		if (EventQueue.isDispatchThread()) {
			resultsModel.addSearchResult(str);

			setNumberOfMatches(resultsModel.getRowCount());
			if (resultsModel.getRowCount() == 1) {
				highlightFirstResult();
			}
		} else {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
				    addSearchResult(str);
				}
			});
		}
	}

    public void setDisplayPanel(HttpPanelRequest requestPanel, HttpPanelResponse responsePanel) {
        this.requestPanel = requestPanel;
        this.responsePanel = responsePanel;
    }
    
    private void doSearch() {
    	ExtensionSearch.Type type = ExtensionSearch.Type.All;
    	
    	if (Constant.messages.getString("search.toolbar.label.type.url").equals(searchType.getSelectedItem())) {
    		type = ExtensionSearch.Type.URL;
    	} else if (Constant.messages.getString("search.toolbar.label.type.request").equals(searchType.getSelectedItem())) {
    		type = ExtensionSearch.Type.Request;
    	} else if (Constant.messages.getString("search.toolbar.label.type.response").equals(searchType.getSelectedItem())) {
    		type = ExtensionSearch.Type.Response;
    	} else if (Constant.messages.getString("search.toolbar.label.type.header").equals(searchType.getSelectedItem())) {
    		type = ExtensionSearch.Type.Header;
    	} else if (Constant.messages.getString("search.toolbar.label.type.fuzz").equals(searchType.getSelectedItem())) {
    		type = ExtensionSearch.Type.Fuzz;
    	}

    	setNumberOfMatches(0);
    	extension.search(regEx.getText(), type, false, chkInverse.isSelected());
		
		// Select first result
		if (resultsTable.getModel().getRowCount() > 0) {
			resultsTable.getSelectionModel().setSelectionInterval(0, 0);
			resultsTable.requestFocus();
		}
    }
    
    protected void setSearchType(ExtensionSearch.Type type) {
    	switch (type) {
    	case All:  	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.all")); break;
    	case URL:	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.url")); break;
    	case Request:	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.request")); break;
    	case Response:	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.response")); break;
    	case Header:	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.header")); break;
    	case Fuzz:	this.getSearchType().setSelectedItem(Constant.messages.getString("search.toolbar.label.type.fuzz")); break;
    	}
    }
    
    private void displayMessage(SearchResult sr) {
        HttpMessage msg = sr.getMessage();
        if (msg == null) {
            return;
        }

        if (msg.getRequestHeader().isEmpty()) {
            requestPanel.clearView(true);
        } else {
            requestPanel.setMessage(msg);
        }
        
        if (msg.getResponseHeader().isEmpty()) {
            responsePanel.clearView(false);
        } else {
            responsePanel.setMessage(msg, true);
        }
        highlightFirstResult(sr);
    }
    
    private void highlightMatch (SearchMatch sm) {
    	if (sm == null) {
    		return;
    	}
    	
    	switch (sm.getLocation()) {
    	case REQUEST_HEAD:
    		requestPanel.highlightHeader(sm);
    		requestPanel.setTabFocus();
    		requestPanel.requestFocus(); 
    		break;
    	case REQUEST_BODY:	
    		requestPanel.highlightBody(sm);
    		requestPanel.setTabFocus();
    		requestPanel.requestFocus(); 
    		break;
    	case RESPONSE_HEAD:	
    		responsePanel.highlightHeader(sm);
    		responsePanel.setTabFocus();
    		responsePanel.requestFocus(); 
    		break;
    	case RESPONSE_BODY:
    		responsePanel.highlightBody(sm);
    		responsePanel.setTabFocus();
    		responsePanel.requestFocus(); 
    		break;
    	}

    }
    
    private void highlightFirstResult (SearchResult sr) {
    	highlightMatch(sr.getFirstMatch(requestPanel, responsePanel));
    }
    
    protected void highlightFirstResult() {
		if (resultsTable.getModel().getRowCount() > 0) {
			resultsTable.getSelectionModel().setSelectionInterval(0, 0);
    		resultsTable.scrollRowToVisible(0);
			resultsTable.requestFocus();
		}
    }

    protected void highlightNextResult () {
    	if (resultsTable.getSelectedSearchResult() == null) {
    		this.highlightFirstResult();
    		return;
    	}
    	
    	SearchResult sr = resultsTable.getSelectedSearchResult();
    	SearchMatch sm = sr.getNextMatch();
    	
    	if (sm != null) {
    		highlightMatch(sm);
    	} else {
    		// Next record
        	if (resultsTable.getSelectedRow() < resultsTable.getModel().getRowCount()-1) {
        		resultsTable.getSelectionModel().setSelectionInterval(resultsTable.getSelectedRow() + 1, resultsTable.getSelectedRow() + 1);
        		resultsTable.scrollRowToVisible(resultsTable.getSelectedRow());
        	} else {
        		this.highlightFirstResult();
        	}
    	}
    }

    private void highlightLastResult (SearchResult sr) {
    	highlightMatch(sr.getLastMatch(requestPanel, responsePanel));
    }

    protected void highlightPrevResult () {
    	if (resultsTable.getSelectedSearchResult() == null) {
    		this.highlightFirstResult();
    		return;
    	}
    	
    	SearchResult sr = resultsTable.getSelectedSearchResult();
    	SearchMatch sm = sr.getPrevMatch();
    	
    	if (sm != null) {
    		highlightMatch(sm);
    	} else {
    		// Previous record
        	if (resultsTable.getSelectedRow() > 0) {
        	    resultsTable.getSelectionModel().setSelectionInterval(resultsTable.getSelectedRow() - 1, resultsTable.getSelectedRow() - 1);
        	} else {
        	    resultsTable.getSelectionModel().setSelectionInterval(resultsTable.getModel().getRowCount()-1, resultsTable.getRowCount() - 1);
        	}
    		resultsTable.scrollRowToVisible(resultsTable.getSelectedRow());
    		highlightLastResult(resultsTable.getSelectedSearchResult());
    	}
    }

    private JComboBox<String> getSearchType () {
    	if (searchType == null) {
	    	searchType = new JComboBox<>();
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.all"));
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.url"));
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.request"));
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.response"));
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.header"));
	    	searchType.addItem(Constant.messages.getString("search.toolbar.label.type.fuzz"));
    	}
    	return searchType;
    }
    
    public void searchFocus() {
    	this.setTabFocus();
        getRegExField().requestFocus();

    }

	@Override
	public void searchComplete() {
		// Ignore
	}
}