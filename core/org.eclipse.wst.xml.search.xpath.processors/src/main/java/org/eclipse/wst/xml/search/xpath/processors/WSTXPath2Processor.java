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
package org.eclipse.wst.xml.search.xpath.processors;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.search.core.xpath.AbstractXPathProcessor;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.xpath.core.util.NodeListImpl;
import org.eclipse.wst.xml.xpath2.processor.DefaultDynamicContext;
import org.eclipse.wst.xml.xpath2.processor.DefaultEvaluator;
import org.eclipse.wst.xml.xpath2.processor.DynamicContext;
import org.eclipse.wst.xml.xpath2.processor.Evaluator;
import org.eclipse.wst.xml.xpath2.processor.JFlexCupParser;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.StaticChecker;
import org.eclipse.wst.xml.xpath2.processor.StaticNameResolver;
import org.eclipse.wst.xml.xpath2.processor.XPathParser;
import org.eclipse.wst.xml.xpath2.processor.XPathParserException;
import org.eclipse.wst.xml.xpath2.processor.function.FnFunctionLibrary;
import org.eclipse.wst.xml.xpath2.processor.function.XSCtrLibrary;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WSTXPath2Processor extends AbstractXPathProcessor {

	public NodeList evaluateNodeSet(Object source, String xpath,
			NamespaceInfos namespaces, String... criteria)
			throws XPathExpressionException {
		ResultSequence rs = evaluate(source, xpath, namespaces, criteria);
		return new NodeListImpl(rs);
	}

	public String evaluateString(Object source, String xpath,
			NamespaceInfos namespaces, String... criteria)
			throws XPathExpressionException {
		ResultSequence rs = evaluate(source, xpath, namespaces, criteria);
		return rs.string();
	}

	private ResultSequence evaluate(Object source, String xp,
			NamespaceInfos namespaces, String... criteria)
			throws XPathExpressionException {

		Document doc = (Document) source;
		DynamicContext dc = new DefaultDynamicContext(null, doc);

		if (namespaces != null) {
			String prefix = null;
			// Add the defined namespaces
			for (NamespaceInfo namespaceinfo : namespaces) {
				prefix = namespaces.getPrefix(namespaceinfo);
				if (prefix != null && prefix.length() > 0) {
					// when prefix is empty and NamespaceInfo#locationHint is
					// not empty, the processor failed?
					dc.add_namespace(prefix, namespaceinfo.uri);
				}
			}
		}

		dc.add_function_library(new FnFunctionLibrary());
		dc.add_function_library(new XSCtrLibrary());

		XPathParser xpp = new JFlexCupParser();

		try {
			// Parses the XPath expression.
			org.eclipse.wst.xml.xpath2.processor.ast.XPath xpath = xpp
					.parse(xp);

			StaticChecker namecheck = new StaticNameResolver(dc);
			namecheck.check(xpath);

			// Static Checking the Xpath expression ’Hello World!’
			// namecheck.check(xp);
			/**
			 * Evaluate the XPath 2.0 expression
			 */

			// Initializing the evaluator with DynamicContext and the name
			// of the XML document XPexample.xml as parameters.
			Evaluator eval = new DefaultEvaluator(dc, doc);

			return eval.evaluate(xpath);
		} catch (Exception ex) {
			throw new XPathExpressionException(ex);
		}
	}

	public IStatus validateXPath(String xp) {
		XPathParser xpp = new JFlexCupParser();
		try {
			xpp.parse(xp);
		} catch (XPathParserException e) {
			return createStatusForXPathNotValid(xp, Activator.PLUGIN_ID, e);
		}
		return Status.OK_STATUS;
	}

}
