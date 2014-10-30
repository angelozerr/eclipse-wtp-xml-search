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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;

public class BaseTokenExpressionParser extends AbstractExpressionParser {

	private final char token;

	public BaseTokenExpressionParser(char token) {
		this.token = token;
	}

	public SearcherToken parse(String nodeValue, String matchingString,
			IXMLReferenceToExpression toExpression) {
		ResultToken resultToken = new ResultToken(token, nodeValue,
				matchingString);
		String realMatchingString = resultToken.getRealMatchingString();
		List<String> tokens = resultToken.getTokens();
		String tokenId = getTokenId(tokens, getTokenIndex(tokens));
		String beforeText = getBeforeText(nodeValue, matchingString,
				realMatchingString, tokenId, tokens);
		String endText = getEndText(nodeValue, resultToken.getEndToken(),
				matchingString, tokenId, tokens);
		return createSearcherToken(toExpression, tokenId, realMatchingString,
				beforeText, endText);
	}

	protected String getTokenId(List<String> tokens, int tokenIndex) {
		return ALL_TOKEN;
	}

	protected String getEndText(String nodeValue, String endToken,
			String matchingString, String tokenId, List<String> tokens) {
		String endText = nodeValue
				.substring(matchingString.length() + endToken.length(),
						nodeValue.length());
		if (endText.length() > 0) {
			char firstChar = endText.charAt(0);
			if (firstChar != token) {
				if (addTokenAtFirst(tokenId, tokens)) {
					return token + endText;
				}
			}
		}
		return endText;
	}

	protected boolean addTokenAtFirst(String tokenId, List<String> tokens) {
		return true;
	}

	protected int getTokenIndex(List<String> tokens) {
		return tokens.size() > 0 ? tokens.size() - 1 : 0;
	}

	protected String getBeforeText(String nodeValue, String matchingString,
			String realMatchingString, String tokenId, List<String> tokens) {
		return matchingString.substring(0, matchingString.length()
				- realMatchingString.length());
	}

	public SearcherToken[] parse(String nodeValue,
			IXMLReferenceToExpression toExpression) {

		ResultToken resultToken = new ResultToken(token, nodeValue);
		List<String> tokens = resultToken.getTokens();
		String tokenId = null;

		List<SearcherToken> searcherTokens = new ArrayList<SearcherToken>();
		int startOffsetOfStartToken = 0;
		String lastToken = null;
		int i = 0;
		for (String token : tokens) {
			if (lastToken != null) {
				startOffsetOfStartToken++;
				startOffsetOfStartToken += lastToken.length();
			}
			lastToken = token;
			tokenId = getTokenId(tokens, i++);
			searcherTokens.add(createSearcherToken(toExpression, tokenId,
					token, startOffsetOfStartToken));
		}
		return searcherTokens.toArray(SearcherToken.EMPTY);
	}

	public SearcherToken parse(String nodeValue, int offset,
			IXMLReferenceToExpression toExpression) {

		ResultToken resultToken = new ResultToken(token, nodeValue, offset);
		String realMatchingString = resultToken.getRealMatchingString();
		List<String> tokens = resultToken.getTokens();
		String tokenId = getTokenId(tokens, getTokenIndex(tokens));
		return createSearcherToken(toExpression, tokenId, realMatchingString,
				resultToken.getStartOffsetOfStartToken());
	}

}
