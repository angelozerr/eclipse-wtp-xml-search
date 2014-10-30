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
package org.eclipse.wst.xml.search.core.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractXPathProcessorForXPathFactory extends
		AbstractXPathProcessor {

	/**
	 * XPath expressions cache
	 */
	private Map<String, XPathExpression> expressions = new HashMap<String, XPathExpression>();

	private XPathFactory factory;

	public NodeList evaluateNodeSet(Object source, String xpath,
			NamespaceInfos namespaceInfo, String... criteria)
			throws XPathExpressionException {
		return (NodeList) evaluate(source, XPathConstants.NODESET, xpath,
				namespaceInfo, criteria);
	}

	public String evaluateString(Object source, String xpath,
			NamespaceInfos namespaceInfo, String... criteria) throws XPathExpressionException {
		return (String) evaluate(source, XPathConstants.STRING, xpath,
				namespaceInfo, criteria);
	}
//	public Node evaluateNode(Object source, String xpath, String... criteria)
//			throws XPathExpressionException {
//		return (Node) evaluate(source, XPathConstants.NODE, xpath, criteria);
//	}
//
//	public Boolean evaluateBoolean(Object source, String xpath,
//			String... criteria) throws XPathExpressionException {
//		return (Boolean) evaluate(source, XPathConstants.BOOLEAN, xpath,
//				criteria);
//	}
//
//	public Number evaluateNumber(Object source, String xpath,
//			String... criteria) throws XPathExpressionException {
//		return (Number) evaluate(source, XPathConstants.NUMBER, xpath, criteria);
//	}
//
//	public String evaluateString(Object source, String xpath,
//			NamespaceInfos namespaceInfo, String... criteria)
//			throws XPathExpressionException {
//		return (String) evaluate(source, XPathConstants.STRING, xpath,
//				namespaceInfo, criteria);
//	}

	public Object evaluate(Object source, QName name, String xpath,
			NamespaceInfos namespaceInfo, String... criteria)
			throws XPathExpressionException {
		XPathExpression expr = getXPathExpression(xpath, namespaceInfo,
				criteria);
		return expr.evaluate(source, name);
	}

	protected XPathExpression getXPathExpression(String xpath,
			NamespaceInfos namespaceInfo, String... args)
			throws XPathExpressionException {
		xpath = computeXPath(xpath, args);
		XPathExpression exp = expressions.get(xpath);
		if (exp == null) {
			exp = createXPathExpression(xpath, namespaceInfo);
			// expressions.put(xpath, exp);
		}
		return exp;
	}

	protected XPathExpression createXPathExpression(String expression,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		XPath xpath = getFactory().newXPath();
		if (namespaceInfo != null && namespaceInfo.size() > 0) {
			xpath.setNamespaceContext(namespaceInfo);
		}
		XPathExpression expr = xpath.compile(expression);
		return expr;
	}

	public IStatus validateXPath(String xpath) {
		try {
			createXPathExpression(xpath, null);
		} catch (XPathExpressionException e) {
			return createStatusForXPathNotValid(xpath,
					XMLSearchCorePlugin.PLUGIN_ID, e);
		}
		return Status.OK_STATUS;
	}

	public XPathFactory getFactory() {
		if (factory == null) {
			factory = createFactory();
		}
		return factory;
	}

	protected abstract XPathFactory createFactory();
}
