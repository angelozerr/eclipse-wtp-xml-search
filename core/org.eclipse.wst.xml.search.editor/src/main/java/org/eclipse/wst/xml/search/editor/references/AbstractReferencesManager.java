package org.eclipse.wst.xml.search.editor.references;

import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.w3c.dom.Node;

public abstract class AbstractReferencesManager<T extends IReference, E> extends AbstractRegistryManager {

	protected static final String REFERENCES_ELT = "references";
	// from
	protected static final String FROM_ELT = "from";

	
	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}
	
	public abstract T getXMLReference(E node, String contentTypeId);
}
