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
package org.eclipse.wst.xml.search.core.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for XML Search Core.
 * 
 */
public class Messages extends NLS {

	public static String searchEngineDOMModelError;
	public static String searchEngineDOMDocumentVisitedError;
	public static String searchEngineContainerResourcesError;
	public static String XMLSearchEngine_statusMessage;

	public static String XMLSearchEngine2_scanning;
	public static String XMLSearchEngine2_progress_updating_job;
	public static String XMLSearchEngine2_canceled;
	public static String XMLSearchEngine2_textsearch_task_label;

	static {
		NLS.initializeMessages(XMLSearchCorePlugin.PLUGIN_ID
				+ ".internal.Messages", Messages.class);
	}

}
