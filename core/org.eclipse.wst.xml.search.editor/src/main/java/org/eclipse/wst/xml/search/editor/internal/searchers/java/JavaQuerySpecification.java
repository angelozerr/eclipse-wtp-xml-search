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
package org.eclipse.wst.xml.search.editor.internal.searchers.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.wst.xml.search.editor.searchers.java.IExtendedClassProvider;
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;

public class JavaQuerySpecification implements IJavaQuerySpecification {

	public static final IJavaQuerySpecification DEFAULT = null;
	private IExtendedClassProvider implementsClassProvider;

	public JavaQuerySpecification(
			IExtendedClassProvider implementsClassProvider) {
		this.implementsClassProvider = implementsClassProvider;
	}

	public static IJavaQuerySpecification newJavaQuerySpecification(
			Object querySpecification) {
		if (querySpecification != null) {
			JavaQuerySpecification specification = new JavaQuerySpecification(
					null);
			if (querySpecification instanceof IExtendedClassProvider) {
				specification
						.setImplementsClassProvider((IExtendedClassProvider) querySpecification);
			}
			return specification;
		}
		return null;
	}

	private void setImplementsClassProvider(
			IExtendedClassProvider implementsClassProvider) {
		this.implementsClassProvider = implementsClassProvider;
	}

	public IType[] getExtends(Object selectedNode, IFile file) {
		if (implementsClassProvider != null) {
			return implementsClassProvider.getExtends(selectedNode,
					file);
		}
		return null;
	}

}
