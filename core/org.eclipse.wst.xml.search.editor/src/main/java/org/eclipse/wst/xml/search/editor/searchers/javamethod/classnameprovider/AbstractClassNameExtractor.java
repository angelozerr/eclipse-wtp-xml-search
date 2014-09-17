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
package org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.w3c.dom.Node;

public abstract class AbstractClassNameExtractor implements IClassNameExtractor {

	public final String[] extractClassNames(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		if (node == null) {
			return null;
		}
		return doExtractClassNames(node, file, pathForClass, findByAttrName,
				findByParentNode, xpathFactoryProviderId, namespaceInfo);
	}

	protected abstract String[] doExtractClassNames(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException;

}
