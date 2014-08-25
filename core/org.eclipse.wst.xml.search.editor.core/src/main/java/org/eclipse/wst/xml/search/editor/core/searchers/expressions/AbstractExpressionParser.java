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
package org.eclipse.wst.xml.search.editor.core.searchers.expressions;

import java.util.List;

import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.w3c.dom.Node;

public abstract class AbstractExpressionParser implements IXMLExpressionParser {

	public static final String ALL_TOKEN = "___ALL_TOKEN";

	protected SearcherToken createSearcherToken(
			IXMLReferenceToExpression toExpression, String tokenId,
			String realMatchingString, String beforeText, String endText) {
		SearcherToken searcherToken = new SearcherToken(tokenId,
				realMatchingString.toString(), beforeText, endText);
		populateTos(toExpression, tokenId, searcherToken);
		return searcherToken;
	}

	protected SearcherToken createSearcherToken(
			IXMLReferenceToExpression toExpression, String tokenId,
			String realMatchingString, int startOffsetOfStartToken) {
		SearcherToken searcherToken = new SearcherToken(tokenId,
				realMatchingString.toString(), startOffsetOfStartToken);
		populateTos(toExpression, tokenId, searcherToken);
		return searcherToken;
	}
	
	protected void populateTos(IXMLReferenceToExpression toExpression,
			String tokenId, SearcherToken searcherToken) {
		if (tokenId != null) {
			List<IXMLReferenceTo> tos = null;
			if (ALL_TOKEN.equals(tokenId)) {
				tos = toExpression.getTo();

			} else {
				tos = toExpression.getTo(tokenId);
			}
			if (tos != null) {
				for (IXMLReferenceTo to : tos) {
					searcherToken.getTos().add(to);
				}
			}
		}
	}

	protected String getTokenId(Node selectedNode,
			IXMLReferenceToExpression toExpression, String matchingString,
			int offset) {
		return ALL_TOKEN;
	}
}
