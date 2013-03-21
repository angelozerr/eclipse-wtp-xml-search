package org.apache.struts2.ide.core.internal.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.resource.DOMNodeBaseURIResolver;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestorProvider;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.resource.IURIResolverProvider;

public class Struts2FileQuerySpecification implements IResourceProvider,
		IResourceRequestorProvider, IURIResolverProvider {

	public IResource getResource(Object selectedNode, IResource resource) {
		return resource.getParent();
	}

	public IResourceRequestor getRequestor() {
		return Struts2IncludeFileRequestor.INSTANCE;
	}

	public IURIResolver getURIResolver(IFile file, Object selectedNode) {
		return DOMNodeBaseURIResolver.INSTANCE;
	}

}
