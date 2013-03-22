package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;

public class JettyQuerySpecification implements IXMLSearchRequestorProvider,
		IResourceProvider {

	public IXMLSearchRequestor getRequestor() {
		return JettySearchRequestor.INSTANCE;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return FolderContainerProvider.INSTANCE.getResource(selectedNode,
				resource);
	}

}
