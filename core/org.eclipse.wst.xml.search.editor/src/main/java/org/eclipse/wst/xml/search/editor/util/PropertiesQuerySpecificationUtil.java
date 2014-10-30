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
package org.eclipse.wst.xml.search.editor.util;

import org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.properties.PropertiesQuerySpecificationManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToProperty;

public class PropertiesQuerySpecificationUtil {

	public static IPropertiesQuerySpecification getQuerySpecification(
			IXMLReferenceToProperty referenceToProperty) {
		return PropertiesQuerySpecificationManager.getDefault()
				.getQuerySpecification(
						referenceToProperty.getQuerySpecificationId());
	}

	public static IPropertiesQuerySpecification[] getQuerySpecifications(
			IXMLReferenceToProperty referenceToProperty) {
		IPropertiesQuerySpecification querySpecification = getQuerySpecification(referenceToProperty);
		if (querySpecification != null) {
			IPropertiesQuerySpecification[] result = new IPropertiesQuerySpecification[1];
			result[0] = querySpecification;
			return result;
		}
		return IPropertiesQuerySpecification.EMPTY;
	}
}
