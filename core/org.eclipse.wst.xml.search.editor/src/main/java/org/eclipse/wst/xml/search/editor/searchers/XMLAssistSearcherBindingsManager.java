package org.eclipse.wst.xml.search.editor.searchers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;

public class XMLAssistSearcherBindingsManager extends AbstractRegistryManager{

	private static final String BINDING_ELT = "binding";
	private static final String SEARCHER_ATTR = "assistSearcher";
	private static final String REFERENCE_ATTR = "reference";
	private static final String REFERENCE_TO_ATTR = "referenceTo";

	public static final XMLAssistSearcherBindingsManager INSTANCE = new XMLAssistSearcherBindingsManager();
	private static final String BINDINGS_EXTENSION_POINT = "assistSearcherBindings";

	private Map<String, String> searcherIdByReferenceId = null;

	// Default validation searcher ID
    private static final String DEFAULT_ASSIST_SEARCHER_FOR_EXPRESSION = "org.eclipse.wst.xml.search.editor.assist.searcher.expression";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_STATICS = "org.eclipse.wst.xml.search.editor.assist.searcher.statics";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_JAVAMETHOD = "org.eclipse.wst.xml.search.editor.assist.searcher.javamethod";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_JAVA = "org.eclipse.wst.xml.search.editor.assist.searcher.java";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_RESOURCE = "org.eclipse.wst.xml.search.editor.assist.searcher.resource";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_PROPERTIES = "org.eclipse.wst.xml.search.editor.assist.searcher.properties";
	private static final String DEFAULT_ASSIST_SEARCHER_FOR_XML = "org.eclipse.wst.xml.search.editor.assist.searcher.xml";
	private static final XMLAssistSearchersManager searcherManager = XMLAssistSearchersManager.getDefault();

	public static XMLAssistSearcherBindingsManager getDefault() {
		return INSTANCE;
	}
    
	private String getDefaultAssistSearcherId(IXMLReferenceTo referenceTo) {
        switch(referenceTo.getType()) {
            case XML:
                return DEFAULT_ASSIST_SEARCHER_FOR_XML;
            case JAVA:
                return DEFAULT_ASSIST_SEARCHER_FOR_JAVA;
            case JAVA_METHOD:
                return DEFAULT_ASSIST_SEARCHER_FOR_JAVAMETHOD;
            case RESOURCE:
                return DEFAULT_ASSIST_SEARCHER_FOR_RESOURCE;
            case PROPERTY:
                return DEFAULT_ASSIST_SEARCHER_FOR_PROPERTIES;
            case STATIC:
                return DEFAULT_ASSIST_SEARCHER_FOR_STATICS;
            default:
                return null;
        }
    }
    
    public IXMLAssistSearcher getXMLAssistSearcher(IXMLReference reference) {
        IXMLAssistSearcher retval = null;
        if (reference instanceof IXMLReferenceToExpression) {
            retval = getXMLAssistSearcher(reference.getReferenceId(), DEFAULT_ASSIST_SEARCHER_FOR_EXPRESSION);
        }
        return retval;
    }
    
    public IXMLAssistSearcher getXMLAssistSearcher(IXMLReferenceTo referenceTo) {
        return getXMLAssistSearcher(referenceTo.getReferenceToId(), getDefaultAssistSearcherId(referenceTo));
    }
    
    private IXMLAssistSearcher getXMLAssistSearcher(String referenceId, String defaultSearcherId) {
        String searcherId = getDefault().getAssistSearcherId(referenceId);
        if (searcherId == null) {
            searcherId = defaultSearcherId;
        }
        IXMLAssistSearcher retval = searcherManager.getAssistSearcher(searcherId);
        if (retval == null) {
            retval = searcherManager.getAssistSearcher(defaultSearcherId);
        }
        return retval;
    }
    
    private synchronized void addBinding(
			Map<String, String> searcherIdByReferenceId, IConfigurationElement[] cf) {
		for (IConfigurationElement ce : cf) {
			if (BINDING_ELT.equals(ce.getName())) {
				final String searcherId = ce.getAttribute(SEARCHER_ATTR);
				final String referenceId = ce.getAttribute(REFERENCE_TO_ATTR) != null ? 
						ce.getAttribute(REFERENCE_TO_ATTR) : ce.getAttribute(REFERENCE_ATTR); 
				searcherIdByReferenceId.put(referenceId, searcherId);
			}
		}
	}

    @Override
	protected String getExtensionPoint() {
		return BINDINGS_EXTENSION_POINT;
	}
    
    @Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
    
    public String getAssistSearcherId(String referenceId) {
		if (searcherIdByReferenceId == null) {
			loadBindings();
		}
		return searcherIdByReferenceId.get(referenceId);
	}

    @Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (searcherIdByReferenceId == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addBinding(searcherIdByReferenceId, cf);
		} else {// TODO : remove references
		}
	}
    
    private synchronized void loadBindings() {
		if (searcherIdByReferenceId != null) {
			return;
		}
		Map<String, String> searcherIdByReferenceId = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID, BINDINGS_EXTENSION_POINT);
			searcherIdByReferenceId = new HashMap<String, String>(cf.length);
			addBinding(searcherIdByReferenceId, cf);
		} else {
			searcherIdByReferenceId = new HashMap<String, String>();
		}
		this.searcherIdByReferenceId = searcherIdByReferenceId;
		super.addRegistryListenerIfNeeded();
	}
}
