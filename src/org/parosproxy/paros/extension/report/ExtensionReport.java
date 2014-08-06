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
// ZAP: 2011/11/20 Set order
// ZAP: 2012/03/17 Issue 282 Added getAuthor()
// ZAP: 2012/04/25 Added @Override annotation to all appropriate methods and
// removed unnecessary cast.
// ZAP: 2013/01/25 Removed the "(non-Javadoc)" comments.
// ZAP: 2013/03/03 Issue 546: Remove all template Javadoc comments
// ZAP: 2013/12/03 Issue 934: Handle files on the command line via extension
// ZAP: 2014/01/28 Issue 207: Support keyboard shortcuts 

package org.parosproxy.paros.extension.report;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.zaproxy.zap.view.ZapMenuItem;
import org.parosproxy.paros.extension.report.ReportLastScan;

public class ExtensionReport extends ExtensionAdaptor implements CommandLineListener {

    private static final int ARG_LAST_SCAN_REPORT_IDX = 0;

    // ZAP: Changed to support XML reports as well
	private ZapMenuItem menuItemHtmlReport = null;
	private ZapMenuItem menuItemXmlReport = null;
	private CommandLineArgument[] arguments = new CommandLineArgument[1];
	// ZAP Added logger
	private Logger logger = Logger.getLogger(ExtensionReport.class);

    /**
     * 
     */
    public ExtensionReport() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionReport(String name) {
        super(name);
        this.setOrder(14);
    }

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setName("ExtensionReport");
			
	}
	@Override
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        //extensionHook.getHookMenu().addNewMenu(getMenuReport());
	        extensionHook.getHookMenu().addReportMenuItem(getMenuItemHtmlReport());
	        extensionHook.getHookMenu().addReportMenuItem(getMenuItemXmlReport());

	    }
        extensionHook.addCommandLine(getCommandLineArguments());

	}

	private ZapMenuItem getMenuItemHtmlReport() {
		if (menuItemHtmlReport == null) {
			menuItemHtmlReport = new ZapMenuItem("menu.report.html.generate");
			menuItemHtmlReport.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    ReportLastScan report = new ReportLastScan();
				    report.generateReport(getView(), getModel(), ReportLastScan.reportTypes.HTML);
	                
				}
			});

		}
		return menuItemHtmlReport;
	}
	
	private ZapMenuItem getMenuItemXmlReport() {
		if (menuItemXmlReport == null) {
			menuItemXmlReport = new ZapMenuItem("menu.report.xml.generate");
			menuItemXmlReport.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    ReportLastScan report = new ReportLastScan();
				    report.generateReport(getView(), getModel(), ReportLastScan.reportTypes.XML);
	                
				}
			});

		}
		return menuItemXmlReport;
	}
	
    @Override
    public void execute(CommandLineArgument[] args) {

        if (arguments[ARG_LAST_SCAN_REPORT_IDX].isEnabled()) {
		    CommandLineArgument arg = arguments[ARG_LAST_SCAN_REPORT_IDX];
            ReportLastScan report = new ReportLastScan();
            // ZAP: Removed unnecessary cast.
            String fileName = arg.getArguments().get(0);
            try {
                report.generate(fileName, getModel(), "xml/report.html.xsl");
                System.out.println("Last Scan Report generated at " + fileName);
            } catch (Exception e) {
            	// ZAP: Log the exception
            	logger.error(e.getMessage(), e);
            }
        } else {
            return;
        }

    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_LAST_SCAN_REPORT_IDX] = new CommandLineArgument("-last_scan_report", 1, null, "", "-last_scan_report [file_path]: Generate 'Last Scan Report' into the file_path provided.");
        return arguments;
    }
	
	

	@Override
	public String getAuthor() {
		return Constant.PAROS_TEAM;
	}

	@Override
	public boolean handleFile(File file) {
		// Cant handle any files
		return false;
	}

	@Override
	public List<String> getHandledExtensions() {
		// Cant handle any extensions
		return null;
	}
}
