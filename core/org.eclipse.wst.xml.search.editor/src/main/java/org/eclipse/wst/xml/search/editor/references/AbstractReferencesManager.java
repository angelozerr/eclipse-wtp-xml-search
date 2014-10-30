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
