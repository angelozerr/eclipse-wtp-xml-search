package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod.set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod.AbstractJettyQueryConfiguration;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;

public class SetJavaMethodQuerySpecification extends
		AbstractJettyQueryConfiguration {

	public IJavaMethodRequestor getRequestor(Object selectedNode, IFile file) {
		return SetJavaMethodRequestor.INSTANCE;
	}

}
