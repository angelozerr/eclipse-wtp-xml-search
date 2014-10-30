/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.xml.search.editor.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for XML Search Editor.
 * 
 */
public class Messages extends NLS {

	// ReferencesInContainerHandler_
	public static String ReferencesInContainerHandler_operationUnavailable_title;
	public static String ReferencesInContainerHandler_operationUnavailable_textEditorUnavailable;
	public static String ReferencesInContainerHandler_operationUnavailable_fileUnavailable;
	public static String ReferencesInContainerHandler_operationUnavailable_domNodeUnavailable;
	public static String ReferencesInContainerHandler_operationUnavailable_noReferencesDefined;

	// Handler search error
	public static String ExceptionDialog_seeErrorLogMessage;

	public static String Validation_ClassNotFounded;
	public static String Validation_ClassHierarchyIncorrect;
	public static String Validation_FileNotFounded;
	public static String Validation_ElementNotFounded;
	public static String Validation_ElementNonUnique;
	public static String Validation_ElementInvalid;

	// JDT search particpant
	public static String ClassSearchParticipant_taskMessage;

	public static String Search_Error_search_notsuccessful_title;
	public static String Search_Error_search_notsuccessful_message;

	// XML references preferences
	public static String XMLSourcePreferencePage_0;
	public static String Sample_XML_doc;
	public static String Referenced_Attribute_Values_UI_;
	public static String Referenced_Content_UI_;
	public static String XMLReferencesSyntaxColoringPage_0;

	// XML references index
	public static String XMLReferencesIndexManager_0;
	public static String XMLReferencesIndexManager_2;

	private static final String BUNDLE_NAME = Messages.class.getName();

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
