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
package org.eclipse.jst.jsp.search.editor.internal.contentassist;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.contentassist.XMLReferencesContentAssistUtils;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

public class JSPReferenceContentAssistProcessor extends
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
}
