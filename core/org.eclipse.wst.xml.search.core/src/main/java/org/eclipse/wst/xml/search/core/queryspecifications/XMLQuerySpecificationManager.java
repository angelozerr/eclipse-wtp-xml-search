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
package org.eclipse.wst.xml.search.core.queryspecifications;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.queryspecifications.XMLQuerySpecification;
import org.eclipse.wst.xml.search.core.util.StringUtils;

/**
 * Manager of {@link IXMLQuerySpecification} which are registered with extension
 * point "querySpecifications".
 * 
 */
public class XMLQuerySpecificationManager extends AbstractRegistryManager {

	private static final XMLQuerySpecificationManager INSTANCE = new XMLQuerySpecificationManager();
	private static final String XML_QUERY_SPECIFICATIONS_EXTENSION_POINT = "querySpecifications";

	private Map<String, IXMLQuerySpecification> querySpecificationsById = null;

	public static XMLQuerySpecificationManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (querySpecificationsById == null) // not loaded yet
			return;
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addQuerySpecifications(querySpecificationsById, cf);
		} else {
			// TODO : remove references
		}
	}

	public IXMLQuerySpecification getQuerySpecification(
			String querySpecificationId) {
		if (StringUtils.isEmpty(querySpecificationId)) {
			return XMLQuerySpecification.INSTANCE;
		}
		if (querySpecificationsById == null) {
			loadQuerySpecifications();
		}

		IXMLQuerySpecification querySpecification = querySpecificationsById
				.get(querySpecificationId);
		if (querySpecification == null) {
			querySpecification = XMLQuerySpecification.INSTANCE;
			querySpecificationsById.put(querySpecificationId,
					querySpecification);
		}
		return querySpecification;
	}

	private synchronized void loadQuerySpecifications() {
		if (querySpecificationsById != null) {
			return;
		}

		Map<String, IXMLQuerySpecification> querySpecificationsById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchCorePlugin.PLUGIN_ID,
					XML_QUERY_SPECIFICATIONS_EXTENSION_POINT);
			querySpecificationsById = new HashMap<String, IXMLQuerySpecification>(
					cf.length);
			addQuerySpecifications(querySpecificationsById, cf);
		} else {
			querySpecificationsById = new HashMap<String, IXMLQuerySpecification>();
		}
		this.querySpecificationsById = querySpecificationsById;
		super.addRegistryListenerIfNeeded();

	}

	private synchronized void addQuerySpecifications(
			Map<String, IXMLQuerySpecification> querySpecificationsById,
			IConfigurationElement[] cf) {
		String id = null;
		IXMLQuerySpecification querySpecification = null;
		for (IConfigurationElement ce : cf) {
			// querySpecification declaration,
			id = ce.getAttribute("id");
			try {
				querySpecification = XMLQuerySpecification
						.newQuerySpecification(ce
								.createExecutableExtension("class"));
				querySpecificationsById.put(id, querySpecification);
			} catch (Throwable t) {
				Trace.trace(Trace.SEVERE,
						"  Could not load querySpecification for id: " + id, t);
			}
		}
	}

	@Override
	protected String getPluginId() {
		return XMLSearchCorePlugin.PLUGIN_ID;
	}

	@Override
	protected String getExtensionPoint() {
		return XML_QUERY_SPECIFICATIONS_EXTENSION_POINT;
	}

}
