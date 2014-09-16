package org.eclipse.wst.xml.search.editor.core.searchers.expressions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.searchers.XMLValidationSearcherBindingsManager;
import org.eclipse.wst.xml.search.editor.core.validation.IMultiValidationResult;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.validation.MultiValidationResult;

public class XMLValidationSearcherForExpression implements IXMLValidationSearcher {

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
				v = getValidationSearcher(to).searchForValidation(selectedNode,
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

	private IXMLValidationSearcher getValidationSearcher(IXMLReferenceTo referenceTo) {
		return XMLValidationSearcherBindingsManager.getDefault().getValidationSearcher(referenceTo);
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
