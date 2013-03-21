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
package org.eclipse.wst.xml.search.editor.internal.searchers.javamethod;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.IJavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractorProvider;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.XPathClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.DefaultJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestorProvider;
import org.w3c.dom.Node;

public class JavaMethodQuerySpecification implements
		IJavaMethodQuerySpecification {

	public static final IJavaMethodQuerySpecification DEFAULT = newDefaultJavaMethodQuerySpecification();

	private IJavaMethodRequestor requestor;
	private IJavaMethodRequestorProvider requestorProvider;
	private IClassNameExtractor classNameExtractor;
	private IClassNameExtractorProvider classNameExtractorProvider;

	public JavaMethodQuerySpecification(IJavaMethodRequestor requestor,
			IJavaMethodRequestorProvider requestorProvider,
			IClassNameExtractor classNameExtractor,
			IClassNameExtractorProvider classNameExtractorProvider) {
		this.requestor = requestor;
		this.requestorProvider = requestorProvider;
		this.classNameExtractor = classNameExtractor;
		this.classNameExtractorProvider = classNameExtractorProvider;
	}

	public static IJavaMethodQuerySpecification newDefaultJavaMethodQuerySpecification() {
		return new JavaMethodQuerySpecification(
				DefaultJavaMethodRequestor.INSTANCE, null,
				XPathClassNameExtractor.INSTANCE, null);
	}

	public static IJavaMethodQuerySpecification newJavaMethodQuerySpecification(
			Object querySpecification) {
		JavaMethodQuerySpecification specification = (JavaMethodQuerySpecification) newDefaultJavaMethodQuerySpecification();
		if (querySpecification instanceof IJavaMethodRequestor) {
			specification
					.setRequestor((IJavaMethodRequestor) querySpecification);
		}
		if (querySpecification instanceof IJavaMethodRequestorProvider) {
			specification
					.setRequestorProvider((IJavaMethodRequestorProvider) querySpecification);
		}
		if (querySpecification instanceof IClassNameExtractor) {
			specification
					.setClassNameExtractor((IClassNameExtractor) querySpecification);
		}
		if (querySpecification instanceof IClassNameExtractorProvider) {
			specification
					.setClassNameExtractorProvider((IClassNameExtractorProvider) querySpecification);
		}
		return specification;
	}

	private void setClassNameExtractorProvider(
			IClassNameExtractorProvider classNameExtractorProvider) {
		this.classNameExtractorProvider = classNameExtractorProvider;
	}

	private void setClassNameExtractor(IClassNameExtractor classNameExtractor) {
		this.classNameExtractor = classNameExtractor;
	}

	private void setRequestorProvider(
			IJavaMethodRequestorProvider requestorProvider) {
		this.requestorProvider = requestorProvider;

	}

	private void setRequestor(IJavaMethodRequestor requestor) {
		this.requestor = requestor;

	}

	public IJavaMethodRequestor getRequestor(Object selectedNode, IFile file) {
		if (requestorProvider != null) {
			return requestorProvider.getRequestor(selectedNode, file);
		}
		return requestor;
	}

	public IClassNameExtractor getClassNameExtractor(Object selectedNode,
			IFile file) {
		if (classNameExtractorProvider != null) {
			return classNameExtractorProvider.getClassNameExtractor(
					selectedNode, file);
		}
		return classNameExtractor;
	}
}
