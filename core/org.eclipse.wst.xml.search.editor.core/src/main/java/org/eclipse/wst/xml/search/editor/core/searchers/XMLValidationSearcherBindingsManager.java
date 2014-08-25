package org.eclipse.wst.xml.search.editor.core.searchers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.editor.core.internal.XMLSearchEditorCorePlugin;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;

public class XMLValidationSearcherBindingsManager extends AbstractRegistryManager{

	// handle bindings between reference or reference-to and searcherId
	public static final XMLValidationSearcherBindingsManager INSTANCE = new XMLValidationSearcherBindingsManager();

	private static final String BINDING_ELT = "binding";
	private static final String SEARCHER_ATTR = "validationSearcher";
	private static final String REFERENCE_ATTR = "reference";
	private static final String REFERENCE_TO_ATTR = "referenceTo";

	private static final String BINDINGS_EXTENSION_POINT = "validationSearcherBindings";

	private Map<String, String> searcherIdByReferenceId = null;

	public static XMLValidationSearcherBindingsManager getDefault() {
		return INSTANCE;
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

	public String getValidationSearcherId(String referenceId) {
		if (searcherIdByReferenceId == null) {
			loadBindings();
		}
		return searcherIdByReferenceId.get(referenceId);
	}

	private synchronized void loadBindings() {
		if (searcherIdByReferenceId != null) {
			return;
		}
		Map<String, String> searcherIdByReferenceId = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorCorePlugin.PLUGIN_ID, BINDINGS_EXTENSION_POINT);
			searcherIdByReferenceId = new HashMap<String, String>(cf.length);
			addBinding(searcherIdByReferenceId, cf);
		} else {
			searcherIdByReferenceId = new HashMap<String, String>();
		}
		this.searcherIdByReferenceId = searcherIdByReferenceId;
		super.addRegistryListenerIfNeeded();

	}
	
	@Override
	protected String getExtensionPoint() {
		return BINDINGS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorCorePlugin.PLUGIN_ID;
	}

	// Default validation searcher ID
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_EXPRESSION = "org.eclipse.wst.xml.search.editor.core.validation.searcher.expression";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_STATICS = "org.eclipse.wst.xml.search.editor.core.validation.searcher.statics";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_JAVAMETHOD = "org.eclipse.wst.xml.search.editor.core.validation.searcher.javamethod";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_JAVA = "org.eclipse.wst.xml.search.editor.core.validation.searcher.java";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_RESOURCE = "org.eclipse.wst.xml.search.editor.core.validation.searcher.resource";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_PROPERTIES = "org.eclipse.wst.xml.search.editor.core.validation.searcher.properties";
    private static final String DEFAULT_VALIDATION_SEARCHER_FOR_XML = "org.eclipse.wst.xml.search.editor.core.validation.searcher.xml";

    private static final XMLValidationSearcherManager searcherManager = XMLValidationSearcherManager.getDefault();
    
    public IXMLValidationSearcher getValidationSearcher(IXMLReference reference) {
        IXMLValidationSearcher retval = null;
        if (reference instanceof IXMLReferenceToExpression) {
            retval = getValidationSearcher(reference.getReferenceId(), DEFAULT_VALIDATION_SEARCHER_FOR_EXPRESSION);
        }
        return retval;
    }
    
    public IXMLValidationSearcher getValidationSearcher(IXMLReferenceTo referenceTo) {
        return getValidationSearcher(referenceTo.getReferenceToId(), getDefaultValidationSearcherId(referenceTo));
    }
    
    private  String getDefaultValidationSearcherId(IXMLReferenceTo referenceTo) {
        switch(referenceTo.getType()) {
            case XML:
                return DEFAULT_VALIDATION_SEARCHER_FOR_XML;
            case JAVA:
                return DEFAULT_VALIDATION_SEARCHER_FOR_JAVA;
            case JAVA_METHOD:
                return DEFAULT_VALIDATION_SEARCHER_FOR_JAVAMETHOD;
            case RESOURCE:
                return DEFAULT_VALIDATION_SEARCHER_FOR_RESOURCE;
            case PROPERTY:
                return DEFAULT_VALIDATION_SEARCHER_FOR_PROPERTIES;
            case STATIC:
                return DEFAULT_VALIDATION_SEARCHER_FOR_STATICS;
            default:
                return null;
        }
    }
    
    private IXMLValidationSearcher getValidationSearcher(String referenceId, String defaultSearcherId) {
        String validationSearcherId = getDefault().getValidationSearcherId(referenceId);
        if (validationSearcherId == null) {
            validationSearcherId = defaultSearcherId;
        }
        IXMLValidationSearcher retval = searcherManager.getValidationSearcher(validationSearcherId);
        if (retval == null) {
            retval = searcherManager.getValidationSearcher(defaultSearcherId);
        }
        return retval;
    }
}
