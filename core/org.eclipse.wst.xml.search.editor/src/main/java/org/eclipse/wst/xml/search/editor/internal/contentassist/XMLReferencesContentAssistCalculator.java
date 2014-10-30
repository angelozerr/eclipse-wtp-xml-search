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
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistCalculator;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistContext;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;

public class XMLReferencesContentAssistCalculator implements
		IContentAssistCalculator {

	private IXMLReference reference;
	private IDOMNode selectedNode;

	public XMLReferencesContentAssistCalculator(IXMLReference reference,
			IDOMNode selectedNode) {
		this.reference = reference;
		this.selectedNode = selectedNode;
	}

	public void computeProposals(IContentAssistContext context,
			IContentAssistProposalRecorder recorder) {
		IFile file = context.getFile();
		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLSearcher searcher = expression.getSearcher();
			if (searcher != null) {
				searcher.searchForCompletion(selectedNode, context
						.getMatchString(), null, null, file, expression,
						recorder);
			}

		} else {
			Collection<IXMLReferenceTo> toPath = reference.getTo();
			for (IXMLReferenceTo referenceTo : toPath) {
				IXMLSearcher searcher = referenceTo.getSearcher();
				if (searcher != null) {
					searcher.searchForCompletion(selectedNode, context
							.getMatchString(), null, null, file, referenceTo,
							recorder);
				}
			}
		}
	}

}
