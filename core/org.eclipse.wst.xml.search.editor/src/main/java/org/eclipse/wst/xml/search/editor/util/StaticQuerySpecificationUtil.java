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

import org.eclipse.wst.xml.search.core.statics.IStaticValueQuerySpecification;
import org.eclipse.wst.xml.search.core.statics.StaticValueQuerySpecificationManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToStatic;

public class StaticQuerySpecificationUtil {

	public static IStaticValueQuerySpecification getStaticQuerySpecification(
			IXMLReferenceToStatic referenceToStatic) {
		return StaticValueQuerySpecificationManager.getDefault()
				.getQuerySpecification(
						referenceToStatic.getQuerySpecificationId());
	}
}
