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

import java.util.ArrayList;
import java.util.List;

class ResultToken {

	private String realMatchingString;
	private final List<String> tokens;
	private String endToken;
	private int startOffsetOfStartToken = -1;

	public ResultToken(char token, String nodeValue, String matchingString) {
		this(token, nodeValue, matchingString, -1);
	}

	public ResultToken(char token, String nodeValue, int offset) {
		this(token, nodeValue, null, offset);
	}

	public ResultToken(char token, String nodeValue) {
		this(token, nodeValue, null, -1);
	}

	private ResultToken(char token, String nodeValue, String matchingString,
			int offset) {
		tokens = new ArrayList<String>();
		if (matchingString == null && offset == -1) {
			parse(token, nodeValue, tokens);
		} else if (matchingString != null) {
			parse(token, nodeValue, matchingString, tokens);
		} else if (offset != -1) {
			parse(token, nodeValue, offset, tokens);
		}
	}

	private void parse(char token, String nodeValue, String matchingString,
			List<String> tokens) {
		char c = 'X';
		boolean isLastCharIsSeparator = false;
		StringBuilder currentMatch = new StringBuilder();
		char[] chars = matchingString.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			c = chars[i];
			if (c == token) {
				if (i == 0 || isLastCharIsSeparator) {
					currentMatch.append(c);
				} else {
					isLastCharIsSeparator = true;
					tokens.add(currentMatch.toString());
					currentMatch.setLength(0);
				}
			} else {
				currentMatch.append(c);
				isLastCharIsSeparator = false;
			}
		}
		if (c == token) {
			tokens.add("");
		} else if (currentMatch.length() > 0) {
			tokens.add(currentMatch.toString());
		}

		char[] nodeValueChars = nodeValue.toCharArray();
		int startChar = chars.length;
		if (startChar < nodeValueChars.length) {
			StringBuilder endTokenBuilder = new StringBuilder();
			for (int i = startChar; i < nodeValueChars.length; i++) {
				c = nodeValueChars[i];
				if (c == token) {
					break;
				} else {
					endTokenBuilder.append(c);
				}
			}
			endToken = endTokenBuilder.toString();
		}
		realMatchingString = currentMatch.toString();
		if (endToken == null) {
			endToken = "";
		}
	}

	private void parse(char token, String nodeValue, int offset,
			List<String> tokens) {
		this.startOffsetOfStartToken = offset;
		char c = 'X';
		boolean isLastCharIsSeparator = false;
		StringBuilder currentMatch = new StringBuilder();
		char[] nodeValueChars = nodeValue.toCharArray();
		int i = 0;
		for (i = 0; i < offset; i++) {
			c = nodeValueChars[i];
			if (c == token) {
				if (i == 0 || isLastCharIsSeparator) {
					currentMatch.append(c);
				} else {
					isLastCharIsSeparator = true;
					tokens.add(currentMatch.toString());
					currentMatch.setLength(0);
				}
			} else {
				currentMatch.append(c);
				isLastCharIsSeparator = false;
			}
		}
		if (c == token) {
			tokens.add("");
		} else if (currentMatch.length() > 0) {
			tokens.add(currentMatch.toString());
		}
		this.startOffsetOfStartToken = i - currentMatch.length();
		if (offset < nodeValueChars.length) {
			for (i = offset; i < nodeValueChars.length; i++) {
				c = nodeValueChars[i];
				if (c == token) {
					break;
				} else {
					currentMatch.append(c);
				}
			}
		}
		realMatchingString = currentMatch.toString();
	}

	private void parse(char token, String nodeValue, List<String> tokens) {
		char c;
		boolean isLastCharIsSeparator = false;
		StringBuilder currentMatch = new StringBuilder();
		char[] nodeValueChars = nodeValue.toCharArray();
		for (int i = 0; i < nodeValueChars.length; i++) {
			c = nodeValueChars[i];
			if (c == token) {
				if (i == 0 || isLastCharIsSeparator) {
					currentMatch.append(c);
				} else {
					isLastCharIsSeparator = true;
					tokens.add(currentMatch.toString());
					currentMatch.setLength(0);
				}
			} else {
				currentMatch.append(c);
				isLastCharIsSeparator = false;
			}
		}
		if (currentMatch.length() > 0) {
			tokens.add(currentMatch.toString());
		}
		
	}

	public String getRealMatchingString() {
		return realMatchingString;
	}

	public List<String> getTokens() {
		return tokens;
	}

	public String getEndToken() {
		return endToken;
	}

	public int getStartOffsetOfStartToken() {
		return startOffsetOfStartToken;
	}
}
