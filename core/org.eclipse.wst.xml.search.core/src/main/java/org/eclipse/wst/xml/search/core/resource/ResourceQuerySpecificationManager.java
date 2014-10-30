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
package org.eclipse.wst.xml.search.core.resource;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.resource.ResourceQuerySpecification;
import org.eclipse.wst.xml.search.core.util.StringUtils;

public class ResourceQuerySpecificationManager extends AbstractRegistryManager {

	private static final ResourceQuerySpecificationManager INSTANCE = new ResourceQuerySpecificationManager();
	private static final String RESOURCE_QUERY_SPECIFICATIONS_EXTENSION_POINT = "resourceQuerySpecifications";
	private Map<String, IResourceQuerySpecification> querySpecificationsById = null;

	public static ResourceQuerySpecificationManager getDefault() {
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

	public IResourceQuerySpecification getQuerySpecification(
			String querySpecificationId) {
		if (StringUtils.isEmpty(querySpecificationId)) {
			return ResourceQuerySpecification.DEFAULT;
		}
		if (querySpecificationsById == null) {
			loadQuerySpecifications();
		}

		IResourceQuerySpecification querySpecification = querySpecificationsById
				.get(querySpecificationId);
		if (querySpecification == null) {
			querySpecification = ResourceQuerySpecification.DEFAULT;
			querySpecificationsById.put(querySpecificationId, querySpecification);
		}
		return querySpecification;
	}

	private synchronized void loadQuerySpecifications() {
		if (querySpecificationsById != null) {
			return;
		}

		Map<String, IResourceQuerySpecification> querySpecificationsById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchCorePlugin.PLUGIN_ID,
					RESOURCE_QUERY_SPECIFICATIONS_EXTENSION_POINT);
			querySpecificationsById = new HashMap<String, IResourceQuerySpecification>(
					cf.length);
			addQuerySpecifications(querySpecificationsById, cf);
		} else {
			querySpecificationsById = new HashMap<String, IResourceQuerySpecification>();
		}
		this.querySpecificationsById = querySpecificationsById;
		super.addRegistryListenerIfNeeded();

	}

	private synchronized void addQuerySpecifications(
			Map<String, IResourceQuerySpecification> querySpecificationsById,
			IConfigurationElement[] cf) {
		String id = null;
		IResourceQuerySpecification querySpecification = null;
		for (IConfigurationElement ce : cf) {
			// querySpecification declaration,
			id = ce.getAttribute("id");
			try {
				querySpecification = ResourceQuerySpecification
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
