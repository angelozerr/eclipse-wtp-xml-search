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
package org.eclipse.wst.xml.search.editor.internal.references;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.references.AbstractReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.IJavaMethodQuerySpecification;

public class XMLReference extends AbstractReference implements IXMLReference {

	private final String[] contentTypeIds;
	private final IXMLReferencePath from;
	private final IXMLReferenceValidator validator;

	public XMLReference(String fromPath, String fromTargetNodes,
			Namespaces namespaces, String querySpecificationId,
			String[] contentTypeIds, IXMLReferenceFilter filter,
			IXMLReferenceValidator validator) {
		this.from = createToXML(null, null, fromPath, fromTargetNodes,
				namespaces, querySpecificationId, null, filter, null);
		this.contentTypeIds = contentTypeIds;
		this.validator = validator;
	}

	public IXMLReferencePath getFrom() {
		return from;
	}

	public List<IXMLReferenceTo> getTo() {
		return this;
	}

	public IXMLReference addTo(IXMLReferenceTo to) {
		super.add(to);
		return this;
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

	public String[] getContentTypeIds() {
		return contentTypeIds;
	}

	public IXMLReferenceValidator getValidator() {
		return validator;
	}

	public boolean isExpression() {
		return false;
	}

}
