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
package org.eclipse.wst.xml.search.editor.core.util;

import org.eclipse.wst.xml.search.editor.core.internal.searchers.javamethod.JavaMethodQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;

public class JavaMethodQuerySpecificationUtil {

	public static IJavaMethodQuerySpecification getQuerySpecification(
			IXMLReferenceToJavaMethod referenceToJavaMethod) {
		return JavaMethodQuerySpecificationrManager.getDefault()
				.getQuerySpecification(
						referenceToJavaMethod.getQuerySpecificationId());
	}
}
