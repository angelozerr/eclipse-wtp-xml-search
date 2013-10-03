/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for XML Search UI.
 * 
 */
public class Messages extends NLS {
	// Search query
	public static String XMLSearchQuery_label;
	public static String XMLSearchQuery_status_ok_message;

	// Search error
	public static String Search_Error_search_title;
	public static String Search_Error_search_message;
	public static String Search_Error_search_notsuccessful_title;
	public static String Search_Error_search_notsuccessful_message;

	// Search open file
	public static String XMLSearchPage_open_file_dialog_title;
	public static String XMLSearchPage_open_file_failed;
	public static String XMLSearchPage_error_marker;

	public static String XMLSearchQuery_xpathPattern;

	// XML Search page
	public static String SearchPage_xpath_text;
	public static String SearchPage_xpath_hint;
	public static String SearchPage_xpath_errorRequired;
	public static String SearchPage_XPathProcessor_text;
	public static String SearchPage_xpathProcessor_errorRequired;
	public static String SearchPage_browse;
	public static String SearchPage_fileNamePatterns_text;
	public static String SearchPage_fileNamePatterns_hint;

	public static String XMLSearchPage_replace_searchproblems_title;

	public static String XMLSearchPage_replace_searchproblems_message;

	// FileTypeEditor
	public static String FileTypeEditor_typeDelimiter;

	// Preferences page
	public static String XMLSearchPreferencePage_0;

	public static String InstalledProcessorsBlock_0;
	public static String InstalledProcessorsBlock_1;
	public static String InstalledProcessorsBlock_2;
	public static String InstalledProcessorsBlock_5;
	public static String InstalledProcessorsBlock_6;
	public static String InstalledProcessorsBlock_7;

	public static String XPathProcessorPreferencePage_0;
	public static String XPathProcessorPreferencePage_1;
	public static String XPathProcessorPreferencePage_2;
	
	public static String SearchParticipant_error_noID;
	public static String SearchParticipant_error_noClass;
	public static String SearchParticipant_error_classCast;
	
	protected static String XMLSearchQuery_error_participant_estimate;
	protected static String XMLSearchQuery_error_participant_search;

	static {
		NLS.initializeMessages(XMLSearchUIPlugin.PLUGIN_ID
				+ ".internal.Messages", Messages.class);
	}

}
