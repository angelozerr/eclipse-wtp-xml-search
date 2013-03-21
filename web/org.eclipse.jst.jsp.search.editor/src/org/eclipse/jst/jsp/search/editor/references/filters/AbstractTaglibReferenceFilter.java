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
package org.eclipse.jst.jsp.search.editor.references.filters;

import java.util.Collection;

import org.eclipse.jst.jsp.search.editor.internal.contentassist.util.TaglibUtils;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.w3c.dom.Node;

public abstract class AbstractTaglibReferenceFilter implements
		IXMLReferenceFilter {

	private Collection<String> taglibURIs = null;

	public boolean accept(Node node) {
		String taglibURI = TaglibUtils.getTagURI(node);
		if (StringUtils.isQuoted(taglibURI)) {
			return false;
		}
		Collection<String> uris = internalGetTaglibURIs();
		return uris.contains(taglibURI);
	}

	public Collection<String> internalGetTaglibURIs() {
		if (taglibURIs == null) {
			taglibURIs = getTaglibURIs();
		}
		return taglibURIs;
	}

	protected abstract Collection<String> getTaglibURIs();
}
