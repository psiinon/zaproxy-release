/*
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2003-2004 Chinotec Technologies Company
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Clarified Artistic License
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Clarified Artistic License for more details.
 * 
 * You should have received a copy of the Clarified Artistic License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
// ZAP: 2011/11/20 Set order
// ZAP: 2012/03/17 Issue 282 Added getAuthor()
// ZAP: 2012/04/25 Added @Override annotation to all appropriate methods.
// ZAP: 2013/03/03 Issue 546: Remove all template Javadoc comments
// ZAP: 2014/01/28 Issue 207: Support keyboard shortcuts 

package org.parosproxy.paros.extension.edit;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.FindDialog;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionEdit extends ExtensionAdaptor {

    private FindDialog findDialog = null;
    private ZapMenuItem menuFind = null;
    private PopupFindMenu popupFindMenu = null;

    /**
     * 
     */
    public ExtensionEdit() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionEdit(String name) {
        super(name);
    }

	/**
	 * This method initializes this
	 */
	private void initialize() {
        this.setName("ExtensionEdit");
        this.setOrder(4);
	}
	

	@Override
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);

	    if (getView() != null) {	        
	        extensionHook.getHookMenu().addEditMenuItem(getMenuFind());
	        extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuFind());
	        
	    }

	}
    
    private void showFindDialog(JFrame frame, JTextComponent lastInvoker) {
        if (findDialog == null || findDialog.getParent() != frame) {
            findDialog = new FindDialog(frame, false);            
        }
        
        findDialog.setLastInvoker(lastInvoker);
        findDialog.setVisible(true);
    }

    /**
     * This method initializes menuFind	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private ZapMenuItem getMenuFind() {
        if (menuFind == null) {
            menuFind = new ZapMenuItem("menu.edit.find", 
            		KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK, false));

            menuFind.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showFindDialog(getView().getMainFrame(), null);
                }
            });
        }
        return menuFind;
    }

    /**
     * This method initializes popupMenuFind	
     * 	
     * @return org.parosproxy.paros.extension.ExtensionPopupMenu	
     */
    private PopupFindMenu getPopupMenuFind() {
        if (popupFindMenu== null) {
            popupFindMenu = new PopupFindMenu();
            popupFindMenu.setText(Constant.messages.getString("edit.find.popup"));	// ZAP: i18n
            popupFindMenu.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showFindDialog(popupFindMenu.getParentFrame(), popupFindMenu.getLastInvoker());
                    
                }
            });
        }
        return popupFindMenu;
    }
	
	@Override
	public String getAuthor() {
		return Constant.PAROS_TEAM;
	}
}
