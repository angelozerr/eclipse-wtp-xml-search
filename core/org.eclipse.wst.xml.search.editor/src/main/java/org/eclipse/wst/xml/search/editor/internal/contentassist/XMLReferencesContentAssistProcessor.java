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
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.contentassist.XMLReferencesContentAssistUtils;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

public class XMLReferencesContentAssistProcessor extends
		XMLContentAssistProcessor {

	@Override
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest) {
		int proposalCount = 0;
		if (contentAssistRequest.getCompletionProposals() != null) {
			proposalCount = contentAssistRequest.getCompletionProposals().length;
		}
		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();
		// XML reference
		XMLReferencesContentAssistUtils
				.addAttributeValueProposals(contentAssistRequest);

		super.addAttributeValueProposals(contentAssistRequest);

	}

	@Override
	protected ContentAssistRequest computeEndTagOpenProposals(
			int documentPosition, String matchString,
			ITextRegion completionRegion, IDOMNode nodeAtOffset, IDOMNode node) {
		ContentAssistRequest request = super.computeEndTagOpenProposals(
				documentPosition, matchString, completionRegion, nodeAtOffset,
				node);
		// the cursor in the end-tag-open position, if matchString is "", that 
		// means actually the cursor is at the end of xml content, only in this case 
		// we need to handle the proposals
		// ignore when the matchString is "<" and "</"
		if(matchString.equals("")) {
			doEntityProposalIfNeeded(request);
		}
		return request;
	}

	@Override
	protected ContentAssistRequest computeContentProposals(
			int documentPosition, String matchString,
			ITextRegion completionRegion, IDOMNode nodeAtOffset, IDOMNode node) {
		// TODO Auto-generated method stub
		ContentAssistRequest request = super.computeContentProposals(
				documentPosition, matchString, completionRegion, nodeAtOffset,
				node);
		doEntityProposalIfNeeded(request);
		return request;
	}

	//
	// @Override
	// protected void addTagInsertionProposals(
	// ContentAssistRequest contentAssistRequest, int childPosition) {
	// doEntityProposalIfNeeded(contentAssistRequest);
	// super.addTagInsertionProposals(contentAssistRequest, childPosition);
	// }
	//
	// @Override
	// protected void addEntityProposals(
	// ContentAssistRequest contentAssistRequest, int documentPosition,
	// ITextRegion completionRegion, IDOMNode treeNode) {
	// CMElementDeclaration parentDecl =
	// getCMElementDeclaration(contentAssistRequest
	// .getParent());
	// if (!((parentDecl != null) && (parentDecl.getContentType() ==
	// CMElementDeclaration.PCDATA))) {
	// doEntityProposalIfNeeded(contentAssistRequest);
	// }
	// super.addEntityProposals(contentAssistRequest, documentPosition,
	// completionRegion, treeNode);
	// }

	private void doEntityProposalIfNeeded(
			ContentAssistRequest contentAssistRequest) {

		XMLReferencesContentAssistUtils.addEntityProposals(this,
				contentAssistRequest);
		// entityProposalsAlreadyDone = true;

	}

	// @Override
	// protected void addPCDATAProposal(String nodeName,
	// ContentAssistRequest contentAssistRequest) {
	// // XML reference
	// doEntityProposalIfNeeded(contentAssistRequest);
	// super.addPCDATAProposal(nodeName, contentAssistRequest);
	// }
}
