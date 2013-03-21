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
package org.eclipse.wst.xml.search.core.internal.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.resource.DefaultResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.DefaultURIResolverProvider;
import org.eclipse.wst.xml.search.core.resource.IResourceQuerySpecification;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestorProvider;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.resource.IURIResolverProvider;

/**
 * 
 * Implementation of {@link IResourceQuerySpecification}.
 * 
 */
public class ResourceQuerySpecification implements IResourceQuerySpecification {

	public static final IResourceQuerySpecification DEFAULT = newDefaultQuerySpecification();

	private IResourceProvider containerProvider;
	private IMultiResourceProvider multiContainerProvider;
	private IResourceRequestor requestor;
	private IURIResolverProvider resolverProvider;

	protected ResourceQuerySpecification(IResourceProvider containerProvider,
			IMultiResourceProvider multiContainerProvider,
			IResourceRequestor requestor, IURIResolverProvider resolverProvider) {
		this.containerProvider = containerProvider;
		this.multiContainerProvider = multiContainerProvider;
		this.requestor = requestor;
		this.resolverProvider = resolverProvider;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return containerProvider.getResource(selectedNode, resource);
	}

	public IResourceRequestor getRequestor() {
		return requestor;
	}

	public IURIResolver getURIResolver(IFile file, Object selectedNode) {
		return resolverProvider.getURIResolver(file, selectedNode);
	}

	public static IResourceQuerySpecification newDefaultQuerySpecification() {
		return new ResourceQuerySpecification(FolderContainerProvider.INSTANCE,
				null, DefaultResourceRequestor.INSTANCE,
				DefaultURIResolverProvider.INSTANCE);
	}

	public static IResourceQuerySpecification newQuerySpecification(
			Object querySpecification) {
		ResourceQuerySpecification specification = (ResourceQuerySpecification) newDefaultQuerySpecification();
		if (querySpecification instanceof IResourceProvider) {
			specification
					.setContainerProvider((IResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IMultiResourceProvider) {
			specification
					.setMultiContainerProvider((IMultiResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IResourceRequestorProvider) {
			specification
					.setRequestor(((IResourceRequestorProvider) querySpecification)
							.getRequestor());
		}
		if (querySpecification instanceof IURIResolverProvider) {
			specification
					.setURIResolverProvider((IURIResolverProvider) querySpecification);
		}
		return specification;
	}

	private void setMultiContainerProvider(
			IMultiResourceProvider multiContainerProvider) {
		this.multiContainerProvider = multiContainerProvider;
	}

	private void setURIResolverProvider(IURIResolverProvider resolverProvider) {
		this.resolverProvider = resolverProvider;
	}

	private void setRequestor(IResourceRequestor requestor) {
		this.requestor = requestor;
	}

	private void setContainerProvider(IResourceProvider containerProvider) {
		this.containerProvider = containerProvider;
	}

	public boolean isMultiResource() {
		return multiContainerProvider != null;
	}

	public IResource[] getResources(Object selectedNode, IResource resource) {
		return multiContainerProvider.getResources(selectedNode, resource);
	}
}
