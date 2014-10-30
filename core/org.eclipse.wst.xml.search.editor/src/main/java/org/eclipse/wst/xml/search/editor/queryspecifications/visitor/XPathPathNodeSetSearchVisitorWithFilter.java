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
package org.eclipse.wst.xml.search.editor.queryspecifications.visitor;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.XPathNodeSetSearchVisitor;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;

public class XPathPathNodeSetSearchVisitorWithFilter extends
		XPathNodeSetSearchVisitor {

	private final IXMLReferenceFilter filter;

	public XPathPathNodeSetSearchVisitorWithFilter(IXMLReferenceFilter filter) {
		this.filter = filter;
	}

	@Override
	protected boolean canAddNode(IDOMNode nodeToAdd, Object selectedNode) {
		return filter.accept(nodeToAdd);
	}
}
