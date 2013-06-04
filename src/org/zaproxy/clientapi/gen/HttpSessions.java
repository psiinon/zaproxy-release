/* Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2012 ZAP development team
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


package org.zaproxy.clientapi.gen;

import java.util.HashMap;
import java.util.Map;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;


/**
 * This file was automatically generated.
 */
public class HttpSessions {

	private ClientApi api = null;

	public HttpSessions(ClientApi api) {
		this.api = api;
	}

	public ApiResponse sessions(String site, String session) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("session", session);
		return api.callApi("httpSessions", "view", "sessions", map);
	}

	public ApiResponse activeSession(String site) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		return api.callApi("httpSessions", "view", "activeSession", map);
	}

	public ApiResponse sessionTokens(String site) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		return api.callApi("httpSessions", "view", "sessionTokens", map);
	}

	public ApiResponse optionDefaultTokens() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("httpSessions", "view", "optionDefaultTokens", map);
	}

	public ApiResponse optionDefaultTokensEnabled() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("httpSessions", "view", "optionDefaultTokensEnabled", map);
	}

	public ApiResponse optionEnabledProxyOnly() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("httpSessions", "view", "optionEnabledProxyOnly", map);
	}

	public ApiResponse optionConfirmRemoveDefaultToken() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("httpSessions", "view", "optionConfirmRemoveDefaultToken", map);
	}

	public ApiResponse createEmptySession(String site) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		return api.callApi("httpSessions", "action", "createEmptySession", map);
	}

	public ApiResponse removeSession(String site, String session) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("session", session);
		return api.callApi("httpSessions", "action", "removeSession", map);
	}

	public ApiResponse setActiveSession(String site, String session) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("session", session);
		return api.callApi("httpSessions", "action", "setActiveSession", map);
	}

	public ApiResponse unsetActiveSession(String site) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		return api.callApi("httpSessions", "action", "unsetActiveSession", map);
	}

	public ApiResponse addSessionToken(String site, String sessiontoken) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("sessionToken", sessiontoken);
		return api.callApi("httpSessions", "action", "addSessionToken", map);
	}

	public ApiResponse removeSessionToken(String site, String sessiontoken) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("sessionToken", sessiontoken);
		return api.callApi("httpSessions", "action", "removeSessionToken", map);
	}

	public ApiResponse setSessionTokenValue(String site, String session, String sessiontoken, String tokenvalue) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("session", session);
		map.put("sessionToken", sessiontoken);
		map.put("tokenValue", tokenvalue);
		return api.callApi("httpSessions", "action", "setSessionTokenValue", map);
	}

	public ApiResponse renameSession(String site, String oldsessionname, String newsessionname) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("site", site);
		map.put("oldSessionName", oldsessionname);
		map.put("newSessionName", newsessionname);
		return api.callApi("httpSessions", "action", "renameSession", map);
	}

	public ApiResponse setOptionEnabledProxyOnly(boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("httpSessions", "action", "setOptionEnabledProxyOnly", map);
	}

	public ApiResponse setOptionConfirmRemoveDefaultToken(boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("httpSessions", "action", "setOptionConfirmRemoveDefaultToken", map);
	}

}
