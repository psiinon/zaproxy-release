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

import java.util.ArrayList;
import java.util.List;

import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.network.ConnectionParam;
import org.zaproxy.zap.extension.anticsrf.AntiCsrfAPI;
import org.zaproxy.zap.extension.ascan.ActiveScanAPI;
import org.zaproxy.zap.extension.auth.AuthAPI;
import org.zaproxy.zap.extension.autoupdate.AutoUpdateAPI;
import org.zaproxy.zap.extension.autoupdate.OptionsParamCheckForUpdates;
import org.zaproxy.zap.extension.params.ParamsAPI;
import org.zaproxy.zap.extension.pscan.PassiveScanAPI;
import org.zaproxy.zap.extension.search.SearchAPI;
import org.zaproxy.zap.extension.spider.SpiderAPI;
import org.zaproxy.zap.spider.SpiderParam;

/**
 * Utility class for the API generators
 * @author simon
 *
 */
public class ApiGeneratorUtils {

	/**
	 * Return all of the available ApiImplementors.
	 * If you implement a new ApiImplementor then you must add it to this class.
	 * @return all of the available ApiImplementors.
	 */
	public static List<ApiImplementor> getAllImplementors() {
		List<ApiImplementor> imps = new ArrayList<ApiImplementor>();
		
		ApiImplementor api;

		imps.add(new AntiCsrfAPI(null));
		imps.add(new PassiveScanAPI(null));
		imps.add(new SearchAPI(null));

		api = new AutoUpdateAPI(null);
		api.addApiOptions(new OptionsParamCheckForUpdates());
		imps.add(api);

		api = new SpiderAPI(null);
		api.addApiOptions(new SpiderParam());
		imps.add(api);

		api = new CoreAPI();
        api.addApiOptions(new ConnectionParam());
		imps.add(api);

		imps.add(new ParamsAPI(null));
		
		api = new ActiveScanAPI(null);
		api.addApiOptions(new ScannerParam());
		imps.add(api);
		
		imps.add(new AuthAPI(null));

		return imps;
	}
}
