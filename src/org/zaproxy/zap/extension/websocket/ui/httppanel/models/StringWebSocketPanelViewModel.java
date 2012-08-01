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
package org.zaproxy.zap.extension.websocket.ui.httppanel.models;

public class StringWebSocketPanelViewModel extends AbstractWebSocketStringPanelViewModel {
	
    @Override
    public String getData() {
        if (webSocketMessage == null || webSocketMessage.payload == null)  {
            return "";
        }
        
        if (webSocketMessage.payload instanceof String) {
        	return (String) webSocketMessage.payload;
        } else if (webSocketMessage.payload instanceof byte[]) {
        	return "<binary data>";
        }
        
        return "";
    }

    @Override
    public void setData(String data) {
    	if (webSocketMessage.payload instanceof String) {
    		webSocketMessage.payload = data;
        } else if (webSocketMessage.payload instanceof byte[]) {
        	webSocketMessage.payload = data.getBytes();
        }
    }

}
