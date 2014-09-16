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
package org.eclipse.wst.xml.search.editor.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.core.validation.LocalizedMessage;
import org.eclipse.wst.xml.search.editor.internal.Messages;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.w3c.dom.Node;

public class ValidatorUtils {

	public static LocalizedMessage createMessage(IDOMAttr attr,
			IXMLReference reference, int nbElements) {
		if (nbElements == 1) {
			return null;
		}
		String textContent = attr.getValue();
		int start = attr.getValueRegionStartOffset();
		return createMessage(attr, start, reference, nbElements, textContent);
	}

	public static LocalizedMessage createMessage(IDOMText text,
			IXMLReference reference, int nbElements) {
		if (nbElements == 1) {
			return null;
		}
		String textContent = DOMUtils.getTextContent(text);
		int start = text.getStartOffset();
		return createMessage(text, start, reference, nbElements, textContent);
	}

	public static LocalizedMessage createMessage(IDOMNode node, int start,
			IXMLReference reference, int nbElements, String textContent) {
		int length = textContent.trim().length() + 2;
		String messageText = getMessageText(reference, nbElements, textContent);
		int severity = getSeverity(reference, nbElements);
		return createMessage(start, length, messageText, severity, node
				.getStructuredDocument());
	}

	public static int getSeverity(IXMLReference reference, int nbElements) {
		if (nbElements < 1) {
			return IMessage.HIGH_SEVERITY;
		}
		return IMessage.NORMAL_SEVERITY;
	}

	public static String getMessageText(IXMLReference reference,
			int nbElements, String textContent, Node node, IFile file) {
		if (nbElements < 0) {
			for (IXMLReferenceTo to : reference.getTo()) {
				if (to.getType() == IXMLReferenceTo.ToType.JAVA) {
					IType[] superTypes = ((IXMLReferenceToJava) to).getExtends(node, file);
					if (superTypes != null && superTypes.length > 0) {
						StringBuilder sb = new StringBuilder();
						for (IType type : superTypes ){
							sb.append(type.getFullyQualifiedName());
							sb.append(", ");
						}
						String superTypeNames = sb.toString().replaceAll(", $", "");
						return NLS.bind(ValidatorUtils.getTypeHierarchyIncorrectMessage(),textContent, superTypeNames);
					}
				}
			}
		} if (nbElements < 1) {
			return NLS.bind(ValidatorUtils.getNotFoundedMessage(reference
					.getTo().get(0)), textContent, nbElements);
		} else if(nbElements > 1) {
			return NLS.bind(ValidatorUtils.getNonUniqueMessage(reference
					.getTo().get(0)), textContent, nbElements);
		} else {
			return NLS.bind(ValidatorUtils.getDefaultMessage(), textContent);
		}
	}

	public static String getMessageText(IXMLReference reference,
			int nbElements, String textContent) {
		if (nbElements < 1) {
			return NLS.bind(ValidatorUtils.getNotFoundedMessage(reference
					.getTo().get(0)), textContent, nbElements);
		} else {
			return NLS.bind(ValidatorUtils.getNonUniqueMessage(reference
					.getTo().get(0)), textContent, nbElements);
		}
	}

	public static LocalizedMessage createMessage(int start, int length,
			String messageText, int severity,
			IStructuredDocument structuredDocument) {
		int lineNo = getLineNumber(start, structuredDocument);
		LocalizedMessage message = new LocalizedMessage(severity, messageText);
		message.setOffset(start);
		message.setLength(length);
		message.setLineNo(lineNo);
		return message;
	}

	private static int getLineNumber(int start, IDocument document) {
		int lineNo = -1;
		try {
			lineNo = document.getLineOfOffset(start);
		} catch (BadLocationException e) {
			Trace.trace(Trace.SEVERE, e.getMessage(), e);
		}
		return lineNo;
	}

	/**
	 * @param delta
	 *            the IFileDelta containing the file name to get
	 * @return the IFile
	 */
	public static IFile getFile(String delta) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path(delta));
		if (file != null && file.exists())
			return file;
		return null;
	}

	public static String getNotFoundedMessage(IXMLReferenceTo referenceTo) {
		switch (referenceTo.getType()) {
		case RESOURCE:
			return Messages.Validation_FileNotFounded;
		case JAVA:
			return Messages.Validation_ClassNotFounded;
		case XML:
			return Messages.Validation_ElementNotFounded;
		}
		return Messages.Validation_ElementNotFounded;
	}

	public static String getNonUniqueMessage(IXMLReferenceTo referenceTo) {
		switch (referenceTo.getType()) {
		case XML:
			return Messages.Validation_ElementNonUnique;
		}
		return Messages.Validation_ElementNonUnique;
	}

	public static String getTypeHierarchyIncorrectMessage() {
		return Messages.Validation_ClassHierarchyIncorrect;
	}

	private static String getDefaultMessage() {
		return Messages.Validation_ElementInvalid;
	}
}
