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
package org.eclipse.wst.xml.search.editor.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.SimpleXMLSearchEngine;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.core.xpath.XPathProcessorManager;
import org.eclipse.wst.xml.search.editor.internal.reporter.XMLSearchReporterManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo.ToType;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToXML;
import org.w3c.dom.Node;

public class XMLSearcherForXMLUtils {

	public static void search(Object selectedNode, String mathingString,
			IFile file, IXMLReferenceTo referenceTo,
			IXMLSearchDOMNodeCollector collector, boolean startsWith) {
		if (referenceTo.getType() != ToType.XML)
			return;

		final IXMLReferenceToXML referencePath = (IXMLReferenceToXML) referenceTo;
		IXMLQuerySpecification querySpecification = XMLQuerySpecificationUtil
				.getQuerySpecification(referencePath);
		if (querySpecification != null) {

			String xpathProcessorId = querySpecification.getXPathProcessorId();
			if (xpathProcessorId == null) {
				IXPathProcessorType processor = XPathProcessorManager
						.getDefault().getDefaultProcessor();
				xpathProcessorId = processor != null ? processor.getId() : null;
			}

			String xpath = null;
			if (startsWith) {
				xpath = referencePath.getQuery(selectedNode, mathingString,
						querySpecification.getStartsWithStringQueryBuilder());
			} else {
				xpath = referencePath.getQuery(selectedNode, mathingString,
						querySpecification.getEqualsStringQueryBuilder());
			}

			Namespaces namespaceInfos = referencePath.getNamespaces();			

			if (querySpecification.isMultiResource()) {
				SimpleXMLSearchEngine.getDefault().search(
						querySpecification.getResources(selectedNode, file),
						querySpecification.getRequestor(),
						querySpecification.getVisitor(), xpath,
						xpathProcessorId, namespaceInfos, collector,
						selectedNode, XMLSearchReporterManager.getDefault(),
						null);
			} else {
				SimpleXMLSearchEngine.getDefault().search(
						querySpecification.getResource(selectedNode, file),
						querySpecification.getRequestor(),
						querySpecification.getVisitor(), xpath,
						xpathProcessorId, namespaceInfos, collector,
						selectedNode, XMLSearchReporterManager.getDefault(),
						null);
			}

			// Storage
			if (querySpecification.isMultiStorage()) {
				SimpleXMLSearchEngine.getDefault().search(
						querySpecification.getStorages(selectedNode, file),
						querySpecification.getRequestor(),
						querySpecification.getVisitor(), xpath,
						xpathProcessorId, namespaceInfos, collector,
						selectedNode, XMLSearchReporterManager.getDefault(),
						null);

			} else {
				if (querySpecification.isSimpleStorage()) {
					SimpleXMLSearchEngine.getDefault().search(
							querySpecification.getStorage(selectedNode, file),
							querySpecification.getRequestor(),
							querySpecification.getVisitor(), xpath,
							xpathProcessorId, namespaceInfos, collector,
							selectedNode,
							XMLSearchReporterManager.getDefault(), null);
				}
			}

		}

	}
}
