package com.liferay.ide.xml.internal.search;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;

public abstract class AbstractWebInfQuerySpecification implements
		IResourceProvider, IXMLSearchRequestorProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.container.
	 * IResourceProvider#getResource(java.lang.Object,
	 * org.eclipse.core.resources.IResource)
	 */
	public IResource getResource(Object selectedNode, IResource resource) {
		// Search WEB-INF folder.
		IContainer folder = resource.getParent();
		if ("WEB-INF".equals(folder.getName())) {
			return folder;
		}
		IFolder webInf = resource.getProject().getFolder(
				new Path("docroot/WEB-INF"));
		if (webInf.exists()) {
			return webInf;
		}
		return null;
	}
}
