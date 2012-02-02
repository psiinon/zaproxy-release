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
package org.zaproxy.zap.extension.httppanel.plugin.response.split;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.httppanel.view.text.AutoDetectSyntaxHttpPanelTextArea;
import org.zaproxy.zap.extension.search.SearchMatch;

public class HttpResponseBodyPanelTextArea extends AutoDetectSyntaxHttpPanelTextArea {

	private static final long serialVersionUID = -8952571125337022950L;
	
	private static final String CSS = Constant.messages.getString("http.panel.text.syntax.css");
	private static final String HTML = Constant.messages.getString("http.panel.text.syntax.html");
	private static final String JAVA_SCRIPT = Constant.messages.getString("http.panel.text.syntax.javascript");
	private static final String XML = Constant.messages.getString("http.panel.text.syntax.xml");
	
	private static ResponseBodyTokenMakerFactory tokenMakerFactory = null;
	
	public HttpResponseBodyPanelTextArea(HttpMessage httpMessage) {
		super(httpMessage);
		
		addSyntaxStyle(CSS, SyntaxConstants.SYNTAX_STYLE_CSS);
		addSyntaxStyle(HTML, SyntaxConstants.SYNTAX_STYLE_HTML);
		addSyntaxStyle(JAVA_SCRIPT, SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		addSyntaxStyle(XML, SyntaxConstants.SYNTAX_STYLE_XML);
	}

	@Override
	public SearchMatch getTextSelection() {
		SearchMatch sm = new SearchMatch(
				getHttpMessage(),
				SearchMatch.Location.RESPONSE_BODY,
				getSelectionStart(),
				getSelectionEnd());
				
		return sm;
	}
	
	@Override
	public void highlight(SearchMatch sm) {
		if (!SearchMatch.Location.RESPONSE_BODY.equals(sm.getLocation())) {
			return;
		}
		
		int len = getText().length();
		if (sm.getStart() > len || sm.getEnd() > len) {
			return;
		}
		
		highlight(sm.getStart(), sm.getEnd());
	}
	
	@Override
	protected String detectSyntax(HttpMessage httpMessage) {
		String syntax = null;
		if (httpMessage != null && httpMessage.getResponseHeader() != null) {
			String contentType = httpMessage.getResponseHeader().getHeader(HttpHeader.CONTENT_TYPE);
			if(contentType != null && !contentType.isEmpty()) {
				contentType = contentType.toLowerCase();
				final int pos = contentType.indexOf(';');
				if (pos != -1) {
					contentType = contentType.substring(0, pos).trim();
				}
				if (contentType.indexOf("javascript") != -1 ||
					contentType.indexOf("json") != -1) {
					syntax = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
				} else if (contentType.indexOf("xhtml") != -1) {
					syntax = SyntaxConstants.SYNTAX_STYLE_HTML;
				} else if (contentType.indexOf("xml") != -1) {
					syntax = SyntaxConstants.SYNTAX_STYLE_XML;
				} else {
					syntax = contentType;
				}
			}
		}
		return syntax;
	}
	
	@Override
	protected synchronized CustomTokenMakerFactory getTokenMakerFactory() {
		if (tokenMakerFactory == null) {
			tokenMakerFactory = new ResponseBodyTokenMakerFactory();
		}
		return tokenMakerFactory;
	}
	
	private static class ResponseBodyTokenMakerFactory extends CustomTokenMakerFactory {
		
		public ResponseBodyTokenMakerFactory() {
			String pkg = "org.fife.ui.rsyntaxtextarea.modes.";
			
			putMapping(SYNTAX_STYLE_CSS, pkg + "CSSTokenMaker");
			putMapping(SYNTAX_STYLE_HTML, pkg + "HTMLTokenMaker");
			putMapping(SYNTAX_STYLE_JAVASCRIPT, pkg + "JavaScriptTokenMaker");
			putMapping(SYNTAX_STYLE_XML, pkg + "XMLTokenMaker");
		}
	}
}