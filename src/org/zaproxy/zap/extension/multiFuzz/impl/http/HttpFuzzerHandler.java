/*
+

 * 
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
package org.zaproxy.zap.extension.multiFuzz.impl.http;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.anticsrf.AntiCsrfToken;
import org.zaproxy.zap.extension.anticsrf.ExtensionAntiCSRF;
import org.zaproxy.zap.extension.multiFuzz.ExtensionFuzz;
import org.zaproxy.zap.extension.multiFuzz.FuzzableComponent;
import org.zaproxy.zap.extension.multiFuzz.FuzzerContentPanel;
import org.zaproxy.zap.extension.multiFuzz.FuzzerHandler;
import org.zaproxy.zap.extension.multiFuzz.FuzzerListener;
import org.zaproxy.zap.extension.multiFuzz.FuzzerParam;
import org.zaproxy.zap.extension.multiFuzz.FuzzerThread;
import org.zaproxy.zap.extension.search.SearchResult;

public class HttpFuzzerHandler implements FuzzerHandler<HttpMessage, HttpFuzzDialog> {

	private ExtensionFuzz ext;
	private HttpFuzzDialog dia;
	private HttpFuzzerContentPanel fuzzerPanel;
	private boolean showTokenRequests;
	private ArrayList<HttpFuzzGap> payloads;
	private FuzzerParam fuzzerParam;
	private FuzzerThread<HttpPayload,HttpMessage, HttpFuzzLocation,HttpFuzzResult, HttpFuzzGap, HttpFuzzProcess> fuzzerThread;


	public HttpFuzzerHandler(ExtensionFuzz parent) {
		this.ext = parent;
		showTokenRequests = false;
	}

	void setShowTokenRequests(boolean showTokenRequests) {
		this.showTokenRequests = showTokenRequests;
	}

	@Override
	public void showFuzzDialog(FuzzableComponent<HttpMessage> comp) {
		final JTextArea compbase = (JTextArea) comp;
		int s = compbase.getSelectionStart();
		int e = compbase.getSelectionEnd();
		String header = comp.getFuzzableMessage().getRequestHeader().toString();
		int hl = 0;
		int pos = 0;
		while (((pos = header.indexOf("\r\n", pos)) != -1) && (pos <= s + hl)) {
			pos += 2;
			++hl;
		}
		if(!comp.getFuzzableMessage().getRequestHeader().toString().substring(s+hl, e+hl).equals(compbase.getText().substring(s, e))){
			s += comp.getFuzzableMessage().getRequestHeader().toString().length();
			e += comp.getFuzzableMessage().getRequestHeader().toString().length();
		}
		dia = new HttpFuzzDialog(getExtension(), comp.getFuzzableMessage(), new HttpFuzzLocation(s + hl, e + hl));
		dia.addFuzzerListener(new FuzzerListener< HttpFuzzDialog , ArrayList<HttpFuzzGap>>() {
			@Override
			public void notifyFuzzerStarted(HttpFuzzDialog process) {}
			@Override
			public void notifyFuzzerPaused(HttpFuzzDialog process) {}
			@Override
			public void notifyFuzzerComplete(ArrayList<HttpFuzzGap> result) {
				payloads = result;
				dia.setVisible(false);
				startFuzzers();
			}
		});
		dia.setVisible(true);

	}

	@Override
	public FuzzerContentPanel getFuzzerContentPanel() {
		return getContentPanel();
	}

	private FuzzerContentPanel getContentPanel() {
		if (fuzzerPanel == null) {
			fuzzerPanel = new HttpFuzzerContentPanel();
			fuzzerPanel.setDisplayPanel(View.getSingleton().getRequestPanel(), View.getSingleton().getResponsePanel());
		}
		fuzzerPanel.setShowTokenRequests(showTokenRequests);
		return fuzzerPanel;
	}

	@Override
	public List<SearchResult> searchResults(Pattern pattern, boolean inverse) {
		return fuzzerPanel.searchResults(pattern, inverse);
	}

	@Override
	public void startFuzzers() {
		fuzzerThread = new FuzzerThread<HttpPayload,HttpMessage, HttpFuzzLocation,HttpFuzzResult, HttpFuzzGap, HttpFuzzProcess>(getFuzzerParam());
		HttpFuzzProcessFactory factory = (HttpFuzzProcessFactory) dia.getFuzzProcessFactory();
		ExtensionAntiCSRF extAntiCSRF = (ExtensionAntiCSRF) Control.getSingleton().getExtensionLoader().getExtension(ExtensionAntiCSRF.NAME);
		List<AntiCsrfToken> tokens = null;
		if (factory.getToken() != null) {
			tokens = extAntiCSRF.getTokens(dia.getMessage());
			if (tokens != null && tokens.size() > 0) {
				fuzzerThread.addPostprocessor(new AntiCSRFProcessor(factory.getSender(),extAntiCSRF, factory.getToken()));
			}
		}

		fuzzerThread.setTarget(payloads, factory);
		fuzzerThread.addHandlerListener(new FuzzerListener<Integer, Boolean>() {

			@Override
			public void notifyFuzzerStarted(Integer process) {
			}

			@Override
			public void notifyFuzzerPaused(Integer process) {
			}

			@Override
			public void notifyFuzzerComplete(Boolean result) {
				ext.getFuzzerPanel().scanFinished();
			}

		});
		fuzzerThread.addFuzzerListener(new FuzzerListener<Integer, HttpFuzzResult>() {

			@Override
			public void notifyFuzzerStarted(Integer process) {
				ext.getFuzzerPanel().setContentPanel(getFuzzerContentPanel());
				ext.getFuzzerPanel().newScan(process);
			}

			@Override
			public void notifyFuzzerPaused(Integer process) {
			}

			@Override
			public void notifyFuzzerComplete(HttpFuzzResult result) {
				ext.getFuzzerPanel().inComingResult(result);
			}
		});
		fuzzerThread.start();
	}

	private FuzzerParam getFuzzerParam() {
		if (fuzzerParam == null) {
			fuzzerParam = new FuzzerParam();
		}
		return fuzzerParam;
	}

	@Override
	public void stopFuzzers() {
		fuzzerThread.stop();
		ext.getFuzzerPanel().scanFinished();
	}

	@Override
	public void pauseFuzzers() {
		fuzzerThread.pause();
	}

	@Override
	public void resumeFuzzers() {
		fuzzerThread.resume();		
	}

	@Override
	public ExtensionFuzz getExtension() {
		return ext;
	}

}
