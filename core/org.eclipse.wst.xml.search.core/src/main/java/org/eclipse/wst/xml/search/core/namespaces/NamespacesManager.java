package org.eclipse.wst.xml.search.core.namespaces;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.util.StringUtils;

public class NamespacesManager extends AbstractRegistryManager {

	private static final String NAMESPACES_EXTENSION_POINT = "namespaces";

	private static final String ID_ATTR = "id";
	private static final String NAMESPACES_ELT = "namespaces";
	private static final String MATCHER_ELT = "matcher";
	private static final String CLASS_ATTR = "class";
	private static final String PREFIX_ATTR = "prefix";

	private static final NamespacesManager INSTANCE = new NamespacesManager();

	private Map<String, Namespaces> namespacesById;

	public static NamespacesManager getInstance() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (namespacesById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addNamespaces(namespacesById, cf);
		} else {
			// TODO : remove references
		}
	}

	public Namespaces getNamespaces(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (namespacesById == null) {
			loadNamespaces();
		}
		return namespacesById.get(id);
	}

	private synchronized void loadNamespaces() {
		if (namespacesById != null) {
			return;
		}

		Map<String, Namespaces> namespacesById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry
					.getConfigurationElementsFor(
							getPluginId(),
							NAMESPACES_EXTENSION_POINT);
			namespacesById = new HashMap<String, Namespaces>(cf.length);
			addNamespaces(namespacesById, cf);
		} else {
			namespacesById = new HashMap<String, Namespaces>();
		}
		this.namespacesById = namespacesById;
		super.addRegistryListenerIfNeeded();

	}

	private synchronized void addNamespaces(
			Map<String, Namespaces> namespacesById, IConfigurationElement[] cf) {
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get searcher declaration
			if (NAMESPACES_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				Namespaces namespaces = new Namespaces();
				for (IConfigurationElement cns : ce.getChildren(MATCHER_ELT)) {
					parseNamespacesDecl(namespaces, cns);
				}
				namespacesById.put(id, namespaces);
			}
		}
	}

	private void parseNamespacesDecl(Namespaces namespaces,
			IConfigurationElement cns) {
		String prefix = cns.getAttribute(PREFIX_ATTR);
		try {
			INamespaceMatcher matcher = (INamespaceMatcher)cns.createExecutableExtension(CLASS_ATTR);
			namespaces.addMatcher(prefix, matcher);			
		} catch (CoreException e) {			
			Trace.trace(Trace.SEVERE, "Error namespace matcher.", e);
		}
		
	}

	@Override
	protected String getPluginId() {
		return XMLSearchCorePlugin.PLUGIN_ID;
	}

	@Override
	protected String getExtensionPoint() {
		return NAMESPACES_EXTENSION_POINT;
	}

}

