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
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.validation.IMultiValidationResult;
import org.eclipse.wst.xml.search.editor.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.validation.MultiValidationResult;

public class XMLSearcherForExpression implements IXMLSearcher {

	public static final IXMLSearcher INSTANCE = new XMLSearcherForExpression();

	public void searchForCompletion(Object selectedNode, String mathingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		IXMLReferenceToExpression toExpression = (IXMLReferenceToExpression) referenceTo;

		SearcherToken token = getToken(selectedNode, mathingString,
				toExpression);

		if (token != null) {
			List<IXMLReferenceTo> tos = token.getTos();
			for (IXMLReferenceTo to : tos) {
				to.getSearcher().searchForCompletion(selectedNode,
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
				to.getSearcher().searchForHyperlink(selectedNode, offset,
						token.getRealMatchingString(), token.getStartOffset(),
						token.getEndOffset(), file, to, hyperlinkRegion,
						hyperLinks, textEditor);
			}
		}
	}

	public IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToExpression toExpression = (IXMLReferenceToExpression) referenceTo;
		SearcherToken[] tokens = getTokens(selectedNode, mathingString,
				toExpression);
		if (tokens == null) {
			return null;
		}

		IValidationResult v = null;
		int nbElements = 0;
		IMultiValidationResult result = new MultiValidationResult();
		SearcherToken token = null;
		for (int i = 0; i < tokens.length; i++) {
			token = tokens[i];
			nbElements = 0;
			v = null;
			List<IXMLReferenceTo> tos = token.getTos();
			for (IXMLReferenceTo to : tos) {
				v = to.getSearcher().searchForValidation(selectedNode,
						token.getRealMatchingString(), token.getStartOffset(),
						token.getEndOffset(), file, to);
				nbElements = v.getNbElements() + nbElements;
			}
			if (nbElements != 1 && v != null) {
				v.setNbElements(nbElements);
				result.add(v);
			}
		}
		return result;
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
				String s = to.getSearcher().searchForTextHover(selectedNode,
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

	private SearcherToken getToken(Object selectedNode, String mathingString,
			IXMLReferenceToExpression toExpression) {
		IXMLExpressionParser parser = toExpression.getParser();
		if (parser == null) {
			return null;
		}
		return parser.parse(DOMUtils.getNodeValue((IDOMNode) selectedNode),
				mathingString, toExpression);
	}

	private SearcherToken getToken(Object selectedNode, String mathingString,
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
