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
package org.eclipse.wst.xml.search.editor.core.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.eclipse.wst.xml.search.editor.core.internal.XMLSearchEditorCorePlugin;

public class JavaReferencesMatchersManager extends AbstractRegistryManager {

	private static final JavaReferencesMatchersManager INSTANCE = new JavaReferencesMatchersManager();
	private static final String JAVA_REFERENCES_MATCHERS_EXTENSION_POINT = "javaReferencesMatchers";

	private Map<String, IJavaElementMatcher> matchersById = null;

	public static JavaReferencesMatchersManager getInstance() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (matchersById == null) // not loaded yet
			return;
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addQuerySpecifications(matchersById, cf);
		} else {
			// TODO : remove references
		}
	}

	public IJavaElementMatcher getMatcher(
			String querySpecificationId) {
		if (StringUtils.isEmpty(querySpecificationId)) {
			return IJavaElementMatcher.TRUE_MATCHER;
		}
		if (matchersById == null) {
			loadQuerySpecifications();
		}

		IJavaElementMatcher querySpecification = matchersById
				.get(querySpecificationId);
		if (querySpecification == null) {
			querySpecification = IJavaElementMatcher.TRUE_MATCHER;
			matchersById.put(querySpecificationId,
					querySpecification);
		}
		return querySpecification;
	}

	private synchronized void loadQuerySpecifications() {
		if (matchersById != null) {
			return;
		}

		Map<String, IJavaElementMatcher> querySpecificationsById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorCorePlugin.PLUGIN_ID,
					JAVA_REFERENCES_MATCHERS_EXTENSION_POINT);
			querySpecificationsById = new HashMap<String, IJavaElementMatcher>(
					cf.length);
			addQuerySpecifications(querySpecificationsById, cf);
		} else {
			querySpecificationsById = new HashMap<String, IJavaElementMatcher>();
		}
		this.matchersById = querySpecificationsById;
		super.addRegistryListenerIfNeeded();

	}

	private synchronized void addQuerySpecifications(
			Map<String, IJavaElementMatcher> querySpecificationsById,
			IConfigurationElement[] cf) {
		String id = null;
		IJavaElementMatcher querySpecification = null;
		for (IConfigurationElement ce : cf) {
			// querySpecification declaration,
			id = ce.getAttribute("id");
			try {
				querySpecification = (IJavaElementMatcher)ce.createExecutableExtension("class");
				querySpecificationsById.put(id, querySpecification);
			} catch (Throwable t) {
				Trace.trace(Trace.SEVERE,
						"  Could not load querySpecification for id: " + id, t);
			}
		}
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorCorePlugin.PLUGIN_ID;
	}

	@Override
	protected String getExtensionPoint() {
		return JAVA_REFERENCES_MATCHERS_EXTENSION_POINT;
	}

}
