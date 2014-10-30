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
package org.eclipse.wst.xml.search.editor.searchers.expressions;

import junit.framework.TestCase;

import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;

public class MultiAttrValuesExpressionParserTestCase extends TestCase {

	private MultiAttrValuesExpressionParser parser;
	private IXMLReferenceToExpression toExpression;

	@Override
	protected void setUp() throws Exception {
		parser = new MultiAttrValuesExpressionParser();
		toExpression = XMLReferencesUtil.createXMLReferenceToExpression("", "",
				null, null, null, null, null, parser, null);
		super.setUp();
	}

	public void test1() throws Exception {
		SearcherToken token = parser.parse("a b c", "a", toExpression);
		assertEquals("a", token.getRealMatchingString());
		// assertEquals(0, token.getStartOffset());
		assertEquals("", token.getBeforeText());
	}

	public void test2() throws Exception {
		SearcherToken token = parser.parse("a b c", " a", toExpression);
		assertEquals(" a", token.getRealMatchingString());
		// assertEquals(0, token.getStartOffset());
		assertEquals("", token.getBeforeText());
	}

	public void test3() throws Exception {
		SearcherToken token = parser.parse("a b c", "a ", toExpression);
		assertEquals("", token.getRealMatchingString());
		// assertEquals(2, token.getStartOffset());
		assertEquals("a ", token.getBeforeText());
	}

	public void test4() throws Exception {
		SearcherToken token = parser.parse("a  b c", "a  ", toExpression);
		assertEquals(" ", token.getRealMatchingString());
		// assertEquals(2, token.getStartOffset());
		assertEquals("a ", token.getBeforeText());
	}

	public void test5() throws Exception {
		SearcherToken token = parser.parse("a b c", "a b", toExpression);
		assertEquals("b", token.getRealMatchingString());
		// assertEquals(2, token.getStartOffset());
		assertEquals("a ", token.getBeforeText());
	}

	public void test6() throws Exception {
		SearcherToken token = parser.parse("a b c", "a b ", toExpression);
		assertEquals("", token.getRealMatchingString());
		// assertEquals(4, token.getStartOffset());
		assertEquals("a b ", token.getBeforeText());
	}

	public void test7() throws Exception {
		SearcherToken token = parser.parse("a  b c", "a  b", toExpression);
		assertEquals(" b", token.getRealMatchingString());
		// assertEquals(2, token.getStartOffset());
		assertEquals("a ", token.getBeforeText());
	}

}
