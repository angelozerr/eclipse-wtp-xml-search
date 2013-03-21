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
package org.eclipse.wst.xml.search.core.internal.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.properties.DefaultPropertiesRequestor;
import org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.properties.IPropertiesRequestor;
import org.eclipse.wst.xml.search.core.properties.IPropertiesRequestorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;

/**
 * 
 * {@link IPropertiesQuerySpecification} implementation.
 * 
 */
public class PropertiesQuerySpecification implements
		IPropertiesQuerySpecification {

	public static final IPropertiesQuerySpecification DEFAULT = newDefaultQuerySpecification();

	private IResourceProvider containerProvider;
	private IMultiResourceProvider multiContainerProvider;
	private IPropertiesRequestor requestor;

	protected PropertiesQuerySpecification(IResourceProvider containerProvider,
			IMultiResourceProvider multiContainerProvider,
			IPropertiesRequestor requestor) {
		this.containerProvider = containerProvider;
		this.multiContainerProvider = multiContainerProvider;
		this.requestor = requestor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.container.
	 * IResourceProvider
	 * #getResource(org.eclipse.wst.xml.core.internal.provisional
	 * .document.IDOMNode, org.eclipse.core.resources.IResource)
	 */
	public IResource getResource(Object selectedNode, IResource resource) {
		return containerProvider.getResource(selectedNode, resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.properties.IPropertiesRequestorProvider
	 * #getRequestor()
	 */
	public IPropertiesRequestor getRequestor() {
		return requestor;
	}

	/**
	 * Create default properties query specification.
	 * 
	 * @return
	 */
	public static IPropertiesQuerySpecification newDefaultQuerySpecification() {
		return new PropertiesQuerySpecification(
				FolderContainerProvider.INSTANCE, null,
				DefaultPropertiesRequestor.INSTANCE);
	}

	/**
	 * Create properties query specification by using information about the
	 * given <code>querySpecification</code>.
	 * 
	 * @param querySpecification
	 * @return
	 */
	public static IPropertiesQuerySpecification newQuerySpecification(
			Object querySpecification) {
		PropertiesQuerySpecification specification = (PropertiesQuerySpecification) newDefaultQuerySpecification();
		if (querySpecification instanceof IResourceProvider) {
			// resource provider is customized.
			specification
					.setContainerProvider((IResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IMultiResourceProvider) {
			// multi-resource provider is customized.
			specification
					.setMultiContainerProvider((IMultiResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IPropertiesRequestorProvider) {
			// requestor provider is customized.
			specification
					.setRequestor(((IPropertiesRequestorProvider) querySpecification)
							.getRequestor());
		}
		return specification;
	}

	private void setMultiContainerProvider(
			IMultiResourceProvider multiContainerProvider) {
		this.multiContainerProvider = multiContainerProvider;
	}

	private void setRequestor(IPropertiesRequestor requestor) {
		this.requestor = requestor;
	}

	private void setContainerProvider(IResourceProvider containerProvider) {
		this.containerProvider = containerProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification
	 * #isMultiResource()
	 */
	public boolean isMultiResource() {
		return multiContainerProvider != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.container.
	 * IMultiResourceProvider
	 * #getResources(org.eclipse.wst.xml.core.internal.provisional
	 * .document.IDOMNode, org.eclipse.core.resources.IResource)
	 */
	public IResource[] getResources(Object selectedNode, IResource resource) {
		return multiContainerProvider.getResources(selectedNode, resource);
	}
}
