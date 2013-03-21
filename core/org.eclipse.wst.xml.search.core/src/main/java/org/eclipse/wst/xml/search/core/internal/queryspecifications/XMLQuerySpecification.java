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
package org.eclipse.wst.xml.search.core.internal.queryspecifications;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXPathProcessorIdProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.ProjectContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.DefaultStringQueryBuilderProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilderProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AllXMLExtensionFilesXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchVisitorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.XPathNodeSetSearchVisitor;

/**
 * 
 * Implementation of {@link IXMLQuerySpecification} which use providers like
 * {@link IResourceProvider} instead of directly {@link IResource}.
 * 
 */
public class XMLQuerySpecification implements IXMLQuerySpecification {

	public static final IXMLQuerySpecification INSTANCE = newDefaultQuerySpecification();

	private IStorageProvider storageProvider;
	private IMultiStorageProvider multiStorageProvider;
	private IResourceProvider containerProvider;
	private IMultiResourceProvider multiContainerProvider;
	private IXMLSearchRequestor requestor;
	private IXMLSearchDOMDocumentVisitor visitor;
	private IStringQueryBuilderProvider queryBuilderProvider;
	private String xpathProcessorId;

	protected XMLQuerySpecification(IResourceProvider containerProvider,
			IMultiResourceProvider multiContainerProvider,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor,
			IStringQueryBuilderProvider queryBuilderProvider,
			String xpathProcessorId) {
		this.containerProvider = containerProvider;
		this.multiContainerProvider = multiContainerProvider;
		this.storageProvider = null;
		this.multiStorageProvider = null;
		this.requestor = requestor;
		this.visitor = visitor;
		this.queryBuilderProvider = queryBuilderProvider;
		this.xpathProcessorId = xpathProcessorId;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return containerProvider.getResource(selectedNode, resource);
	}

	public IResource[] getResources(Object selectedNode, IResource resource) {
		if (multiContainerProvider == null) {
			return null;
		}
		return multiContainerProvider.getResources(selectedNode, resource);
	}

	public boolean isMultiResource() {
		return multiContainerProvider != null;
	}

	public IStorage getStorage(Object selectedNode, IResource resource) {
		if (storageProvider == null) {
			return null;
		}
		return storageProvider.getStorage(selectedNode, resource);
	}

	public IStorage[] getStorages(Object selectedNode, IResource resource) {
		if (multiStorageProvider == null) {
			return null;
		}
		return multiStorageProvider.getStorages(selectedNode, resource);
	}

	public boolean isSimpleStorage() {
		return storageProvider != null;
	}

	public boolean isMultiStorage() {
		return multiStorageProvider != null;
	}

	public IXMLSearchRequestor getRequestor() {
		return requestor;
	}

	public IXMLSearchDOMDocumentVisitor getVisitor() {
		return visitor;
	}

	public static IXMLQuerySpecification newDefaultQuerySpecification() {
		return new XMLQuerySpecification(ProjectContainerProvider.INSTANCE,
				null, AllXMLExtensionFilesXMLSearchRequestor.INSTANCE,
				XPathNodeSetSearchVisitor.INSTANCE,
				DefaultStringQueryBuilderProvider.INSTANCE, null);
	}

	public static IXMLQuerySpecification newQuerySpecification(
			Object querySpecification) {
		XMLQuerySpecification specification = (XMLQuerySpecification) newDefaultQuerySpecification();
		if (querySpecification instanceof IResourceProvider) {
			specification
					.setContainerProvider((IResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IMultiResourceProvider) {
			specification
					.setMultiContainerProvider((IMultiResourceProvider) querySpecification);
		}
		if (querySpecification instanceof IStorageProvider) {
			specification
					.setStorageProvider((IStorageProvider) querySpecification);
		}
		if (querySpecification instanceof IMultiStorageProvider) {
			specification
					.setMultiStorageProvider((IMultiStorageProvider) querySpecification);
		}
		if (querySpecification instanceof IXMLSearchRequestorProvider) {
			specification
					.setRequestor(((IXMLSearchRequestorProvider) querySpecification)
							.getRequestor());
		}
		if (querySpecification instanceof IXMLSearchVisitorProvider) {
			specification
					.setVisitor(((IXMLSearchVisitorProvider) querySpecification)
							.getVisitor());
		}
		if (querySpecification instanceof IStringQueryBuilderProvider) {
			specification
					.setStringQueryBuilderProvider((IStringQueryBuilderProvider) querySpecification);
		}
		if (querySpecification instanceof IXPathProcessorIdProvider) {
			specification
					.setXPathEvaluatorId(((IXPathProcessorIdProvider) querySpecification)
							.getXPathProcessorId());
		}
		return specification;
	}

	private void setMultiContainerProvider(
			IMultiResourceProvider multiContainerProvider) {
		this.multiContainerProvider = multiContainerProvider;
	}

	private void setStorageProvider(IStorageProvider storageProvider) {
		this.storageProvider = storageProvider;
	}

	private void setMultiStorageProvider(
			IMultiStorageProvider multiStorageProvider) {
		this.multiStorageProvider = multiStorageProvider;
	}

	private void setXPathEvaluatorId(String xPathEvaluatorId) {
		this.xpathProcessorId = xPathEvaluatorId;
	}

	private void setStringQueryBuilderProvider(
			IStringQueryBuilderProvider queryBuilderProvider) {
		this.queryBuilderProvider = queryBuilderProvider;
	}

	private void setRequestor(IXMLSearchRequestor requestor) {
		this.requestor = requestor;
	}

	private void setVisitor(IXMLSearchDOMDocumentVisitor visitor) {
		this.visitor = visitor;
	}

	private void setContainerProvider(IResourceProvider containerProvider) {
		this.containerProvider = containerProvider;
	}

	public IStringQueryBuilder getEqualsStringQueryBuilder() {
		return queryBuilderProvider.getEqualsStringQueryBuilder();
	}

	public IStringQueryBuilder getStartsWithStringQueryBuilder() {
		return queryBuilderProvider.getStartsWithStringQueryBuilder();
	}

	public String getXPathProcessorId() {
		return xpathProcessorId;
	}
}
