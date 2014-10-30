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
package org.eclipse.wst.xml.search.core.xpath.matcher;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.wst.xml.search.core.DOMUtils;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XPathMatcherTestCase extends TestCase {

	public void testOneAttrCondition() throws Exception {
		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);
		XPathMatcher matcher = new XPathMatcher(
				"/pipelines/pipeline/transformer[@type='pipeline']");
		boolean match = matcher.match(transformer);
		assertTrue(match);

	}

	public void testOneAttrNoMatchedCondition() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"/pipelines/pipeline/transformer[@type='xslt']");
		boolean match = matcher.match(transformer);
		assertFalse(match);

	}

	public void testTwoAttrCondition() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"/pipelines/pipeline/transformer[@type='pipeline'][@src='p1']");
		boolean match = matcher.match(transformer);
		assertTrue(match);

	}

	public void testOneAttrAndAnyPathCondition() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"/pipelines//transformer[@type='pipeline']");
		boolean match = matcher.match(transformer);
		assertTrue(match);

	}

	public void testOneAttrAndAnyPathConditionNotMatched() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"/AAAAAAAAAAAAAAAA//transformer[@type='pipeline']");
		boolean match = matcher.match(transformer);
		assertFalse(match);

	}

	public void testOneAttrAndFullAnyPathCondition() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("pipelines.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "//transformer[@type='pipeline']", null);
		Element transformer = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"//transformer[@type='pipeline']");
		boolean match = matcher.match(transformer);
		assertTrue(match);

	}

	public void testWildCardValue() throws Exception {

		Document document = DOMUtils.load(XPathMatcherTestCase.class
				.getResourceAsStream("struts.xml"));

		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/struts/package/action[@name='hello']", null);
		Element action = (Element) list.item(0);

		XPathMatcher matcher = new XPathMatcher(
				"/struts/package/action[@name='$0']");

		List<String> wildCardValues = new ArrayList<String>();
		boolean match = matcher.match(action, wildCardValues);
		assertTrue(match);
		assertEquals(1, wildCardValues.size());
		assertEquals("hello", wildCardValues.get(0));
	}
	
}
