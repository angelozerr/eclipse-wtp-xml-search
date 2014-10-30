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
package org.eclipse.wst.xml.search.editor.internal.searchers.java;

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
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;

public class JavaQuerySpecificationrManager extends AbstractRegistryManager {

	private static final String CLASS_ATTR = "class";
	private static final String ID_ATTR = "id";
	private static final String QUERY_SPECIFICATION_ELT = "querySpecification";
	public static final JavaQuerySpecificationrManager INSTANCE = new JavaQuerySpecificationrManager();
	private static final String JAVA_QUERY_SPECIFICATIONS_EXTENSION_POINT = "javaQuerySpecifications";

	private Map<String, IJavaQuerySpecification> querySpecificationById = null;

	public static JavaQuerySpecificationrManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (querySpecificationById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addJavaFilter(querySpecificationById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addJavaFilter(
			Map<String, IJavaQuerySpecification> querySpecificationById,
			IConfigurationElement[] cf) {
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get provider declaration
			if (QUERY_SPECIFICATION_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {
					Object o = ce.createExecutableExtension(CLASS_ATTR);
					IJavaQuerySpecification querySpecification = JavaQuerySpecification
							.newJavaQuerySpecification(o);
					if (querySpecification != null) {
						querySpecificationById.put(id, querySpecification);
					}
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load javaQuerySpecification for id: "
									+ id, t);
				}
			}
		}
	}

	public IJavaQuerySpecification getQuerySpecification(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (querySpecificationById == null) {
			loadQuerySpecifications();
		}

		return querySpecificationById.get(id);
	}

	private synchronized void loadQuerySpecifications() {
		if (querySpecificationById != null) {
			return;
		}
		Map<String, IJavaQuerySpecification> querySpecificationById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID,
					JAVA_QUERY_SPECIFICATIONS_EXTENSION_POINT);
			querySpecificationById = new HashMap<String, IJavaQuerySpecification>(
					cf.length);
			addJavaFilter(querySpecificationById, cf);
		} else {
			querySpecificationById = new HashMap<String, IJavaQuerySpecification>();
		}
		this.querySpecificationById = querySpecificationById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return JAVA_QUERY_SPECIFICATIONS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
}
