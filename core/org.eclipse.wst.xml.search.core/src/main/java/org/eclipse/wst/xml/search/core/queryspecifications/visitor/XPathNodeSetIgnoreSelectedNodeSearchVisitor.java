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
package org.eclipse.wst.xml.search.core.queryspecifications.visitor;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Search visitor which execute XPath and collect the resulted nodes and ignore
 * same nodes.
 * 
 */
public class XPathNodeSetIgnoreSelectedNodeSearchVisitor extends
		XPathNodeSetSearchVisitor {

	public static IXMLSearchDOMDocumentVisitor INSTANCE = new XPathNodeSetIgnoreSelectedNodeSearchVisitor();

	@Override
	protected boolean canAddNode(IDOMNode nodeToAdd, Object selectedNode) {
		if (selectedNode == null) {
			return true;
		}
		if (selectedNode.equals(nodeToAdd)) {
			return true;
		}
		if (!(selectedNode instanceof Node)) {
			return false;
		}
		Element element1 = DOMUtils.getOwnerElement(nodeToAdd);
		Element element2 = DOMUtils.getOwnerElement((Node) selectedNode);
		return (!(element1 != null && element1.equals(element2) || (element1 == null && element2 == null)));
	}
}
