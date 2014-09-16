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

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.wst.xml.search.editor.core.references.IReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.core.searchers.java.IJavaQuerySpecification;

public class XMLReferenceToJava extends AbstractXMLReferenceTo implements
		IXMLReferenceToJava {

	private final IJavaQuerySpecification querySpecification;

	public XMLReferenceToJava(IReference ownerReference, String id,
			String querySpecificationId, IJavaQuerySpecification querySpecification, String tokenId) {
		super(ownerReference, id, querySpecificationId, tokenId);
		this.querySpecification = querySpecification;
	}

	public ToType getType() {
		return ToType.JAVA;
	}

	public IType[] getExtends(Object selectedNode, IFile file) {
		if (querySpecification != null) {
			return querySpecification.getExtends(selectedNode, file);
		}
		return null;
	}

}
