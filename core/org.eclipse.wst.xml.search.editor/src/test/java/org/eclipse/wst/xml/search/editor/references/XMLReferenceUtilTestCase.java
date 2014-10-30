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
package org.eclipse.wst.xml.search.editor.references;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import junit.framework.TestCase;

import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.editor.DOMUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLReferenceUtilTestCase extends TestCase {

	private static String[] PIPELINES_CONTENT_TYPE = { "pipelinesContentType" };

	public void testNodeNull() throws Exception {
		IXMLReference reference = XMLReferencesUtil.getXMLReference(null,
				(String) null);
		assertNull(reference);
	}

	public void testOneAttrCondition() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/pipelines/pipeline/transformer[@type='pipeline']", "@src",
				null, null, PIPELINES_CONTENT_TYPE, null, null).getFrom();
		boolean match = referencePath.match(transformer);
		assertTrue(match);

	}

	public void testOneAttrNoMatchedCondition() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/pipelines/pipeline/transformer[@type='xslt']", "@src", null,
				null, PIPELINES_CONTENT_TYPE, null, null).getFrom();

		boolean match = referencePath.match(transformer);
		assertFalse(match);

	}

	public void testTwoAttrCondition() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/pipelines/pipeline/transformer[@type='pipeline'][@src='p1']",
				"@src", null, null, PIPELINES_CONTENT_TYPE, null, null)
				.getFrom();

		boolean match = referencePath.match(transformer);
		assertTrue(match);

	}

	public void testXMLReferenceInRegisrty() throws Exception {

		IXMLReference reference = XMLReferencesUtil.createXMLReference(
				"/pipelines/pipeline/transformer[@type='pipeline'][@src='p1']",
				"@src", null, null, PIPELINES_CONTENT_TYPE, null, null);

		XMLReferencesUtil.registerXMLReference(reference);

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));
		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReference referenceFromRegisrty = XMLReferencesUtil
				.getXMLReference(transformer.getAttributeNode("src"),
						PIPELINES_CONTENT_TYPE[0]);
		assertNotNull(referenceFromRegisrty);

	}

	public void testXMLReferenceInversed() throws Exception {

		IXMLReference reference = XMLReferencesUtil.createXMLReference(
				"/pipelines/pipeline/transformer[@type='pipeline']", "@src",
				null, null, PIPELINES_CONTENT_TYPE, null, null);
		IXMLReferencePath to = reference.createToXML("", null,
				"/pipelines/pipeline", "@uri", null, null, null, null, null);
		reference.addTo(to);

		XMLReferencesUtil.registerXMLReference(reference);

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));
		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//pipeline[@uri='p1']", null);
		Element pipelineP1 = (Element) list.item(0);

		Attr uriAttr = pipelineP1.getAttributeNode("uri");

		List<IXMLReference> referenceInversed = XMLReferencesUtil
				.getXMLReferenceInversed(uriAttr, PIPELINES_CONTENT_TYPE[0]);

		assertNotNull(referenceInversed);
		assertEquals(1, referenceInversed.size());

		IXMLReference reference2 = referenceInversed.get(0);
		IXMLReferencePath fromPath = reference2.getFrom();
		String xpath = fromPath.getQuery(uriAttr, null,
				EqualsStringQueryBuilder.INSTANCE);

		assertEquals(
				"/pipelines/pipeline/transformer[@type='pipeline'][@src=\"p1\"]/@src",
				xpath);
		// assertEquals("/pipelines/pipeline",
		// referenceInversed.get(0).getPath());

	}

	public void testOneAttrAndAnyPathCondition() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/pipelines//transformer[@type='pipeline']", "@src", null,
				null, PIPELINES_CONTENT_TYPE, null, null).getFrom();
		boolean match = referencePath.match(transformer);
		assertTrue(match);

	}

	public void testOneAttrAndAnyPathConditionNotMatched() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/AAAAAAAAAAAAAAAA//transformer[@type='pipeline']", "@src",
				null, null, PIPELINES_CONTENT_TYPE, null, null).getFrom();
		boolean match = referencePath.match(transformer);
		assertFalse(match);

	}

	public void testOneAttrAndFullAnyPathCondition() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"//transformer[@type='pipeline']", "@src", null, null,
				PIPELINES_CONTENT_TYPE, null, null).getFrom();
		boolean match = referencePath.match(transformer);
		assertTrue(match);

	}

	public void testWildCardValue() throws Exception {

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("struts.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/struts/package/action[@name='hello']", null);
		Element action = (Element) list.item(0);

		IXMLReferencePath referencePath = XMLReferencesUtil.createXMLReference(
				"/struts/package/action[@name='$0']", "@method", null, null,
				PIPELINES_CONTENT_TYPE, null, null).getFrom();

		boolean match = referencePath.match(action);
		assertTrue(match);
		List<String> wildCardValues = referencePath.getWildcardValues(action);
		assertEquals(1, wildCardValues.size());
		assertEquals("hello", wildCardValues.get(0));
	}

	public void testExtractJavaMethodForStruts2() throws Exception {

		IXMLReference reference = XMLReferencesUtil.createXMLReference(
				"/struts/package/action", "@method", null, null,
				PIPELINES_CONTENT_TYPE, null, null);
		IXMLReferenceToJavaMethod toJavaMethod = reference.createToJavaMethod(
				"", null, null, "/pipelines/pipeline", "@class", null);
		reference.addTo(toJavaMethod);

		XMLReferencesUtil.registerXMLReference(reference);

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("struts.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/struts/package/action[@name='hello']", null);
		Element action = (Element) list.item(0);

		XPathExpressionException ex = null;
		String className = null;
		try {
			className = toJavaMethod.extractClassName(action, null, null, null);
		} catch (XPathExpressionException e) {
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals("org.apache.struts.helloworld.action.HelloWorldAction",
				className);
	}

	public void testExtractJavaMethodForJetty() throws Exception {

		IXMLReference reference = XMLReferencesUtil.createXMLReference(
				"/struts/package/action", "@method", null, null,
				PIPELINES_CONTENT_TYPE, null, null);
		IXMLReferenceToJavaMethod toJavaMethod = reference.createToJavaMethod(
				"", null, null, "/pipelines/pipeline", "@class", null);
		reference.addTo(toJavaMethod);

		XMLReferencesUtil.registerXMLReference(reference);

		Document document = DOMUtils.load(XMLReferenceUtilTestCase.class
				.getResourceAsStream("struts.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/struts/package/action[@name='hello']", null);
		Element action = (Element) list.item(0);

		XPathExpressionException ex = null;
		String className = null;
		try {
			className = toJavaMethod.extractClassName(action, null, null, null);
		} catch (XPathExpressionException e) {
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals("org.apache.struts.helloworld.action.HelloWorldAction",
				className);
	}
}
