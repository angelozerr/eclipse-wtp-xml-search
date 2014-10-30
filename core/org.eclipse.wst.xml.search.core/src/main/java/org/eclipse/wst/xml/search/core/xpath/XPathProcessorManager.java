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
package org.eclipse.wst.xml.search.core.xpath;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.preferences.PreferenceInitializer;
import org.eclipse.wst.xml.search.core.internal.xpath.XPathProcessorType;
import org.eclipse.wst.xml.search.core.util.StringUtils;

public class XPathProcessorManager extends AbstractRegistryManager {

	private static final XPathProcessorManager INSTANCE = new XPathProcessorManager();
	private static final String XPATH_EVALUATORS_EXTENSION_POINT = "xpathProcessors";
	private Map<String, IXPathProcessorType> processorsById = null;

	private static final String PROCESSOR_ELT = "processor";
	private static final String ID_ATTR = "id";
	private static final String CLASS_ATTR = "class";
	private static final String NAME_ATTR = "name";

	private IXPathProcessorType processorType;

	public static XPathProcessorManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (processorsById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addProcessors(processorsById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addProcessors(
			Map<String, IXPathProcessorType> processorsById,
			IConfigurationElement[] cf) {
		String id = null;
		String name = null;
		String source = null;
		for (IConfigurationElement ce : cf) {
			source = ce.getContributor().getName();
			// loop for to get processor declaration
			if (PROCESSOR_ELT.equals(ce.getName())) {
				try {
					id = ce.getAttribute(ID_ATTR);
					name = ce.getAttribute(NAME_ATTR);
					IXPathProcessor processor = (IXPathProcessor) ce
							.createExecutableExtension(CLASS_ATTR);
					processorsById.put(id, new XPathProcessorType(id, name,
							source, true, processor));
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load XPath processor for id: " + id, t);
				}
			}
		}
	}

	public IXPathProcessorType getProcessor(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (processorsById == null) {
			loadProcessors();
		}

		return processorsById.get(id);
	}

	private synchronized void loadProcessors() {
		if (processorsById != null) {
			return;
		}
		Map<String, IXPathProcessorType> processorsById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					getPluginId(), getExtensionPoint());
			processorsById = new HashMap<String, IXPathProcessorType>(cf.length);
			addProcessors(processorsById, cf);
		} else {
			processorsById = new HashMap<String, IXPathProcessorType>();
		}
		this.processorsById = processorsById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return XPATH_EVALUATORS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchCorePlugin.PLUGIN_ID;
	}

	public IXPathProcessorType[] getProcessors() {
		if (processorsById == null) {
			loadProcessors();
		}
		return processorsById.values().toArray(IXPathProcessorType.EMPTY);
	}

	public void setDefaultProcessor(IXPathProcessorType processorType) {
		this.processorType = processorType;
		if (processorType == null) {
			XMLSearchCorePlugin
					.getDefault()
					.getPluginPreferences()
					.setValue(PreferenceInitializer.DEFAULT_XPATH_PROCESSOR, "");
		} else {
			XMLSearchCorePlugin
					.getDefault()
					.getPluginPreferences()
					.setValue(PreferenceInitializer.DEFAULT_XPATH_PROCESSOR,
							processorType.getId());
		}
		XMLSearchCorePlugin.getDefault().savePluginPreferences();
	}

	public IXPathProcessorType getDefaultProcessor() {
		if (processorType == null) {
			String id = XMLSearchCorePlugin.getDefault().getPluginPreferences()
					.getString(PreferenceInitializer.DEFAULT_XPATH_PROCESSOR);
			processorType = getProcessor(id);
		}
		return processorType;
	}

}
