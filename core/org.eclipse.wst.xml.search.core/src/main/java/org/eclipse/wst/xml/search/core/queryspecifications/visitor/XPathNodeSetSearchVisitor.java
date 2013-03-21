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
package org.eclipse.wst.xml.search.core.queryspecifications.visitor;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.XMLSearchEngineException;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.w3c.dom.NodeList;

/**
 * Search visitor which execute XPath and collect the resulted nodes.
 * 
 */
public class XPathNodeSetSearchVisitor extends AbstractXMLSearchVisitor {

	public static IXMLSearchDOMDocumentVisitor INSTANCE = new XPathNodeSetSearchVisitor();

	public void visit(IDOMDocument document, String query,
			String xpathProcessorId, 
			IXMLSearchDOMNodeCollector collector, Object selectedNode)
			throws XMLSearchEngineException {
		try {
			NamespaceInfos namespaceInfos = XPathManager.getManager().getNamespaceInfo(
					document);
			NodeList list = XPathManager.getManager().evaluateNodeSet(
					xpathProcessorId, document, query, namespaceInfos, null);
			for (int i = 0; i < list.getLength(); i++) {
				IDOMNode el = (IDOMNode) list.item(i);
				if (canAddNode(el, selectedNode)) {
					collector.add(el);
				}
			}
		} catch (XPathExpressionException e) {
			throw new XMLSearchEngineException(e);
		}
	}

	protected boolean canAddNode(IDOMNode nodeToAdd, Object selectedNode) {
		return true;
	}

}
