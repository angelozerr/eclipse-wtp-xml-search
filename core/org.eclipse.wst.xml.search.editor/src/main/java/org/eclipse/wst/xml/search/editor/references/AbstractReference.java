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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferencePath;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.IJavaMethodQuerySpecification;

public abstract class AbstractReference extends ArrayList<IXMLReferenceTo> implements IReference {

	public List<IXMLReferenceTo> getTo() {
		return this;
	}

	public IReference addTo(IXMLReferenceTo to) {
		super.add(to);
		return this;
	}

	public IXMLReferenceToXML createToXML(
			String id,
			IXMLSearcher searcher,
			String path,
			String targetNodes,
			Namespaces namespaces,
			String querySpecificationId,
			String tokenId,
			IXMLReferenceFilter filter,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider) {
		return new XMLReferencePath(this, id, searcher, path, targetNodes
				.split(","), namespaces, querySpecificationId, tokenId, filter,
				additionalProposalInfoProvider);
	}

	public IXMLReferenceToResource createToResource(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<IResource> additionalProposalInfoProvider) {
		return new XMLReferenceToResource(this, id, searcher,
				querySpecificationId, tokenId, additionalProposalInfoProvider);
	}

	public IXMLReferenceToJava createToJava(String id, IXMLSearcher searcher,
			String querySpecificationId, String tokenId,
			IJavaQuerySpecification querySpecification) {
		return new XMLReferenceToJava(this, id, searcher, querySpecificationId,
				querySpecification, tokenId);
	}

	public IXMLReferenceToJavaMethod createToJavaMethod(String id,
			IXMLSearcher searcher, String querySpecificationId, String tokenId,
			String pathForClass,
			IJavaMethodQuerySpecification querySpecification) {
		return new XMLReferenceToJavaMethod(this, id, searcher,
				querySpecificationId, pathForClass, querySpecification, tokenId);
	}

	public IXMLReferenceToStatic createToStatic(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider) {
		return new XMLReferenceToStatic(this, id, searcher,
				querySpecificationId, tokenId, additionalProposalInfoProvider);
	}

	public IXMLReferenceToProperty createToProperty(
			String id,
			IXMLSearcher searcher,
			String querySpecificationId,
			String tokenId,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider) {
		return new XMLReferenceToProperty(this, id, searcher,
				querySpecificationId, tokenId, additionalProposalInfoProvider);
	}

	public boolean isExpression() {
		return false;
	}

}
