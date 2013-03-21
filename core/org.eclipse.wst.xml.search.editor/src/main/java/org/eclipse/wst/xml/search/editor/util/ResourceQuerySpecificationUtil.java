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
package org.eclipse.wst.xml.search.editor.util;

import org.eclipse.wst.xml.search.core.resource.IResourceQuerySpecification;
import org.eclipse.wst.xml.search.core.resource.ResourceQuerySpecificationManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToResource;

public class ResourceQuerySpecificationUtil {

	public static IResourceQuerySpecification getQuerySpecification(
			IXMLReferenceToResource referencetoResource) {
		return ResourceQuerySpecificationManager.getDefault()
				.getQuerySpecification(
						referencetoResource.getQuerySpecificationId());
	}

	public static IResourceQuerySpecification[] getQuerySpecifications(
			IXMLReferenceToResource referencetoResource) {
		IResourceQuerySpecification querySpecification = getQuerySpecification(referencetoResource);
		if (querySpecification != null) {
			IResourceQuerySpecification[] result = new IResourceQuerySpecification[1];
			result[0] = querySpecification;
			return result;
		}
		return IResourceQuerySpecification.EMPTY;
	}
}
