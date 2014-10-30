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
package org.eclipse.wst.xml.search.core.xpath.matcher.ns;

import junit.framework.TestCase;

import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.search.core.DOMUtils;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.core.xpath.matcher.XPathMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XPathMatcherNSTestCase extends TestCase {

	public void testWeb25() throws Exception {
		Document document = DOMUtils.load(XPathMatcherNSTestCase.class
				.getResourceAsStream("web2.5.xml"));
		document.getNamespaceURI();
		
		NamespaceInfos infos = new NamespaceInfos();
		infos.add(new NamespaceInfo("http://java.sun.com/xml/ns/javaee", "ns", "")); 
		
		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/ns:web-app/ns:servlet", infos);
		Element servlet = (Element) list.item(0);
		assertNotNull(servlet);
		
		XPathMatcher matcher = new XPathMatcher(
				"/ns:web-app/ns:servlet");
		boolean match = matcher.match(servlet);
		assertTrue(match);
	}
	
	public void testWeb25NS() throws Exception {
		Document document = DOMUtils.load(XPathMatcherNSTestCase.class
				.getResourceAsStream("web2.5_ns.xml"));
		document.getNamespaceURI();
		
		NamespaceInfos infos = new NamespaceInfos();
		infos.add(new NamespaceInfo("http://java.sun.com/xml/ns/javaee", "ns", "")); 
		
		NodeList list = XPathManager.getManager().evaluateNodeSet(null,
				document, "/ns:web-app/ns:servlet", infos);
		Element servlet = (Element) list.item(0);
		assertNotNull(servlet);
		
		XPathMatcher matcher = new XPathMatcher(
				"/ns:web-app/ns:servlet");
		boolean match = matcher.match(servlet);
		assertTrue(match);
	}
}
