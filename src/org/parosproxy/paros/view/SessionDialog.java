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
// ZAP: 2012/08/29 Issue 250 Support for authentication management (enlarged window size)
// ZAP: 2013/03/03 Issue 547: Deprecate unused classes and methods

package org.parosproxy.paros.view;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.util.HashMap;
import java.util.Map;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.view.AbstractContextPropertiesPanel;

public class SessionDialog extends AbstractParamDialog {

	private static final long serialVersionUID = 2078860056416521552L;

	/** The map of duplicate of the contexts, used for temporary changes in the UI. */
	private Map<Integer, Context> uiContexts = new HashMap<Integer, Context>();

	public SessionDialog() {
		super();
		initialize();
	}

	/**
	 * @deprecated No longer used/needed. It will be removed in a future release. Use the
	 *             constructor
	 *             {@link #SessionDialog(Frame parent, boolean modal, String title, String rootName)}
	 *             instead.
	 */
	@Deprecated
	public SessionDialog(Frame parent, boolean modal, String title) throws HeadlessException {
		super(parent, modal, title, "Session");
		initialize();
	}

	public SessionDialog(Frame parent, boolean modal, String title, String rootName) {
		super(parent, modal, title, rootName);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		if (Model.getSingleton().getOptionsParam().getViewParam().getWmUiHandlingOption() == 0) {
			this.setSize(650, 500);
		} else {
			pack();
		}
	}

	@Override
	public void initParam(Object obj) {
		super.paramObject = obj;
		// Send the 'ui context' duplicate to Context Properties Panels
		prepareUIContextCopies(Model.getSingleton().getSession());
		for (AbstractParamPanel panel : super.getPanels()) {
			if (panel instanceof AbstractContextPropertiesPanel) {
				AbstractContextPropertiesPanel contextPanel = (AbstractContextPropertiesPanel) panel;
				contextPanel.initContextData((Session) obj, uiContexts.get(contextPanel.getContextIndex()));
			} else {
				panel.initParam(obj);
			}
		}
	}

	private void prepareUIContextCopies(Session session) {
		uiContexts.clear();
		for (Context context : session.getContexts()) {
			Context uiContext = context.duplicate();
			uiContexts.put(context.getIndex(), uiContext);
		}
	}
}
