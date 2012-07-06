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
package org.zaproxy.zap.extension.websocket.brk;

import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.brk.ExtensionBreak;
import org.zaproxy.zap.extension.websocket.WebSocketException;
import org.zaproxy.zap.extension.websocket.WebSocketMessage;
import org.zaproxy.zap.extension.websocket.WebSocketObserver;
import org.zaproxy.zap.extension.websocket.WebSocketMessage.Direction;
import org.zaproxy.zap.extension.websocket.ui.WebSocketMessageDAO;
import org.zaproxy.zap.extension.websocket.ui.WebSocketPanel;

public class WebSocketProxyListenerBreak implements WebSocketObserver {

	private static final Logger logger = Logger.getLogger(WebSocketProxyListenerBreak.class);

	private ExtensionBreak extension = null;
	
	public static final int WEBSOCKET_OBSERVING_ORDER = WebSocketPanel.WEBSOCKET_OBSERVING_ORDER - 5;

	public WebSocketProxyListenerBreak(ExtensionBreak extension) {
	    this.extension = extension;
	}
	
    @Override
    public int getObservingOrder() {
		// should be the last one to be notified before saving/showing the
		// message
        return WEBSOCKET_OBSERVING_ORDER;
    }


    @Override
    public boolean onMessageFrame(int channelId, WebSocketMessage message) {
        WebSocketMessageDAO dao = message.getDAO();
    	boolean isRequest = (message.getDirection().equals(Direction.OUTGOING));
    	
        if (!message.isFinished()) {
        	
        	if (extension.isBreakpointSet(dao, isRequest)) {
            	// prevent forwarding unfinished message when there is a breakpoint
            	// wait until all frames are received, before processing
            	// (showing/saving/etc.)
        		return false;
        	} else {
        		// gain performance by allowing each frame to be forwarded
        		// immediately, as this frame is not changed
        		return true;
        	}
        }

        if (dao.direction == WebSocketMessage.Direction.OUTGOING) {
            if (extension.messageReceivedFromClient(dao)) {
                // As the DAO that is shown and modified in the
                // Request/Response panels we must set the content to message
                // here.
                try {
					message.setReadablePayload(dao.payload);
				} catch (WebSocketException e) {
					logger.error(e);
				}
                return true;
            }
        } else if (dao.direction == WebSocketMessage.Direction.INCOMING) {
            if (extension.messageReceivedFromServer(dao)) {
            	try {
	                message.setReadablePayload(dao.payload);
				} catch (WebSocketException e) {
					logger.error(e);
				}
                return true;
            }
        }

        return false;
    }
}
