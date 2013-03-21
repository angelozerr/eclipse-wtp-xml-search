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
public class DOMTextContentAssistProposalRecorder implements
		IContentAssistProposalRecorder {

	private static final String ICONS_FULL_OBJ16_ENUM_GIF = "icons/full/obj16/enum.gif";
	
	private final ContentAssistRequest request;

	private final String textContent;

	private final int startOffset;

	/**
	 * 
	 * Creates a new {@link DOMTextContentAssistProposalRecorder}.
	 */

	public DOMTextContentAssistProposalRecorder(ContentAssistRequest request,
			String textContent, int startOffset) {
		this.request = request;
		this.textContent = textContent;
		this.startOffset = startOffset;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */

	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText) {
		recordProposal(image, relevance, displayText, replaceText, null);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */

	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText, Object proposedObject) {
		int newLength = replaceText.length() - 1;
		recordProposal(image, relevance, displayText, replaceText, newLength,
				proposedObject);
	}

	public void recordProposal(Image image, int relevance, String displayText,
			String replaceText, int cursorPosition, Object proposedObject) {
		if (image == null) {
			image = XMLEditorPluginImageHelper.getInstance().getImage(
					ICONS_FULL_OBJ16_ENUM_GIF);
		}
		int oldLength = textContent.length();
		request.addProposal(new DOMTextCompletionProposal(replaceText,
				startOffset, oldLength, cursorPosition, image, displayText,
				null, relevance, proposedObject));

	}
}
