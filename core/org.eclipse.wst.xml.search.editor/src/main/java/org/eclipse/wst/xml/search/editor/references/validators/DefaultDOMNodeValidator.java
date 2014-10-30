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
package org.eclipse.wst.xml.search.editor.references.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.internal.utils.StringPool;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.reconcile.validator.AnnotationInfo;
import org.eclipse.wst.sse.ui.internal.reconcile.validator.IncrementalReporter;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.java.XMLSearcherForJava;
import org.eclipse.wst.xml.search.editor.validation.IMultiValidationResult;
import org.eclipse.wst.xml.search.editor.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.validation.LocalizedMessage;
import org.eclipse.wst.xml.search.editor.validation.ValidatorUtils;
import org.w3c.dom.Node;

public class DefaultDOMNodeValidator implements IXMLReferenceValidator {

	public static final IXMLReferenceValidator INSTANCE = new DefaultDOMNodeValidator();

	public void validate(IXMLReference reference, IDOMNode node, IFile file,
			IValidator validator, IReporter reporter, boolean batchMode) {
		if (reference != null && reference.getTo().size() > 0) {
			doValidate(reference, node, file, validator, reporter, batchMode);
		}
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

	protected void doValidate(IXMLReference reference, IDOMNode node,
			IFile file, IValidator validator, IReporter reporter,
			boolean batchMode) {
		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLSearcher searcher = expression.getSearcher();
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
									addMessage(node, file, validator, reporter,
											batchMode, messageText, severity,
											startOffset, length);
								}
							}
						}
					}
				} catch (Throwable e) {
					Trace.trace(Trace.SEVERE, e.getMessage(), e);
				}
			}
			return;
		}

		int nonNegativeElements = 0;
		for (IXMLReferenceTo referenceTo : reference.getTo()) {
			IXMLSearcher searcher = referenceTo.getSearcher();
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
								addMessage(node, file, validator, reporter, batchMode,
										messageText, severity, startOffset, length);
							}
							return;
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
				addMessage(node, file, validator, reporter, batchMode,
						messageText, severity, startOffset, length);
			}
		}
	}

	private void addMessage(IDOMNode node, IFile file, IValidator validator,
			IReporter reporter, boolean batchMode, String messageText,
			int severity, int startOffset, int length) {
		LocalizedMessage message = createMessage(startOffset, length,
				messageText, severity, node.getStructuredDocument());
		if (message != null) {
			if (batchMode) {
				message.setTargetObject(file);
				reporter.addMessage(validator, message);
			} else {
				AnnotationInfo info = new AnnotationInfo(message);
				((IncrementalReporter) reporter).addAnnotationInfo(validator,
						info);
			}
		}
	}

}
