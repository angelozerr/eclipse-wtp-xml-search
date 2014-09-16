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
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToProperty;

public class XMLReferenceToProperty extends AbstractXMLReferenceTo implements
		IXMLReferenceToProperty {

	public XMLReferenceToProperty(
			IReference ownerReference,
			String id,
			String querySpecificationId,
			String tokenId) { 
		super(ownerReference, id, querySpecificationId, tokenId);
	}

	public ToType getType() {
		return ToType.PROPERTY;
	}

}
