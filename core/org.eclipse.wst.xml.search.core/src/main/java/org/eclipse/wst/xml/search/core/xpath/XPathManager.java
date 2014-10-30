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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceTable;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * XPath manager used to cache XPath expression.
 * 
 */
public class XPathManager implements IXPathManager {

	private static final IXPathManager INSTANCE = new XPathManager();

	private Map<String, NamespaceInfos> namespaceInfo = new ConcurrentHashMap<String, NamespaceInfos>();

	public static IXPathManager getManager() {
		return INSTANCE;
	}

	public NodeList evaluateNodeSet(String xpathFactoryProviderId,
			Object source, String xpath, NamespaceInfos namespaceInfo,
			String... criteria) throws XPathExpressionException {
		return getProcessor(xpathFactoryProviderId).evaluateNodeSet(source,
				xpath, namespaceInfo, criteria);
	}

	// public Node evaluateNode(String xpathFactoryProviderId, Object source,
	// String xpath, String... criteria) throws XPathExpressionException {
	// return getManager(xpathFactoryProviderId).evaluateNode(source, xpath,
	// criteria);
	// }
	//
	// public Boolean evaluateBoolean(String xpathFactoryProviderId,
	// Object source, String xpath, String... criteria)
	// throws XPathExpressionException {
	// return getManager(xpathFactoryProviderId).evaluateBoolean(source,
	// xpath, criteria);
	// }
	//
	// public Number evaluateNumber(String xpathFactoryProviderId, Object
	// source,
	// String xpath, String... criteria) throws XPathExpressionException {
	// return getManager(xpathFactoryProviderId).evaluateNumber(source, xpath,
	// criteria);
	// }
	//
	public String evaluateString(String xpathFactoryProviderId, Object source,
			String xpath, NamespaceInfos namespaceInfo, String... criteria)
			throws XPathExpressionException {
		return getProcessor(xpathFactoryProviderId).evaluateString(source,
				xpath, namespaceInfo, criteria);
	}

	//
	// public XPathExpression getXPathExpression(String xpathFactoryProviderId,
	// String xpath, String... args) throws XPathExpressionException {
	// return getManager(xpathFactoryProviderId).getXPathExpression(xpath,
	// args);
	// }
	//
	// public XPathExpression createXPathExpression(String
	// xpathFactoryProviderId,
	// String expression) throws XPathExpressionException {
	// return getManager(xpathFactoryProviderId).createXPathExpression(
	// expression);
	// }

	public String getXPath(String xpath, String... args) {
		if (args == null || args.length < 1) {
			return xpath;
		}
		return MessageFormat.format(xpath, (Object[]) args);
	}

	public String computeBasicXPath(Node node, NamespaceInfos namespaceInfos) {
		if (node == null) {
			return null;
		}
		StringBuilder xpath = new StringBuilder();
		computeBasicXPath(node, xpath, namespaceInfos);
		return xpath.toString();
	}

