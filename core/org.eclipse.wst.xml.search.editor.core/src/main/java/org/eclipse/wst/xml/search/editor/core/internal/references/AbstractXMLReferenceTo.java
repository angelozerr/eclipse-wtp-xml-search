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

import org.eclipse.wst.xml.search.editor.core.references.IReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;

public abstract class AbstractXMLReferenceTo implements IXMLReferenceTo {

	private final String referenceToId;	
	private final IReference ownerReference;
	private final String querySpecificationId;
	private final String tokenId;
	
	public AbstractXMLReferenceTo(IReference ownerReference, String referenceToId,
			String querySpecificationId, String tokenId) {
		this.ownerReference = ownerReference;
		this.querySpecificationId = querySpecificationId;
		this.referenceToId = (referenceToId != null ? referenceToId : querySpecificationId);
		this.tokenId = tokenId;
	}

	public IReference getOwnerReference() {
		return ownerReference;
	}

	public String getReferenceToId() {
		return referenceToId;
	}

	public String getQuerySpecificationId() {
		return querySpecificationId;
	}

	public String getTokenId() {
		return tokenId;
	}
}
