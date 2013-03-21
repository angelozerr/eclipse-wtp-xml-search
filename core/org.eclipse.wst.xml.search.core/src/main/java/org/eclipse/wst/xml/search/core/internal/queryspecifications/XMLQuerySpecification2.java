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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.DefaultStringQueryBuilderProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AllXMLExtensionFilesXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.XPathNodeSetSearchVisitor;

/**
 * 
 * Implementation of {@link IXMLQuerySpecification} which doesn't use providers
 * (it use directly {@link IResource} instead of using {@link IResourceProvider}
 * ).
 * 
 */
public class XMLQuerySpecification2 implements IXMLQuerySpecification {

	private IResource container;
	private IResource[] containers;
	private IXMLSearchRequestor requestor;
	private IXMLSearchDOMDocumentVisitor visitor;
	private IStringQueryBuilder equalsStringQueryBuilder;
	private IStringQueryBuilder startsWithStringQueryBuilder;
	private String xpathEvaluatorId;

	public XMLQuerySpecification2(IResource container, IResource[] containers,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor,
			IStringQueryBuilder equalsStringQueryBuilder,
			IStringQueryBuilder startsWithStringQueryBuilder,
			String xpathEvaluatorId) {
		this.container = container;
		this.requestor = requestor;
		this.visitor = visitor;
		this.equalsStringQueryBuilder = equalsStringQueryBuilder;
		this.startsWithStringQueryBuilder = startsWithStringQueryBuilder;
	}

	public static XMLQuerySpecification2 newDefaultQuerySpecification(
			IResource container) {
		return new XMLQuerySpecification2(container, null,
				AllXMLExtensionFilesXMLSearchRequestor.INSTANCE,
				XPathNodeSetSearchVisitor.INSTANCE,
				DefaultStringQueryBuilderProvider.INSTANCE
						.getEqualsStringQueryBuilder(),
				DefaultStringQueryBuilderProvider.INSTANCE
						.getStartsWithStringQueryBuilder(), null);
	}

	public static XMLQuerySpecification2 newDefaultQuerySpecification(
			IResource[] containers) {
		return new XMLQuerySpecification2(null, containers,
				AllXMLExtensionFilesXMLSearchRequestor.INSTANCE,
				XPathNodeSetSearchVisitor.INSTANCE,
				DefaultStringQueryBuilderProvider.INSTANCE
						.getEqualsStringQueryBuilder(),
				DefaultStringQueryBuilderProvider.INSTANCE
						.getStartsWithStringQueryBuilder(), null);
	}

	public boolean isMultiResource() {
		return containers != null;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return container;
	}

	public IResource[] getResources(Object selectedNode, IResource resource) {
		return containers;
	}

	public void setContainer(IContainer container) {
		this.container = container;
	}

	public IXMLSearchRequestor getRequestor() {
		return requestor;
	}

	public void setRequestor(IXMLSearchRequestor requestor) {
		this.requestor = requestor;
	}

	public IXMLSearchDOMDocumentVisitor getVisitor() {
		return visitor;
	}

	public void setVisitor(IXMLSearchDOMDocumentVisitor visitor) {
		this.visitor = visitor;
	}

	public IStringQueryBuilder getEqualsStringQueryBuilder() {
		return equalsStringQueryBuilder;
	}

	public void setEqualsStringQueryBuilder(
			IStringQueryBuilder equalsStringQueryBuilder) {
		this.equalsStringQueryBuilder = equalsStringQueryBuilder;
	}

	public IStringQueryBuilder getStartsWithStringQueryBuilder() {
		return startsWithStringQueryBuilder;
	}

	public void setStartsWithStringQueryBuilder(
			IStringQueryBuilder startsWithStringQueryBuilder) {
		this.startsWithStringQueryBuilder = startsWithStringQueryBuilder;
	}

	public String getXPathProcessorId() {
		return xpathEvaluatorId;
	}

	public void setXPathProcessorId(String xpathEvaluatorId) {
		this.xpathEvaluatorId = xpathEvaluatorId;
	}

	public boolean isMultiStorage() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSimpleStorage() {
		// TODO Auto-generated method stub
		return false;
	}

	public IStorage getStorage(Object selectedNode, IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	public IStorage[] getStorages(Object selectedNode, IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
