package org.apache.struts2.ide.core.internal.search.all;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import org.eclipse.wst.xml.search.editor.queryspecifications.JavaProjectSrcFolders;

public class FindAllStruts2QuerySpecification implements
		IXMLSearchRequestorProvider, IMultiResourceProvider {

	public IXMLSearchRequestor getRequestor() {
		return FindAllStruts2SearchRequestor.INSTANCE;
	}

	public IResource[] getResources(Object selectedNode, IResource resource) {
		return JavaProjectSrcFolders.INSTANCE.getResources(selectedNode,
				resource);
	}
}
