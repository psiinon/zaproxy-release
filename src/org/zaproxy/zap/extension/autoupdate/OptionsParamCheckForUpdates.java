/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2010 psiinon@gmail.com
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
package org.zaproxy.zap.extension.autoupdate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.common.AbstractParam;
import org.zaproxy.zap.extension.api.ZapApiIgnore;

public class OptionsParamCheckForUpdates extends AbstractParam {

	public static final String CHECK_ON_START = "start.checkForUpdates";
	public static final String DAY_LAST_CHECKED = "start.dayLastChecked";
	public static final String DOWNLOAD_NEW_RELEASE = "start.downloadNewRelease";
	public static final String CHECK_ADDON_UPDATES = "start.checkAddonUpdates";
	public static final String INSTALL_ADDON_UPDATES = "start.installAddonUpdates";
	public static final String INSTALL_SCANNER_RULES = "start.installScannerRules";
	public static final String REPORT_RELEASE_ADDON = "start.reportReleaseAddons";
	public static final String REPORT_BETA_ADDON = "start.reportBetaAddons";
	public static final String REPORT_ALPHA_ADDON = "start.reportAlphaAddons";
	
	private boolean checkOnStart;
	private boolean downloadNewRelease = false;
	private boolean checkAddonUpdates = false;
	private boolean installAddonUpdates = false;
	private boolean installScannerRules = false;
	private boolean reportReleaseAddons = false;
	private boolean reportBetaAddons = false;
	private boolean reportAlphaAddons = false;
	
	// Day last checked is used to ensure if the user has agreed then we only check the first time ZAP is run every day
	private String dayLastChecked = null; 
	private boolean unset = true;
    private static Logger log = Logger.getLogger(OptionsParamCheckForUpdates.class);
    
    public OptionsParamCheckForUpdates() {
    }

    @Override
    protected void parse() {
        updateOldOptions();
        
	    checkOnStart = getConfig().getBoolean(CHECK_ON_START, false);
	    dayLastChecked = getConfig().getString(DAY_LAST_CHECKED, "");
	    // There was a bug in 1.2.0 where it defaulted silently to dont check
	    // We now use the lack of a dayLastChecked value to indicate we should reprompt the user.
		unset = dayLastChecked.length() == 0;
		
		downloadNewRelease = getConfig().getBoolean(DOWNLOAD_NEW_RELEASE, false);
		checkAddonUpdates = getConfig().getBoolean(CHECK_ADDON_UPDATES, false);
		installAddonUpdates = getConfig().getBoolean(INSTALL_ADDON_UPDATES, false);
		installScannerRules = getConfig().getBoolean(INSTALL_SCANNER_RULES, false);
		reportReleaseAddons = getConfig().getBoolean(REPORT_RELEASE_ADDON, false);
		reportBetaAddons = getConfig().getBoolean(REPORT_BETA_ADDON, false);
		reportAlphaAddons = getConfig().getBoolean(REPORT_ALPHA_ADDON, false);
    }

	private void updateOldOptions() {
		try {
			int oldValue = getConfig().getInt(CHECK_ON_START, 0);
			getConfig().setProperty(CHECK_ON_START, Boolean.valueOf(oldValue != 0));
		} catch(ConversionException ignore) {
			// Option already using boolean type.
		}
	}

	@ZapApiIgnore
	public boolean isCheckOnStartUnset() {
		return unset;
	}
	
	/**
	 * @param checkOnStart 0 to disable check for updates on startup, any other number to enable.
	 * @deprecated (2.3.0) Replaced by {@link #setCheckOnStart(boolean)}. It will be removed in a future release.
	 */
	@Deprecated
	@ZapApiIgnore
	public void setCheckOnStart(int checkOnStart) {
	    setCheckOnStart(checkOnStart != 0);
	}

	/**
	 * Sets whether or not the "check for updates on start up" is enabled.
	 * 
	 * @param checkOnStart {@code true} if the "check for updates on start up" should be enabled, {@code false} otherwise.
	 */
	public void setCheckOnStart(boolean checkOnStart) {
		this.checkOnStart = checkOnStart;
		getConfig().setProperty(CHECK_ON_START, Boolean.valueOf(checkOnStart));
		if (dayLastChecked.length() == 0) {
			dayLastChecked = "Never";
			getConfig().setProperty(DAY_LAST_CHECKED, dayLastChecked);
		}
	}
	
	@ZapApiIgnore
	public boolean isCheckOnStart() {
		if (!checkOnStart) {
			log.debug("isCheckForStart - false");
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		if (today.equals(dayLastChecked)) {
			log.debug("isCheckForStart - already checked today");
			return false;
		}
		getConfig().setProperty(DAY_LAST_CHECKED, today);
		try {
			getConfig().save();
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		
		return true;
	}

	public boolean isDownloadNewRelease() {
		return downloadNewRelease;
	}

	public void setDownloadNewRelease(boolean downloadNewRelease) {
		this.downloadNewRelease = downloadNewRelease;
		getConfig().setProperty(DOWNLOAD_NEW_RELEASE, this.downloadNewRelease);
	}

	public boolean isCheckAddonUpdates() {
		return checkAddonUpdates;
	}

	public void setCheckAddonUpdates(boolean checkAddonUpdates) {
		this.checkAddonUpdates = checkAddonUpdates;
		getConfig().setProperty(CHECK_ADDON_UPDATES, checkAddonUpdates);
	}

	public boolean isInstallAddonUpdates() {
		return installAddonUpdates;
	}

	public void setInstallAddonUpdates(boolean installAddonUpdates) {
		this.installAddonUpdates = installAddonUpdates;
		getConfig().setProperty(INSTALL_ADDON_UPDATES, installAddonUpdates);
	}

	public boolean isInstallScannerRules() {
		return installScannerRules;
	}

	public void setInstallScannerRules(boolean installScannerRules) {
		this.installScannerRules = installScannerRules;
		getConfig().setProperty(INSTALL_SCANNER_RULES, installScannerRules);
	}

	public boolean isReportReleaseAddons() {
		return reportReleaseAddons;
	}

	public void setReportReleaseAddons(boolean reportReleaseAddons) {
		this.reportReleaseAddons = reportReleaseAddons;
		getConfig().setProperty(REPORT_RELEASE_ADDON, reportReleaseAddons);
	}

	public boolean isReportBetaAddons() {
		return reportBetaAddons;
	}

	public void setReportBetaAddons(boolean reportBetaAddons) {
		this.reportBetaAddons = reportBetaAddons;
		getConfig().setProperty(REPORT_BETA_ADDON, reportBetaAddons);
	}

	public boolean isReportAlphaAddons() {
		return reportAlphaAddons;
	}

	public void setReportAlphaAddons(boolean reportAlphaAddons) {
		this.reportAlphaAddons = reportAlphaAddons;
		getConfig().setProperty(REPORT_ALPHA_ADDON, reportAlphaAddons);
	}

	
}
