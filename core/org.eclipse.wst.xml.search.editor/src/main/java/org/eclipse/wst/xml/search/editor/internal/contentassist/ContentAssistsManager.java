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
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;

public class ContentAssistsManager extends AbstractRegistryManager {

	private static final String ID_ATTR = "id";
	private static final String CLASS_ATTR = "class";
	private static final String ADDITIONAL_PROPOSAL_INFO_PROVIDER_ELT = "additionalProposalInfoProvider";
	public static final ContentAssistsManager INSTANCE = new ContentAssistsManager();
	private static final String CONTENT_ASSISTS_EXTENSION_POINT = "contentAssists";

	private Map<String, IContentAssistAdditionalProposalInfoProvider> providersById = null;

	public static ContentAssistsManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (providersById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addContentAssists(providersById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addContentAssists(
			Map<String, IContentAssistAdditionalProposalInfoProvider> providersById,
			IConfigurationElement[] cf) {
		IXMLReference reference = null;
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get additionalProposalInfoProvider declaration
			if (ADDITIONAL_PROPOSAL_INFO_PROVIDER_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {
					IContentAssistAdditionalProposalInfoProvider provider = (IContentAssistAdditionalProposalInfoProvider) ce
							.createExecutableExtension(CLASS_ATTR);
					providersById.put(id, provider);
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load contentAssists for id: " + id, t);
				}
			}
		}
	}

	public IContentAssistAdditionalProposalInfoProvider getProvider(
			String providerId) {
		if (StringUtils.isEmpty(providerId)) {
			return null;
		}
		if (providersById == null) {
			loadContentAssists();
		}

		return providersById.get(providerId);
	}

	private synchronized void loadContentAssists() {
		if (providersById != null) {
			return;
		}
		Map<String, IContentAssistAdditionalProposalInfoProvider> providersById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID,
					CONTENT_ASSISTS_EXTENSION_POINT);
			providersById = new HashMap<String, IContentAssistAdditionalProposalInfoProvider>(
					cf.length);
			addContentAssists(providersById, cf);
		} else {
			providersById = new HashMap<String, IContentAssistAdditionalProposalInfoProvider>();
		}
		this.providersById = providersById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return CONTENT_ASSISTS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
}
