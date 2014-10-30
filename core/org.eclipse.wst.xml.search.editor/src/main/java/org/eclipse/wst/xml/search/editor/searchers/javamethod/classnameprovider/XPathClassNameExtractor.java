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
package org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XPathClassNameExtractor extends AbstractClassNameExtractor {

	public static final IClassNameExtractor INSTANCE = new XPathClassNameExtractor();

	public String[] doExtractClassNames(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		if (findByAttrName != null && node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = ((Element) node);
			// Optimisation : don't execute XPath if class come from for
			// attribute name
			if (findByParentNode) {
				Node parent = element.getParentNode();
				if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
					return new String[] { ((Element) parent).getAttribute(findByAttrName) };
				}
				return null;
			}
			return new String[] { element.getAttribute(findByAttrName) };
		}

		return new String[] { XPathManager.getManager().evaluateString(xpathFactoryProviderId,
				node, pathForClass, namespaceInfo, null) };
	}

}
