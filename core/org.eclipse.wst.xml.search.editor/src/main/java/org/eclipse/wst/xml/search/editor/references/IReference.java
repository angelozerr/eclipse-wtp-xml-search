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
package org.eclipse.wst.xml.search.editor.references;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.IJavaMethodQuerySpecification;

public interface IReference {

	List<IXMLReferenceTo> getTo();
	
	IReference addTo(IXMLReferenceTo to);
	
	IXMLReferenceToXML createToXML(
			String id,
			IXMLSearcher searcher,
			String path,
			String targetNodes,
			Namespaces namespaces,
			String querySpecificationId,
			String tokenId,
			IXMLReferenceFilter filter,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider);

	IXMLReferenceToResource createToResource(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<IResource> additionalProposalInfoProvider);

	IXMLReferenceToJava createToJava(String id, IXMLSearcher searcher,
			String querySpecificationId, String tokenId,
			IJavaQuerySpecification querySpecification);

	IXMLReferenceToJavaMethod createToJavaMethod(String id,
			IXMLSearcher searcher, String querySpecificationId, String tokenId,
			String pathForClass,
			IJavaMethodQuerySpecification querySpecification);

	IXMLReferenceToStatic createToStatic(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider);

	IXMLReferenceToProperty createToProperty(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider);

	boolean isExpression();
}
