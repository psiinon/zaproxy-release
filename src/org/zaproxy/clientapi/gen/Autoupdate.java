/* Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright the ZAP development team
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
public class Autoupdate {

	private ClientApi api = null;

	public Autoupdate(ClientApi api) {
		this.api = api;
	}

	public ApiResponse latestVersionNumber() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "latestVersionNumber", map);
	}

	public ApiResponse isLatestVersion() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "isLatestVersion", map);
	}

	public ApiResponse optionReportBetaAddons() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionReportBetaAddons", map);
	}

	public ApiResponse optionCheckAddonUpdates() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionCheckAddonUpdates", map);
	}

	public ApiResponse optionCheckOnStart() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionCheckOnStart", map);
	}

	public ApiResponse optionReportReleaseAddons() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionReportReleaseAddons", map);
	}

	public ApiResponse optionInstallAddonUpdates() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionInstallAddonUpdates", map);
	}

	public ApiResponse optionReportAlphaAddons() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionReportAlphaAddons", map);
	}

	public ApiResponse optionDownloadNewRelease() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionDownloadNewRelease", map);
	}

	public ApiResponse optionInstallScannerRules() throws ClientApiException {
		Map<String, String> map = null;
		return api.callApi("autoupdate", "view", "optionInstallScannerRules", map);
	}

	public ApiResponse downloadLatestRelease(String apikey) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		return api.callApi("autoupdate", "action", "downloadLatestRelease", map);
	}

	public ApiResponse setOptionCheckAddonUpdates(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionCheckAddonUpdates", map);
	}

	public ApiResponse setOptionDownloadNewRelease(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionDownloadNewRelease", map);
	}

	public ApiResponse setOptionCheckOnStart(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionCheckOnStart", map);
	}

	public ApiResponse setOptionReportReleaseAddons(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionReportReleaseAddons", map);
	}

	public ApiResponse setOptionInstallAddonUpdates(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionInstallAddonUpdates", map);
	}

	public ApiResponse setOptionReportAlphaAddons(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionReportAlphaAddons", map);
	}

	public ApiResponse setOptionReportBetaAddons(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionReportBetaAddons", map);
	}

	public ApiResponse setOptionInstallScannerRules(String apikey, boolean bool) throws ClientApiException {
		Map<String, String> map = null;
		map = new HashMap<String, String>();
		map.put("apikey", apikey);
		map.put("Boolean", Boolean.toString(bool));
		return api.callApi("autoupdate", "action", "setOptionInstallScannerRules", map);
	}

}
