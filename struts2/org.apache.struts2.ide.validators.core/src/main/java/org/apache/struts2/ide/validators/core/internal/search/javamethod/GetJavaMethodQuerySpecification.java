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
