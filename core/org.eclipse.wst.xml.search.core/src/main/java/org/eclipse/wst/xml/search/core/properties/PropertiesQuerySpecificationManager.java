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
package org.eclipse.wst.xml.search.core.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.properties.PropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.util.StringUtils;

/**
 * Manager of {@link IPropertiesQuerySpecification} which are registered with
 * extension point "propertiesQuerySpecifications".
 * 
 */
public class PropertiesQuerySpecificationManager extends
		AbstractRegistryManager {

	private static final PropertiesQuerySpecificationManager INSTANCE = new PropertiesQuerySpecificationManager();
	private static final String RESOURCE_QUERY_SPECIFICATIONS_EXTENSION_POINT = "propertiesQuerySpecifications";
	private Map<String, IPropertiesQuerySpecification> querySpecificationsById = null;

	public static PropertiesQuerySpecificationManager getDefault() {
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

	public IPropertiesQuerySpecification getQuerySpecification(
			String querySpecificationId) {
		if (StringUtils.isEmpty(querySpecificationId)) {
			return PropertiesQuerySpecification.DEFAULT;
		}
		if (querySpecificationsById == null) {
			loadQuerySpecifications();
		}

		IPropertiesQuerySpecification querySpecification = querySpecificationsById
				.get(querySpecificationId);
		if (querySpecification == null) {
			querySpecification = PropertiesQuerySpecification.DEFAULT;
			querySpecificationsById.put(querySpecificationId,
					querySpecification);
		}
		return querySpecification;
	}

	private synchronized void loadQuerySpecifications() {
		if (querySpecificationsById != null) {
			return;
		}

		Map<String, IPropertiesQuerySpecification> querySpecificationsById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchCorePlugin.PLUGIN_ID,
					RESOURCE_QUERY_SPECIFICATIONS_EXTENSION_POINT);
			querySpecificationsById = new HashMap<String, IPropertiesQuerySpecification>(
					cf.length);
			addQuerySpecifications(querySpecificationsById, cf);
		} else {
			querySpecificationsById = new HashMap<String, IPropertiesQuerySpecification>();
		}
		this.querySpecificationsById = querySpecificationsById;
		super.addRegistryListenerIfNeeded();

	}

	private synchronized void addQuerySpecifications(
			Map<String, IPropertiesQuerySpecification> querySpecificationsById,
			IConfigurationElement[] cf) {
		String id = null;
		IPropertiesQuerySpecification querySpecification = null;
		for (IConfigurationElement ce : cf) {
			// querySpecification declaration,
			id = ce.getAttribute("id");
			try {
				querySpecification = PropertiesQuerySpecification
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
		return RESOURCE_QUERY_SPECIFICATIONS_EXTENSION_POINT;
	}

}
