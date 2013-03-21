package org.eclipse.wst.html.search.editor.internal.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.html.ui.internal.contentassist.HTMLTemplatesCompletionProposalComputer;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.contentassist.XMLReferencesContentAssistUtils;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;

public class HTMLReferencesCompletionProposalComputer extends HTMLTemplatesCompletionProposalComputer {

	@Override
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		// XML reference
		XMLReferencesContentAssistUtils.addAttributeValueProposals(contentAssistRequest);
		super.addAttributeValueProposals(contentAssistRequest, context);
	}
	
	
}
