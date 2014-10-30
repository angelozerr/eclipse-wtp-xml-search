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
package org.apache.struts2.ide.validators.core.internal.search.javamethod;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractorProvider;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.GetterJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestorProvider;
import org.w3c.dom.Node;

public class GetJavaMethodQuerySpecification implements
		IJavaMethodRequestorProvider, IClassNameExtractorProvider {

	public IJavaMethodRequestor getRequestor(Object selectedNode, IFile file) {
		return GetterJavaMethodRequestor.INSTANCE;
	}

	public IClassNameExtractor getClassNameExtractor(Object selectedNode,
			IFile file) {
		return ValidatorClassNameExtractor.INSTANCE;
	}

}
