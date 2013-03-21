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
package org.eclipse.wst.xml.search.editor.internal.references.filters;

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
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;

public class XMLReferenceFiltersManager extends AbstractRegistryManager {

	private static final String CLASS_ATTR = "class";
	private static final String ID_ATTR = "id";
	private static final String FILTER_ELT = "filter";
	public static final XMLReferenceFiltersManager INSTANCE = new XMLReferenceFiltersManager();
	private static final String XML_REFERENCE_FILTERS_EXTENSION_POINT = "referenceFilters";

	private Map<String, IXMLReferenceFilter> filtersById = null;

	public static XMLReferenceFiltersManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (filtersById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addXMLReferenceFilters(filtersById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addXMLReferenceFilters(
			Map<String, IXMLReferenceFilter> providersById,
			IConfigurationElement[] cf) {
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get additionalProposalInfoProvider declaration
			if (FILTER_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {
					IXMLReferenceFilter provider = (IXMLReferenceFilter) ce
							.createExecutableExtension(CLASS_ATTR);
					providersById.put(id, provider);
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load XML references filters for id: "
									+ id, t);
				}
			}
		}
	}

	public IXMLReferenceFilter getProvider(String referenceId) {
		if (StringUtils.isEmpty(referenceId)) {
			return null;
		}
		if (filtersById == null) {
			loadXMLReferenceFilters();
		}

		return filtersById.get(referenceId);
	}

	private synchronized void loadXMLReferenceFilters() {
		if (filtersById != null) {
			return;
		}
		Map<String, IXMLReferenceFilter> providersById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID,
					XML_REFERENCE_FILTERS_EXTENSION_POINT);
			providersById = new HashMap<String, IXMLReferenceFilter>(cf.length);
			addXMLReferenceFilters(providersById, cf);
		} else {
			providersById = new HashMap<String, IXMLReferenceFilter>();
		}
		this.filtersById = providersById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return XML_REFERENCE_FILTERS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
}