	private Node computeBasicXPath(Node node, StringBuilder xpath,
			NamespaceInfos namespaceInfos) {
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			computeBasicXPath((Attr) node, xpath, namespaceInfos);
			return null;
		case Node.TEXT_NODE:
			computeBasicXPath((Text) node, xpath, namespaceInfos);
			return null;
		case Node.ELEMENT_NODE:
			return computeBasicXPath((Element) node, xpath, namespaceInfos);
		}
		return null;
	}

	private Node computeBasicXPath(Element element, StringBuilder xpath,
			NamespaceInfos namespaceInfos) {
		xpath.insert(0, element.getLocalName());
		if (!StringUtils.isEmpty(element.getNamespaceURI())
				&& namespaceInfos != null) {
			String prefix = namespaceInfos.getPrefix(element.getNamespaceURI());
			if (prefix != null) {
				xpath.insert(0, ":");
				xpath.insert(0, prefix);
			}
		}
		xpath.insert(0, "/");
		Node parentNode = element;
		while (parentNode != null
				&& parentNode.getNodeType() != Node.DOCUMENT_NODE) {
			parentNode = parentNode.getParentNode();
			parentNode = computeBasicXPath(parentNode, xpath, namespaceInfos);
		}
		return parentNode;
	}

	private void computeBasicXPath(Attr attr, StringBuilder xpath,
			NamespaceInfos namespaceInfos) {
		computeBasicXPath(attr.getOwnerElement(), xpath, namespaceInfos);
		xpath.append("[@");
		xpath.append(attr.getName());
		xpath.append("=");
		addXPathConditionValue(attr.getValue(), xpath);
		xpath.append("]");
		xpath.append("/@");
		xpath.append(attr.getName());
	}

	private void computeBasicXPath(Text text, StringBuilder xpath,
			NamespaceInfos namespaceInfos) {
		computeBasicXPath(text.getParentNode(), xpath, namespaceInfos);
		xpath.append("[text()=");
		addXPathConditionValue(text.getData(), xpath);
		xpath.append("]");
		xpath.append("/text()");
	}

	private void addXPathConditionValue(String value, StringBuilder xpath) {
		char c = getEnclosingXPathCondition(value);
		xpath.append(c);
		xpath.append(value);
		xpath.append(c);

	}

	private char getEnclosingXPathCondition(String value) {
		if (value.indexOf("\"") != -1) {
			return '\'';
		}
		return '\"';
	}

	private IXPathProcessor getProcessor(String id) {
		if (id == null) {
			return DefaultXPathProcessor.INSTANCE;
		}
		IXPathProcessorType processorType = XPathProcessorManager.getDefault()
				.getProcessor(id);
		if (processorType == null) {
			throw new RuntimeException(
					"Cannot retrieve XPath processor type with id=" + id);
		}
		IXPathProcessor processor = processorType.getProcessor();
		if (processor == null) {
			throw new RuntimeException(
					"Null XPath processor for XPath processor with id=" + id);
		}
		return processor;
	}

	// private synchronized XPathManagerForXPathProcessor registerManager(String
	// id) {
	// XPathManagerForXPathProcessor manager = managers.get(id);
	// if (manager != null) {
	// return manager;
	// }
	// IXPathProcessorType processor = XPathProcessorManager.getDefault()
	// .getProcessor(id);
	// if (processor == null) {
	// throw new RuntimeException(
	// "Cannot retrieve XPath processor with id=" + id);
	// }
	// manager = new XPathManagerForXPathProcessor(processor.getProcessor());
	// managers.put(id, manager);
	// return manager;
	// }

	public IStatus validateXPath(String xpathFactoryProviderId, String xpath) {
		IXPathProcessor processor = null;
		try {
			processor = getProcessor(xpathFactoryProviderId);
		} catch (Throwable e) {
			return XMLSearchCorePlugin.createStatus(IStatus.ERROR, "", e);
		}
		return processor.validateXPath(xpath);
	}

	// public String escapeForXPathAttrCondition(String value,
	// boolean enclosedByDoubleQuote) {
	// if (value == null) {
	// return "";
	// }
	// if (enclosedByDoubleQuote) {
	// if (value.indexOf("\"") != -1) {
	// return value.replaceAll("\"", "&quot;");
	// }
	// } else {
	// if (value.indexOf("'") != -1) {
	// return value.replaceAll("\'", "&pos;");
	// }
	// }
	// return value;
	// }

	public NamespaceInfos getNamespaceInfo(Node node) {
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			return getNamespaceInfo((IDOMDocument) node);
		}
		return getNamespaceInfo((IDOMDocument) node.getOwnerDocument());
	}

	public NamespaceInfos getNamespaceInfo(IDOMDocument document) {
		String modelID = document.getModel().getId();
		Map<String, NamespaceInfos> namespaceInfo = getNamespaceInfo();
		NamespaceInfos info = namespaceInfo.get(modelID);

		if (info == null) {
			if (document.getDocumentElement() != null) {
				info = new NamespaceInfos();
				NamespaceTable namespaceTable = new NamespaceTable(document);
				namespaceTable.visitElement(document.getDocumentElement());
				Collection<?> namespaces = namespaceTable
						.getNamespaceInfoCollection();
				info.addAll((Collection<NamespaceInfo>) namespaces);
				namespaceInfo.put(modelID, info);
				setNamespaceInfo(namespaceInfo);
			}
		}
		return info;
	}

	public void setNamespaceInfo(Map<String, NamespaceInfos> namespaceInfo) {
		this.namespaceInfo = namespaceInfo;
	}

	public Map<String, NamespaceInfos> getNamespaceInfo() {
		return namespaceInfo;
	}
}
