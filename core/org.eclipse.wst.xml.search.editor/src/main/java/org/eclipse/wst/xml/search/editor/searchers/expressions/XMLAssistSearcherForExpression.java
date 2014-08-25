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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.IXMLExpressionParser;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.SearcherToken;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.searchers.XMLAssistSearcherBindingsManager;

public class XMLAssistSearcherForExpression implements IXMLAssistSearcher {

	public static final IXMLAssistSearcher INSTANCE = new XMLAssistSearcherForExpression();

	public void searchForCompletion(Object selectedNode, String mathingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		IXMLReferenceToExpression toExpression = (IXMLReferenceToExpression) referenceTo;

		SearcherToken token = getToken(selectedNode, mathingString,
				toExpression);

		if (token != null) {
			List<IXMLReferenceTo> tos = token.getTos();
			for (IXMLReferenceTo to : tos) {
				getAssistSearcher(to).searchForCompletion(selectedNode,
						token.getRealMatchingString(), token.getBeforeText(),
						token.getEndText(), file, to, recorder);
			}
		}
	}

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IXMLReferenceToExpression toExpression = (IXMLReferenceToExpression) referenceTo;

		SearcherToken token = getToken(selectedNode, mathingString, offset,
				toExpression);

		if (token != null) {
			List<IXMLReferenceTo> tos = token.getTos();
			for (IXMLReferenceTo to : tos) {
				getAssistSearcher(to).searchForHyperlink(selectedNode, offset,
						token.getRealMatchingString(), token.getStartOffset(),
						token.getEndOffset(), file, to, hyperlinkRegion,
						hyperLinks, textEditor);
			}
		}
	}

	

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToExpression toExpression = (IXMLReferenceToExpression) referenceTo;
		SearcherToken[] tokens = getTokens(selectedNode, mathingString,
				toExpression);
		if (tokens == null) {
			return null;
		}
		StringBuilder textHover = null;
		SearcherToken token = null;
		for (int i = 0; i < tokens.length; i++) {
			token = tokens[i];
			List<IXMLReferenceTo> tos = token.getTos();
			for (IXMLReferenceTo to : tos) {
				String s = getAssistSearcher(to).searchForTextHover(selectedNode,
						offset, token.getRealMatchingString(),
						token.getStartOffset(), token.getEndOffset(), file, to);
				if (s != null) {
					if (textHover == null) {
						textHover = new StringBuilder();
						textHover.append(s);
					} else {
						textHover.append("<br>");
						textHover.append("<br>");
						textHover.append(s);
					}
				}
			}

		}
		return textHover != null ? textHover.toString() : null;
	}

	private IXMLAssistSearcher getAssistSearcher(IXMLReferenceTo referenceTo) {
		return XMLAssistSearcherBindingsManager.getDefault().getXMLAssistSearcher(referenceTo);
	}
	
	protected SearcherToken getToken(Object selectedNode, String mathingString,
			IXMLReferenceToExpression toExpression) {
		IXMLExpressionParser parser = toExpression.getParser();
		if (parser == null) {
			return null;
		}
		return parser.parse(DOMUtils.getNodeValue((IDOMNode) selectedNode),
				mathingString, toExpression);
	}

	protected SearcherToken getToken(Object selectedNode, String mathingString,
			int offset, IXMLReferenceToExpression toExpression) {
		IXMLExpressionParser parser = toExpression.getParser();
		if (parser == null) {
			return null;
		}
		return parser.parse(mathingString, offset, toExpression);
	}

	protected SearcherToken[] getTokens(Object selectedNode,
			String matchingString, IXMLReferenceToExpression toExpression) {
		IXMLExpressionParser parser = toExpression.getParser();
		if (parser == null) {
			return null;
		}
		SearcherToken[] tokens = parser.parse(matchingString, toExpression);
		return tokens;
	}
}
