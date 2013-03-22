package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractorProvider;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestorProvider;
import org.w3c.dom.Node;

public abstract class AbstractJettyQueryConfiguration implements
		IJavaMethodRequestorProvider, IClassNameExtractorProvider {

	public IClassNameExtractor getClassNameExtractor(Object selectedNode,
			IFile file) {
		return JettyClassNameExtractor.INSTANCE;
	}

}
