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
package org.eclipse.wst.xml.search.editor.searchers.expressions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;

public class SearcherToken {

	// private final String tokenId;

	public static final SearcherToken[] EMPTY = new SearcherToken[0];

	private final String tokenId;
	private final int startOffset;
	private final int endOffset;
	private final String realMatchingString;
	private final String beforeText;
	private final String endText;
	private final List<IXMLReferenceTo> tos;

	public SearcherToken(String tokenId, String realMatchingString,
			String beforeText, String endText) {
		this.tokenId = tokenId;
		this.realMatchingString = realMatchingString;
		this.beforeText = beforeText;
		this.endText = endText;
		this.tos = new ArrayList<IXMLReferenceTo>();
		this.startOffset = beforeText.length();
		this.endOffset = startOffset + realMatchingString.length();
	}

	public SearcherToken(String tokenId, String realMatchingString,
			int startOffsetOfStartToken) {
		this.tokenId = tokenId;
		this.realMatchingString = realMatchingString;
		this.beforeText = null;
		this.endText = null;
		this.tos = new ArrayList<IXMLReferenceTo>();
		this.startOffset = startOffsetOfStartToken;
		this.endOffset = startOffset + realMatchingString.length();
	}

	public String getTokenId() {
		return tokenId;
	}

	public List<IXMLReferenceTo> getTos() {
		return tos;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public String getRealMatchingString() {
		return realMatchingString;
	}

	public String getBeforeText() {
		return beforeText;
	}

	public String getEndText() {
		return endText;
	}

}
