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

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.XMLSearchEngineException;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;

/**
 * XML search visitor to visit a DOM document and collect DOM Node which follow
 * the query.
 * 
 */
public interface IXMLSearchDOMDocumentVisitor {

	/**
	 * Visit the DOM document and collect DOM Node which follow the query XPath.
	 * 
	 * @param document
	 *            the DOM document to visit.
	 * @param query
	 *            the query (ex : XPath) to use to retrieve DOM Node to collect.
	 * @param xpathProcessorId
	 *            the XPath processor id to use to execute XPath.
	 * @param collector
	 *            the collector used to collect DOM Node.
	 */
	void visit(IDOMDocument document, String query, String xpathProcessorId,			
			IXMLSearchDOMNodeCollector collector, Object selectedNode)
			throws XMLSearchEngineException;

}
