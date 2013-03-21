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
package org.eclipse.wst.xml.search.core.util;

import junit.framework.TestCase;

public class StringUtilsTestCase extends TestCase {

	public void testSpaceNormalize() {
		String s1 = "\r\n         org.eclipse.wst.xml.search.core.util.StringUtils\r\n";
		String s2 = StringUtils.normalizeSpace(s1);
		assertEquals("org.eclipse.wst.xml.search.core.util.StringUtils", s2);
	}

	public void testSpaceNormalizeFirst() {
		String s1 = "\r\n         org.eclipse.wst.xml.search.core.util.StringUtils";
		String s2 = StringUtils.normalizeSpace(s1);
		assertEquals("org.eclipse.wst.xml.search.core.util.StringUtils", s2);
	}

	public void testSpaceNormalizeLast() {
		String s1 = "org.eclipse.wst.xml.search.core.util.StringUtils\r\n";
		String s2 = StringUtils.normalizeSpace(s1);
		assertEquals("org.eclipse.wst.xml.search.core.util.StringUtils", s2);
	}
	
	public void testSpaceNormalizeTab() {
		String s1 = "org.eclipse.wst.xml.search.core.util.StringUtils\r\n\t";
		String s2 = StringUtils.normalizeSpace(s1);
		assertEquals("org.eclipse.wst.xml.search.core.util.StringUtils", s2);
	}

	public void testTrimNormalizeBody() {
		String s1 = "org.eclipse.wst.xml.search.core.util.StringUtils\r\naaa";
		String s2 = StringUtils.normalizeSpace(s1);
		assertEquals("org.eclipse.wst.xml.search.core.util.StringUtils\r\naaa",
				s2);
	}
}
