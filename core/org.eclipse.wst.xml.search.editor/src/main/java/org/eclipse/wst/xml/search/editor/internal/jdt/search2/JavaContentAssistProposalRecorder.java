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
package org.eclipse.wst.xml.search.editor.internal.jdt.search2;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;

@SuppressWarnings("restriction")
public class JavaContentAssistProposalRecorder implements
		IContentAssistProposalRecorder {

	private final IRegion region;
	private final Collection<ICompletionProposal> proposals;

	private static final String ICONS_FULL_OBJ16_ENUM_GIF = "icons/full/obj16/enum.gif";

	/**
	 * 
	 * Creates a new {@link JavaContentAssistProposalRecorder}.
	 * 
	 * @param region
	 */

	public JavaContentAssistProposalRecorder(IRegion region,
			List<ICompletionProposal> proposals) {
		this.proposals = proposals;
		this.region = region;
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
		proposals.add(new CustomCompletionProposal(replaceText, region
				.getOffset(), region.getLength(), cursorPosition, image,
				displayText, null, proposedObject != null ? proposedObject
						.toString() : null, relevance, true));

	}

}
