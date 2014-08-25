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
package org.eclipse.wst.xml.search.editor.core.references.validators;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.searchers.XMLValidationSearcherBindingsManager;
import org.eclipse.wst.xml.search.editor.core.validation.IMultiValidationResult;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.validation.LocalizedMessage;
import org.eclipse.wst.xml.search.editor.core.validation.ValidatorUtils;
import org.w3c.dom.Node;

public class DefaultDOMNodeValidator implements IXMLReferenceValidator {

	public static final IXMLReferenceValidator INSTANCE = new DefaultDOMNodeValidator();

	public LocalizedMessage validate(IXMLReference reference, IDOMNode node, IFile file, IValidator validator) {
		if (reference != null && reference.getTo().size() > 0) {
			return doValidate(reference, node, file, validator );
		}
		return null;
	}

	private int getStartOffset(IDOMNode node) {
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ((IDOMAttr) node).getValueRegionStartOffset();
		}
		return node.getStartOffset();
	}

	protected int getSeverity(IXMLReference reference, int nbElements,
			IFile file, IDOMNode node) {
		return ValidatorUtils.getSeverity(reference, nbElements);
	}

	protected String getMessageText(IXMLReference reference, int nbElements,
			String textContent, Node node, IFile file) {
		if (textContent == null) {
			return null;
		}
		return ValidatorUtils
				.getMessageText(reference, nbElements, textContent, node, file);
	}
	
	protected String getMessageText(IXMLReference reference, int nbElements,
			Node node, String textContent) {
		if (textContent == null) {
			return null;
		}
		return ValidatorUtils
				.getMessageText(reference, nbElements, textContent);
	}

	protected LocalizedMessage createMessage(int start, int length,
			String messageText, int severity,
			IStructuredDocument structuredDocument) {
		return ValidatorUtils.createMessage(start, length, messageText,
				severity, structuredDocument);
	}

	protected LocalizedMessage doValidate(IXMLReference reference, IDOMNode node, IFile file, IValidator validator) {
		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLValidationSearcher searcher = XMLValidationSearcherBindingsManager.
			                getDefault().getValidationSearcher( reference );
			if (searcher != null) {
				try {
					IValidationResult result = searcher.searchForValidation(
							node, DOMUtils.getNodeValue(node), -1, -1, file,
							expression);
					if (result != null) {
						IMultiValidationResult multiResult = (IMultiValidationResult) result;
						List<IValidationResult> result2 = multiResult
								.getResults();
						for (IValidationResult r : result2) {
							int nbElements = r.getNbElements();
							if (nbElements != 1) {
								String messageText = getMessageText(reference,
										nbElements, r.getValue(), node, file);
								if (messageText != null) {
									int severity = getSeverity(reference,
											nbElements, file, node);
									int startOffset = getStartOffset(node)
											+ r.getStartIndex() + 1;
									int length = r.getValue().length();
									return createMessage(startOffset, length,messageText, severity, node.getStructuredDocument());
								}
							}
						}
					}
				} catch (Throwable e) {
					Trace.trace(Trace.SEVERE, e.getMessage(), e);
				}
			}
		}

		int nonNegativeElements = 0;
		for (IXMLReferenceTo referenceTo : reference.getTo()) {
			IXMLValidationSearcher searcher = XMLValidationSearcherBindingsManager
			                .getDefault().getValidationSearcher(referenceTo);
			if (searcher != null) {
				try {
					IValidationResult result = searcher.searchForValidation(
							node, DOMUtils.getNodeValue(node), -1, -1, file, referenceTo);
					if (result != null) {
						if (result.getNbElements() < 0) {
							String textContent = DOMUtils.getNodeValue(node);
							String messageText = getMessageText(reference,
									result.getNbElements(), textContent, node, file);
							if (messageText != null) {
								int severity = getSeverity(reference, result.getNbElements(), file, node);
								int startOffset = getStartOffset(node);
								int length = textContent.trim().length() + 2;
								return createMessage(startOffset, length,messageText, severity, node.getStructuredDocument());
							}
						} else if (result.getNbElements() > 0) {
							nonNegativeElements += result.getNbElements();
						}
					}
				} catch (Throwable e) {
					Trace.trace(Trace.SEVERE, e.getMessage(), e);
				}
			}
		}

		if (nonNegativeElements != 1) {
			String textContent = DOMUtils.getNodeValue(node);
			String messageText = getMessageText(reference, nonNegativeElements, textContent, node, file);
			if (messageText != null) {
				int severity = getSeverity(reference, nonNegativeElements, file, node);
				int startOffset = getStartOffset(node);
				int length = textContent.trim().length() + 2;
				return createMessage(startOffset, length,messageText, severity, node.getStructuredDocument());
			}
		}
		return null;
	}
}
