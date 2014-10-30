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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.internal.contentassist.IRelevanceCompletionProposal;
import org.w3c.dom.Attr;

/**
 * 
 * JFace {@link ICompletionProposal} completion proposal for DOM {@link Attr}.
 */
class DOMAttrCompletionProposal implements ICompletionProposal,
		ICompletionProposalExtension, ICompletionProposalExtension2,
		ICompletionProposalExtension4, ICompletionProposalExtension5,
		IRelevanceCompletionProposal {

	private IContextInformation fContextInformation;
	private int fCursorPosition;
	private String fDisplayString;
	private Image fImage;
	private int fOriginalReplacementLength;
	private int fRelevance;
	private int fReplacementLength;
	private int fReplacementOffset;
	private String fReplacementString;
	private char fTriggers[];
	private boolean fUpdateLengthOnValidate;
	private Object proposedObject;

	public DOMAttrCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			IContextInformation contextInformation, int relevance,
			boolean updateReplacementLengthOnValidate, Object proposedObject) {
		fCursorPosition = 0;
		fRelevance = 0;
		fReplacementLength = 0;
		fReplacementOffset = 0;
		fReplacementString = null;
		this.proposedObject = null;
		fReplacementString = (new StringBuilder("\""))
				.append(replacementString).toString();
		fReplacementOffset = replacementOffset;
		fReplacementLength = replacementLength;
		fCursorPosition = cursorPosition + 1;
		fImage = image;
		fDisplayString = displayString;
		fContextInformation = contextInformation;
		fRelevance = relevance;
		fUpdateLengthOnValidate = updateReplacementLengthOnValidate;
		fOriginalReplacementLength = fReplacementLength;
		this.proposedObject = proposedObject;
	}

	public DOMAttrCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			IContextInformation contextInformation, int relevance,
			Object proposedObject) {
		this(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, contextInformation,
				relevance, true, proposedObject);
	}

	public void apply(IDocument document) {
		String charBeforeCursor = "";
		try {
			charBeforeCursor = document.get(getReplacementOffset() - 1, 1);
		} catch (BadLocationException _ex) {
		}
		if ("\"".equals(charBeforeCursor)) {
			CompletionProposal proposal = new CompletionProposal(
					getReplacementString(), getReplacementOffset() - 1,
					getReplacementLength(), getCursorPosition() + 1,
					getImage(), getDisplayString(), getContextInformation(),
					getAdditionalProposalInfo());
			proposal.apply(document);
		} else {
			CompletionProposal proposal = new CompletionProposal(
					(new StringBuilder(String.valueOf(getReplacementString())))
							.append("\"").toString(), getReplacementOffset(),
					getReplacementLength(), getCursorPosition() + 1,
					getImage(), getDisplayString(), getContextInformation(),
					getAdditionalProposalInfo());
			proposal.apply(document);
		}
	}

	public void apply(IDocument document, char trigger, int offset) {
		try {
			document.get(getReplacementOffset(), 1);
		} catch (BadLocationException _ex) {
		}
		CompletionProposal proposal = new CompletionProposal(
				(new StringBuilder(String.valueOf(getReplacementString())))
						.append("\"").toString(), getReplacementOffset(),
				getReplacementLength(), getCursorPosition() + 1, getImage(),
				getDisplayString(), getContextInformation(),
				getAdditionalProposalInfo());
		proposal.apply(document);
	}

	public void apply(ITextViewer viewer, char trigger, int stateMask,
			int offset) {
		IDocument document = viewer.getDocument();
		int caretOffset = viewer.getTextWidget().getCaretOffset();
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
			caretOffset = extension.widgetOffset2ModelOffset(caretOffset);
		} else {
			caretOffset = viewer.getTextWidget().getCaretOffset()
					+ viewer.getVisibleRegion().getOffset();
		}
		if (caretOffset == getReplacementOffset())
			apply(document);
		else
			try {
				int endOffsetOfChanges = getReplacementString().length()
						+ getReplacementOffset();
				if (endOffsetOfChanges >= caretOffset) {
					int postCaretReplacementLength = (getReplacementOffset() + getReplacementLength())
							- caretOffset;
					int preCaretReplacementLength = getReplacementString()
							.length() - (endOffsetOfChanges - caretOffset);
					if (postCaretReplacementLength < 0)
						postCaretReplacementLength = 0;
					String charAfterCursor = document.get(caretOffset, 1);
					if ("\"".equals(charAfterCursor))
						document.replace(
								caretOffset,
								postCaretReplacementLength - 1 >= 0 ? postCaretReplacementLength - 1
										: postCaretReplacementLength,
								getReplacementString().substring(
										preCaretReplacementLength));
					else
						document.replace(
								caretOffset,
								postCaretReplacementLength,
								(new StringBuilder(
										String.valueOf(getReplacementString()
												.substring(
														preCaretReplacementLength))))
										.append("\"").toString());
				}
				if (caretOffset > getReplacementOffset()) {
					int preCaretTextLength = caretOffset
							- getReplacementOffset();
					document.replace(getReplacementOffset(),
							preCaretTextLength, getReplacementString()
									.substring(0, preCaretTextLength));
				}
			} catch (BadLocationException _ex) {
				apply(document);
			} catch (StringIndexOutOfBoundsException _ex) {
				apply(document);
			}
	}

	public String getAdditionalProposalInfo() {
		Object o = getProposedObject();
		if (o instanceof String) {
			return (String) o;
		}
		return "";
		// SIDocEditorUtils.createAdditionalProposalInfo(
		// getProposedObject(), new NullProgressMonitor());
	}

	public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
		Object o = getProposedObject();
		if (o instanceof String) {
			return (String) o;
		}
		return "";
	}

	public IContextInformation getContextInformation() {
		return fContextInformation;
	}

	public int getContextInformationPosition() {
		return getCursorPosition();
	}

	public int getCursorPosition() {
		return fCursorPosition;
	}

	public String getDisplayString() {
		return fDisplayString;
	}

	public Image getImage() {
		return fImage;
	}

	public Object getProposedObject() {
		return proposedObject;
	}

	public int getRelevance() {
		return fRelevance;
	}

	public int getReplacementLength() {
		return fReplacementLength;
	}

	public int getReplacementOffset() {
		return fReplacementOffset;
	}

	public String getReplacementString() {
		return fReplacementString;
	}

	public Point getSelection(IDocument document) {
		CompletionProposal proposal = new CompletionProposal(
				getReplacementString(), getReplacementOffset(),
				getReplacementLength(), getCursorPosition(), getImage(),
				getDisplayString(), getContextInformation(), null);
		return proposal.getSelection(document);
	}

	public char[] getTriggerCharacters() {
		return fTriggers;
	}

	public boolean isAutoInsertable() {
		return true;
	}

	public boolean isValidFor(IDocument document, int offset) {
		return validate(document, offset, null);
	}

	public void selected(ITextViewer itextviewer, boolean flag) {
	}

	public void setContextInformation(IContextInformation contextInfo) {
		fContextInformation = contextInfo;
	}

	public void setCursorPosition(int pos) {
		fCursorPosition = pos;
	}

	public void setRelevance(int relevance) {
		fRelevance = relevance;
	}

	public void setReplacementOffset(int replacementOffset) {
		fReplacementOffset = replacementOffset;
	}

	public void setReplacementString(String replacementString) {
		fReplacementString = replacementString;
	}

	public void setTriggerCharacters(char triggers[]) {
		fTriggers = triggers;
	}

	protected boolean startsWith(IDocument document, int offset, String word) {
		int wordLength = word != null ? word.length() : 0;
		if (offset > fReplacementOffset + wordLength)
			return false;
		try {
			int length = offset - fReplacementOffset;
			String start = document.get(fReplacementOffset, length);
			start = start.replaceAll("\"", "");
			String wordTemp = word.replaceAll("\"", "").substring(0,
					start.length());
			return wordTemp.equalsIgnoreCase(start);
		} catch (BadLocationException _ex) {
			return false;
		}
	}

	public void unselected(ITextViewer itextviewer) {
	}

	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		if (offset < fReplacementOffset)
			return false;
		boolean validated = startsWith(document, offset, fReplacementString);
		boolean validatedClass = startsWith(document, offset, fDisplayString);
		if (fUpdateLengthOnValidate) {
			int newLength = offset - getReplacementOffset();
			int delta = newLength - fOriginalReplacementLength;
			fReplacementLength = delta + fOriginalReplacementLength;
		}
		return validated || validatedClass;
	}
}
