/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.editor.internal.reporter;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;
import org.eclipse.wst.xml.search.core.reporter.MutliXMLSearchReporter;
import org.eclipse.wst.xml.search.core.reporter.SysOutSearchReporter;

public class XMLSearchReporterManager implements IXMLSearchReporter {

	private static final XMLSearchReporterManager INSTANCE = new XMLSearchReporterManager();

	public static XMLSearchReporterManager getDefault() {
		return INSTANCE;
	}

	private MutliXMLSearchReporter multiReporter = new MutliXMLSearchReporter();

	public void beginSearch(int searchId,
			Map<IResource, Collection<String>> queries) {
		multiReporter.beginSearch(searchId, queries);
	}

	public void endSearch(int searchId, long elapsedTime) {
		multiReporter.endSearch(searchId, elapsedTime);
	}

	public boolean isEnabled() {
		return multiReporter.isEnabled();
	}

	public void initialize() {
		boolean debug = Boolean
				.valueOf(
						Platform
								.getDebugOption("org.eclipse.wst.xml.search.editor/debug/reporter"))
				.booleanValue();
		if (debug) {
			multiReporter.add(SysOutSearchReporter.INSTANCE);
		}
	}

	public void destroy() {

	}

}
