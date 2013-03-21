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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;

@SuppressWarnings("restriction")
public class DOMAttrContentAssistProposalRecorder implements
		IContentAssistProposalRecorder {

	private static final String ICONS_FULL_OBJ16_ENUM_GIF = "icons/full/obj16/enum.gif";

	private final ContentAssistRequest request;

	/**
	 * 
	 * Creates a new {@link DOMAttrContentAssistProposalRecorder}.
	 */

	public DOMAttrContentAssistProposalRecorder(ContentAssistRequest request) {
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.editor.contentassist.
	 * IContentAssistProposalRecorder
	 * #recordProposal(org.eclipse.swt.graphics.Image, int, java.lang.String,
	 * java.lang.String)
	 */
	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText) {
		recordProposal(image, relevance, displayText, replaceText, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.editor.contentassist.
	 * IContentAssistProposalRecorder
	 * #recordProposal(org.eclipse.swt.graphics.Image, int, java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText, Object proposedObject) {
		recordProposal(image, relevance, displayText, replaceText,
				replaceText.length(), proposedObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.editor.contentassist.
	 * IContentAssistProposalRecorder
	 * #recordProposal(org.eclipse.swt.graphics.Image, int, java.lang.String,
	 * java.lang.String, int, java.lang.Object)
	 */
	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText, int cursorPosition, Object proposedObject) {
		if (image == null) {
			image = XMLEditorPluginImageHelper.getInstance().getImage(
					ICONS_FULL_OBJ16_ENUM_GIF);
		}
		request.addProposal(new DOMAttrCompletionProposal(replaceText, request
				.getReplacementBeginPosition(), request.getReplacementLength(),
				cursorPosition, image, displayText, null, relevance,
				proposedObject));
	}

}
