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
package org.eclipse.wst.xml.search.core.reporter;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IResource;

/**
 * Search reporter which trace to System.out.
 * 
 */
public class SysOutSearchReporter implements IXMLSearchReporter {

	public static final IXMLSearchReporter INSTANCE = new SysOutSearchReporter();

	public void beginSearch(int searchId,
			Map<IResource, Collection<String>> queries) {
		System.out.println("Start search [" + searchId + "]");
		Set<Entry<IResource, Collection<String>>> entries = queries.entrySet();
		for (Entry<IResource, Collection<String>> entry : entries) {
			System.out.print("\tresources=");
			System.out.println(entry.getKey().getFullPath());
			System.out.print("\t\tqueries=");
			Collection<String> q = entry.getValue();
			boolean first = true;
			for (String query : q) {
				if (!first) {
					System.out.print(", ");
				}
				System.out.print(query);
				first = false;
			}
			System.out.println();
		}
	}

	public void endSearch(int searchId, long elapsedTime) {
		System.out.println("End search [" + searchId + "] with " + elapsedTime
				+ "(ms).");
	}

	public boolean isEnabled() {
		return true;
	}

}
