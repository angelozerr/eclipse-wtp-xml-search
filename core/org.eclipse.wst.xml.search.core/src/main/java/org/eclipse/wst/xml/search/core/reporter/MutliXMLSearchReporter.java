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
package org.eclipse.wst.xml.search.core.reporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResource;

/**
 * Multi search reporter.
 * 
 */
public class MutliXMLSearchReporter extends ArrayList<IXMLSearchReporter>
		implements IXMLSearchReporter {

	private static final long serialVersionUID = -4324750000148797136L;

	public void beginSearch(int searchId,
			Map<IResource, Collection<String>> queries) {
		IXMLSearchReporter reporter = null;
		for (int i = 0; i < super.size(); i++) {
			reporter = super.get(i);
			if (reporter.isEnabled()) {
				reporter.beginSearch(searchId, queries);
			}
		}
	}

	public void endSearch(int searchId, long elapsedTime) {
		IXMLSearchReporter reporter = null;
		for (int i = 0; i < super.size(); i++) {
			reporter = super.get(i);
			if (reporter.isEnabled()) {
				reporter.endSearch(searchId, elapsedTime);
			}
		}
	}

	public boolean isEnabled() {
		return !super.isEmpty();
	}

}
