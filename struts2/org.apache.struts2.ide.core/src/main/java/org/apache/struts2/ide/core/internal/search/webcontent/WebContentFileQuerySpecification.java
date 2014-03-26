package org.apache.struts2.ide.core.internal.search.webcontent;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.resource.AllResourcesRequestor;
import org.eclipse.wst.xml.search.core.resource.DefaultResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestorProvider;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.resource.IURIResolverProvider;
import org.eclipse.wst.xml.search.core.resource.ResourceBaseURIResolver;

public class WebContentFileQuerySpecification implements IResourceProvider,
		IResourceRequestorProvider, IURIResolverProvider {

	public IResource getResource(Object selectedNode, IResource resource) {
		IProject project = resource.getProject();
		IFolder webINFFolder = project.getFolder("WEB-INF");
		if (!webINFFolder.exists()) {
			webINFFolder = project.getFolder("WebContent/WEB-INF");
		}
		if (!webINFFolder.exists()) {
			return null;
		}
		return webINFFolder.getParent();
	}

	public IResourceRequestor getRequestor() {
		return WebContentResourceRequestor.INSTANCE;
	}

	public IURIResolver getURIResolver(IFile file, Object selectedNode) {
		return ResourceBaseURIResolver.INSTANCE;
	}

}
