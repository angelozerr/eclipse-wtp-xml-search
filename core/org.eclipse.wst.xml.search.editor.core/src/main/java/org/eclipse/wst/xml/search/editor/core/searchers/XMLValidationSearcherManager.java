package org.eclipse.wst.xml.search.editor.core.searchers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.eclipse.wst.xml.search.editor.core.internal.XMLSearchEditorCorePlugin;

public class XMLValidationSearcherManager extends AbstractRegistryManager{

	private static final String SEARCHER_ELT = "searcher";
	private static final String ID_ATTR = "id";
	private static final String CLASS_ATTR = "class";
	
	public static final XMLValidationSearcherManager INSTANCE = new XMLValidationSearcherManager();
	private static final String SEARCHERS_EXTENSION_POINT = "validationSearchers";

	private Map<String, IXMLValidationSearcher> searchersById = null;

	public static XMLValidationSearcherManager getDefault() {
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
			Map<String, IXMLValidationSearcher> searchersById, IConfigurationElement[] cf) {
		IXMLValidationSearcher searcher = null;
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get searcher declaration
			if (SEARCHER_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {//					
					searcher = (IXMLValidationSearcher) ce
							.createExecutableExtension(CLASS_ATTR);
					searchersById.put(id, searcher);
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load validation searcher for id: " + id, t);
				}
			}
		}
	}

	public IXMLValidationSearcher getValidationSearcher(String searcherId) {
		if (StringUtils.isEmpty(searcherId)) {
			return null;
		}
		if (searchersById == null) {
			loadSearchers();
		}

		return searchersById.get(searcherId);
	}

	private synchronized void loadSearchers() {
		if (searchersById != null) {
			return;
		}
		Map<String, IXMLValidationSearcher> searchersById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorCorePlugin.PLUGIN_ID, SEARCHERS_EXTENSION_POINT);
			searchersById = new HashMap<String, IXMLValidationSearcher>(cf.length);
			addSearcher(searchersById, cf);
		} else {
			searchersById = new HashMap<String, IXMLValidationSearcher>();
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
		// TODO move this class to editor core plugin
		return XMLSearchEditorCorePlugin.PLUGIN_ID;
	}

	public IXMLValidationSearcher getXMLValidationSearcher(IXMLReferenceTo referenceTo) {
		return null;
	}
}
