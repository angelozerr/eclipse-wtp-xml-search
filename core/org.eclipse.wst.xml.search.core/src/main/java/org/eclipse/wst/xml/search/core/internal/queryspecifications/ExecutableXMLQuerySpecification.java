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
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.IExecutableXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;

/**
 * 
 * Implementation of {@link IExecutableXMLQuerySpecification}.
 * 
 */
public class ExecutableXMLQuerySpecification implements
		IExecutableXMLQuerySpecification {

	private final IXMLQuerySpecificationRegistry registry;
	private final IXMLQuerySpecification querySpecification;
	private final String query;
	private final Namespaces namespaceInfos;

	public ExecutableXMLQuerySpecification(
			IXMLQuerySpecificationRegistry registry,
			IXMLQuerySpecification querySpecification, String query,
			Namespaces namespaceInfos) {
		this.registry = registry;
		this.querySpecification = querySpecification;
		this.query = query;
		this.namespaceInfos = namespaceInfos;
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
		return querySpecification.getResource(selectedNode, resource);
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
		return querySpecification.getResources(selectedNode, resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification
	 * #isMultiResource()
	 */
	public boolean isMultiResource() {
		return querySpecification.isMultiResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.container.
	 * IStorageProvider
	 * #getStorage(org.eclipse.wst.xml.core.internal.provisional.
	 * document.IDOMNode, org.eclipse.core.resources.IResource)
	 */
	public IStorage getStorage(Object selectedNode, IResource resource) {
		return querySpecification.getStorage(selectedNode, resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.container.
	 * IMultiStorageProvider
	 * #getStorages(org.eclipse.wst.xml.core.internal.provisional
	 * .document.IDOMNode, org.eclipse.core.resources.IResource)
	 */
	public IStorage[] getStorages(Object selectedNode, IResource resource) {
		return querySpecification.getStorages(selectedNode, resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification
	 * #isSimpleStorage()
	 */
	public boolean isSimpleStorage() {
		return querySpecification.isSimpleStorage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification
	 * #isMultiStorage()
	 */
	public boolean isMultiStorage() {
		return querySpecification.isMultiStorage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.requestor.
	 * IXMLSearchRequestorProvider#getRequestor()
	 */
	public IXMLSearchRequestor getRequestor() {
		return querySpecification.getRequestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.visitor.
	 * IXMLSearchVisitorProvider#getVisitor()
	 */
	public IXMLSearchDOMDocumentVisitor getVisitor() {
		return querySpecification.getVisitor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.
	 * IExecutableXMLQuerySpecification#getSelectedNode()
	 */
	public Object getSelectedNode() {
		return registry.getSelectedNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.
	 * IExecutableXMLQuerySpecification#getQuery()
	 */
	public String getQuery() {
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.
	 * IExecutableXMLQuerySpecification#getNamespaces()
	 */
	public Namespaces getNamespaces() {
		return namespaceInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.queryspecifications.IXPathEvaluatorIdProvider
	 * #getXPathProcessorId()
	 */
	public String getXPathProcessorId() {
		return querySpecification.getXPathProcessorId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.
	 * IStringQueryBuilderProvider#getEqualsStringQueryBuilder()
	 */
	public IStringQueryBuilder getEqualsStringQueryBuilder() {
		return querySpecification.getEqualsStringQueryBuilder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.
	 * IStringQueryBuilderProvider#getStartsWithStringQueryBuilder()
	 */
	public IStringQueryBuilder getStartsWithStringQueryBuilder() {
		return querySpecification.getStartsWithStringQueryBuilder();
	}
}
