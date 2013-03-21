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
package org.eclipse.jst.jsp.search.editor.queryspecifications;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchVisitorProvider;
import org.eclipse.wst.xml.search.editor.queryspecifications.visitor.XPathPathNodeSetSearchVisitorWithFilter;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;

public abstract class JSPQuerySpecification implements
		IXMLSearchRequestorProvider, IResourceProvider,
		IXMLSearchVisitorProvider {

	private final IXMLSearchDOMDocumentVisitor visitor;

	public JSPQuerySpecification(IXMLReferenceFilter filter) {
		visitor = new XPathPathNodeSetSearchVisitorWithFilter(filter);
	}

	public IXMLSearchRequestor getRequestor() {
		return JSPSearchRequestor.INSTANCE;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return WebContentFolderProvider.INSTANCE.getResource(selectedNode,
				resource);
	}

	public IXMLSearchDOMDocumentVisitor getVisitor() {
		return visitor;
	}
}
