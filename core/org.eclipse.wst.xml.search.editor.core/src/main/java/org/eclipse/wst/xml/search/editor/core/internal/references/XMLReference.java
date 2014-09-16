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
package org.eclipse.wst.xml.search.editor.core.internal.references;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.core.references.AbstractReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.core.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;

public class XMLReference extends AbstractReference implements IXMLReference {

	private final String referenceId;
	private final String[] contentTypeIds;
	private final IXMLReferencePath from;
	private final IXMLReferenceValidator validator;

	public XMLReference(String referenceId, String fromPath, String fromTargetNodes,
			Namespaces namespaces, String querySpecificationId,
			String[] contentTypeIds, IXMLReferenceFilter filter,
			IXMLReferenceValidator validator) {
		this.from = createToXML(null, fromPath, fromTargetNodes,
				namespaces, querySpecificationId, null, filter);
		this.referenceId = referenceId;
		this.contentTypeIds = contentTypeIds;
		this.validator = validator;
	}

	public String getReferenceId() {
		return referenceId;
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
			String querySpecificationId,
			String tokenId) {
		return new XMLReferenceToResource(this, id,querySpecificationId, tokenId); 
	}

	public IXMLReferenceToJava createToJava(String id, String querySpecificationId, String tokenId,
			IJavaQuerySpecification querySpecification) {
		return new XMLReferenceToJava(this, id, querySpecificationId,
				querySpecification, tokenId);
	}

	public IXMLReferenceToJavaMethod createToJavaMethod(String id,
			String querySpecificationId, String tokenId, String pathForClass,
			IJavaMethodQuerySpecification querySpecification) {
		return new XMLReferenceToJavaMethod(this, id,
				querySpecificationId, pathForClass, querySpecification, tokenId);
	}

	public IXMLReferenceToStatic createToStatic(
			String referenceToid,
			String querySpecificationId,
			String tokenId ) {
		return new XMLReferenceToStatic(this, referenceToid, querySpecificationId, 
				tokenId);
	}

	public IXMLReferenceToProperty createToProperty(
			String referenceToid,
			String querySpecificationId,
			String tokenId ) {
		return new XMLReferenceToProperty(this, referenceToid,
				querySpecificationId, tokenId);
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
