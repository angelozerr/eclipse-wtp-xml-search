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

import org.eclipse.wst.xml.search.editor.references.IReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;

public abstract class AbstractXMLReferenceTo implements IXMLReferenceTo {

	private final String id;	
	private final IXMLSearcher searcher;
	private final IReference ownerReference;
	private final String querySpecificationId;
	private final String tokenId;
	
	public AbstractXMLReferenceTo(IReference ownerReference, String id,
			IXMLSearcher searcher, String querySpecificationId, String tokenId) {
		this.ownerReference = ownerReference;
		this.querySpecificationId = querySpecificationId;
		this.id = (id != null ? id : querySpecificationId);
		this.searcher = searcher;
		this.tokenId = tokenId;
	}

	public IReference getOwnerReference() {
		return ownerReference;
	}

	public String getId() {
		return id;
	}

	public String getQuerySpecificationId() {
		return querySpecificationId;
	}

	public IXMLSearcher getSearcher() {
		return searcher;
	}
	
	public String getTokenId() {
		return tokenId;
	}
}
