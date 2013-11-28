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
package org.zaproxy.zap.extension.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.zap.utils.XMLStringUtil;

public class ApiResponseSet extends ApiResponse {

	private Map<String, String> values = null;

	public ApiResponseSet(String name, Map<String, String> values) {
		super(name);
		this.values = values;
	}

	public ApiResponseSet(Node node) throws ClientApiException {
		super(node.getNodeName());
		Node child = node.getFirstChild();
		this.values = new HashMap<>();
		while (child != null) {
			ApiResponseElement elem = (ApiResponseElement) ApiResponseFactory
					.getResponse(child);
			values.put(elem.getName(), elem.getValue());
			child = child.getNextSibling();
		}
	}

	@Override
	public JSON toJSON() {
		if (values == null) {
			return null;
		}
		JSONObject jo = new JSONObject();
		for (Entry<String, String> val : values.entrySet()) {
			jo.put(val.getKey(), val.getValue());
		}
		return jo;
	}

	@Override
	public void toXML(Document doc, Element parent) {
		parent.setAttribute("type", "set");
		for (Entry<String, String> val : values.entrySet()) {
			Element el = doc.createElement(val.getKey());
			Text text = doc.createTextNode(XMLStringUtil.escapeControlChrs(val
					.getValue()));
			el.appendChild(text);
			parent.appendChild(el);
		}
	}

	@Override
	public void toHTML(StringBuilder sb) {
		sb.append("<h2>" + this.getName() + "</h2>\n");
		sb.append("<table border=\"1\">\n");
		for (Entry<String, String> val : values.entrySet()) {
			sb.append("<tr><td>\n");
			sb.append(val.getKey());
			sb.append("</td><td>\n");
			sb.append(StringEscapeUtils.escapeHtml(val.getValue()));
			sb.append("</td></tr>\n");
		}
		sb.append("</table>\n");
	}

	@Override
	public String toString(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append("\t");
		}
		sb.append("ApiResponseSet ");
		sb.append(this.getName());
		sb.append(" : [\n");
		for (Entry<String, String> val : values.entrySet()) {
			for (int i = 0; i < indent + 1; i++) {
				sb.append("\t");
			}
			sb.append(val.getKey());
			sb.append(" = ");
			sb.append(val.getValue());
			sb.append("\n");
		}
		for (int i = 0; i < indent; i++) {
			sb.append("\t");
		}
		sb.append("]\n");
		return sb.toString();
	}

	/*
	 * Package visible method for simplified unit testing
	 */
	Map<String, String> getValues() {
		return values;
	}

}
