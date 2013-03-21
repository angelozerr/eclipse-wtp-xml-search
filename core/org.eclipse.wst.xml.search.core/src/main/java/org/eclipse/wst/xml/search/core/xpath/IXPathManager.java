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
package org.eclipse.wst.xml.search.core.xpath;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface IXPathManager {

	NodeList evaluateNodeSet(String xpathFactoryProviderId, Object source,
			String xpath, NamespaceInfos namespaceInfo, String... criteria)
			throws XPathExpressionException;

	String evaluateString(String xpathFactoryProviderId, Object source,
			String xpath, NamespaceInfos namespaceInfo, String... criteria)
			throws XPathExpressionException;

	NamespaceInfos getNamespaceInfo(Node node);

	NamespaceInfos getNamespaceInfo(IDOMDocument document);

	//
	// Node evaluateNode(String xpathFactoryProviderId, Object source,
	// String xpath, String... criteria) throws XPathExpressionException;
	//
	// Boolean evaluateBoolean(String xpathFactoryProviderId, Object source,
	// String xpath, String... criteria) throws XPathExpressionException;
	//
	// Number evaluateNumber(String xpathFactoryProviderId, Object source,
	// String xpath, String... criteria) throws XPathExpressionException;
	//
	// String evaluateString(String xpathFactoryProviderId, Object source,
	// String xpath, String... criteria) throws XPathExpressionException;
	//
	// XPathExpression getXPathExpression(String xpathFactoryProviderId,
	// String xpath, String... args) throws XPathExpressionException;
	//
	// XPathExpression createXPathExpression(String xpathFactoryProviderId,
	// String expression) throws XPathExpressionException;
	//
	IStatus validateXPath(String xpathFactoryProviderId, String xpath);

	String getXPath(String xpath, String... args);

	//
	String computeBasicXPath(Node node, NamespaceInfos namespaceInfos);

}
