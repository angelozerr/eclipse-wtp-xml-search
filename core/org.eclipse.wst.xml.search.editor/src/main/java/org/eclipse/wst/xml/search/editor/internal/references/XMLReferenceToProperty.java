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
package org.eclipse.wst.xml.search.editor.internal.references;

import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.references.IReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;

public class XMLReferenceToProperty extends AbstractXMLReferenceTo implements
		IXMLReferenceToProperty {

	private final IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider;

	public XMLReferenceToProperty(
			IReference ownerReference,
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId, 
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider) {
		super(ownerReference, id, searcher, querySpecificationId, tokenId);
		this.additionalProposalInfoProvider = additionalProposalInfoProvider;
	}

	public ToType getType() {
		return ToType.PROPERTY;
	}

	public IContentAssistAdditionalProposalInfoProvider<?> getAdditionalProposalInfoProvider() {
		return additionalProposalInfoProvider;
	}
}
