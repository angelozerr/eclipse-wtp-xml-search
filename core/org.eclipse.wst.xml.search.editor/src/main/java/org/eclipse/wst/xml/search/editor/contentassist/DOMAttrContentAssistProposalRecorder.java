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
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
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

		int replacementLength = request.getReplacementLength();

		if (request.getRegion().getType().equals(DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE)) {
			// handle the code completion of attribute of jsp file,
			// because jsp tokenizer doesn't detect the attribute value really well and
			// causes the code completion proposal to replace more text than it should 
			String text = request.getText().trim();
			if (text.length() > 0){
				if (!text.equals("\"") && !text.equals("\'")) {
					// Split the text with " ", ":", "\r", "n", "<", ">"
					final String separators = "[\\s\\<\\>:\"\']";
					// remove the beginning quote, since the first quote cannot be a separator,
					// when finished, increase the length by 1 for the quote removed.
					String[] splittedTexts = text.replaceFirst("^[\"\']", "").split(separators);
					if (splittedTexts != null && splittedTexts.length > 0) {
						replacementLength = splittedTexts[0].length() + 1;
					}
					else {
						replacementLength = 0;
					}
				}
			}
		}

		request.addProposal(new DOMAttrCompletionProposal(replaceText, request
				.getReplacementBeginPosition(), replacementLength,
				cursorPosition, image, displayText, null, relevance,
				proposedObject));
	}

}
