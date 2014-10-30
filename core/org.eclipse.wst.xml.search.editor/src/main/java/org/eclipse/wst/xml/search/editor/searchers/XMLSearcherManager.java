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
package org.eclipse.wst.xml.search.editor.searchers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;

public class XMLSearcherManager extends AbstractRegistryManager {

	private static final String SEARCHER_ELT = "searcher";
	private static final String ID_ATTR = "id";
	private static final String CLASS_ATTR = "class";	
	
	public static final XMLSearcherManager INSTANCE = new XMLSearcherManager();
	private static final String SEARCHERS_EXTENSION_POINT = "searchers";

	private Map<String, IXMLSearcher> searchersById = null;

	public static XMLSearcherManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (searchersById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addSearcher(searchersById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addSearcher(
			Map<String, IXMLSearcher> searchersById, IConfigurationElement[] cf) {
		IXMLSearcher searcher = null;
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get searcher declaration
			if (SEARCHER_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {//					
					searcher = (IXMLSearcher) ce
							.createExecutableExtension(CLASS_ATTR);
					searchersById.put(id, searcher);
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load searcher for id: " + id, t);
				}
			}
		}
	}

	public IXMLSearcher getSearcher(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (searchersById == null) {
			loadSearchers();
		}

		return searchersById.get(id);
	}

	private synchronized void loadSearchers() {
		if (searchersById != null) {
			return;
		}
		Map<String, IXMLSearcher> searchersById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID, SEARCHERS_EXTENSION_POINT);
			searchersById = new HashMap<String, IXMLSearcher>(cf.length);
			addSearcher(searchersById, cf);
		} else {
			searchersById = new HashMap<String, IXMLSearcher>();
		}
		this.searchersById = searchersById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return SEARCHERS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
}
