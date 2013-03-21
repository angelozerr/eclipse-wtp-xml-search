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
package org.eclipse.wst.xml.search.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.xml.search.core.internal.AbstractXMLSearchEngine;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.queryspecifications.XMLQuerySpecification2;
import org.eclipse.wst.xml.search.core.internal.reporter.XMLSearchReporterIdProvider;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.IExecutableXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.XMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AllFilesXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;

public class XMLSearchEngine extends AbstractXMLSearchEngine implements
		IXMLSearchEngine {

	private static final XMLSearchEngine INSTANCE = new XMLSearchEngine();

	public static IXMLSearchEngine getDefault() {
		return INSTANCE;
	}

	protected void search(IFile[] files, IXMLSearchDOMDocumentVisitor visitor,
			String query, String xpathEvaluatorId,
			Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor, MultiStatus status) {
		IFile file = null;
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			processFile(file, file, AllFilesXMLSearchRequestor.INSTANCE,
					visitor, query, xpathEvaluatorId, namespaceInfos,
					collector, selectedNode, status);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.IXMLSearchEngine#search(org.eclipse.wst
	 * .xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry,
	 * org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus search(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector, IXMLSearchReporter reporter,
			IProgressMonitor monitor) {
		long startTime = -1;
		int searchId = -1;
		if (isReporterEnabled(reporter)) {
			searchId = XMLSearchReporterIdProvider.getSearchId();
			startTime = System.currentTimeMillis();

			Map<IResource, Collection<String>> containersReporter = new HashMap<IResource, Collection<String>>();
			Set<Entry<IResource, Collection<IExecutableXMLQuerySpecification>>> entries = querySpecificationRegistry
					.getQuerySpecificationsMap().entrySet();
			for (Entry<IResource, Collection<IExecutableXMLQuerySpecification>> entry : entries) {
				IResource container = entry.getKey();
				Collection<String> queries = new ArrayList<String>();
				Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications = entry
						.getValue();
				for (IExecutableXMLQuerySpecification executableXMLQuerySpecification : executableXMLQuerySpecifications) {
					queries.add(executableXMLQuerySpecification.getQuery());
				}
				containersReporter.put(container, queries);
			}
			reporter.beginSearch(searchId, containersReporter);
		}
		MultiStatus status = createStatus();
		try {
			search(querySpecificationRegistry, collector, monitor, status);
		} finally {
			if (searchId != -1) {
				reporter.endSearch(searchId, System.currentTimeMillis()
						- startTime);
			}
		}
		return status;
	}

	protected void search(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector, IProgressMonitor monitor,
			MultiStatus status) {
		Set<Entry<IResource, Collection<IExecutableXMLQuerySpecification>>> entries = querySpecificationRegistry
				.getQuerySpecificationsMap().entrySet();
		for (Entry<IResource, Collection<IExecutableXMLQuerySpecification>> entry : entries) {
			IResource container = entry.getKey();
			Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications = entry
					.getValue();
			search(container, container, executableXMLQuerySpecifications,
					collector, status);
		}
	}

	protected void search(
			IResource resource,
			IResource rootResource,
			Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications,
			IXMLSearchDOMNodeCollector collector, MultiStatus status) {
		if (acceptResource(resource, rootResource,
				executableXMLQuerySpecifications)) {
			int resourceType = resource.getType();
			switch (resourceType) {
			case IResource.FILE:
				IFile file = (IFile) resource;
				processFile(file, rootResource,
						executableXMLQuerySpecifications, collector, status);
				break;
			case IResource.ROOT:
			case IResource.PROJECT:
			case IResource.FOLDER:
				try {
					IResource[] resources = ((IContainer) resource).members();
					search(resources, rootResource,
							executableXMLQuerySpecifications, collector, status);
				} catch (CoreException e) {
					// Error while loop for files
					status.add(XMLSearchCorePlugin.createStatus(IStatus.ERROR,
							e.getMessage(), e));
				}
				break;
			}
		}
	}

	private void search(
			IResource[] resources,
			IResource rootResource,
			Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications,
			IXMLSearchDOMNodeCollector collector, MultiStatus status) {
		for (int i = 0; i < resources.length; i++) {
			search(resources[i], rootResource,
					executableXMLQuerySpecifications, collector, status);
		}
	}

	private boolean acceptResource(
			IResource resource,
			IResource rootResource,
			Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications) {
		if (resource == null) {
			return false;
		}
		for (IExecutableXMLQuerySpecification executableXMLQuerySpecification : executableXMLQuerySpecifications) {
			if (acceptResource(resource, rootResource,
					executableXMLQuerySpecification)) {
				return true;
			}
		}
		return false;
	}

	private boolean acceptResource(IResource resource, IResource rootResource,
			IExecutableXMLQuerySpecification executableXMLQuerySpecification) {
		IXMLSearchRequestor requestor = executableXMLQuerySpecification
				.getRequestor();
		return requestor != null && requestor.accept(resource, rootResource);
	}

	protected void processFile(
			IFile file,
			IResource rootResource,
			Collection<IExecutableXMLQuerySpecification> executableXMLQuerySpecifications,
			IXMLSearchDOMNodeCollector collector, MultiStatus status) {
		for (IExecutableXMLQuerySpecification executableXMLQuerySpecification : executableXMLQuerySpecifications) {
			if (acceptResource(file, rootResource,
					executableXMLQuerySpecification)) {
				super.processFile(file, rootResource,
						executableXMLQuerySpecification.getRequestor(),
						executableXMLQuerySpecification.getVisitor(),
						executableXMLQuerySpecification.getQuery(),
						executableXMLQuerySpecification.getXPathProcessorId(),
						executableXMLQuerySpecification.getNamespaces(),
						collector,
						executableXMLQuerySpecification.getSelectedNode(),
						status);
			}
		}
	}

	public IStatus search(IResource container, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathProcessorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		XMLQuerySpecificationRegistry querySpecificationRegistry = new XMLQuerySpecificationRegistry(
				container, selectedNode);
		XMLQuerySpecification2 specification = XMLQuerySpecification2
				.newDefaultQuerySpecification(container);
		specification.setVisitor(visitor);
		specification.setRequestor(requestor);
		specification.setXPathProcessorId(xpathProcessorId);
		querySpecificationRegistry.register(specification, query,
				namespaceInfos);
		return search(querySpecificationRegistry, collector, reporter, monitor);
	}

	public IStatus search(IResource[] containers,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathProcessorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		XMLQuerySpecificationRegistry querySpecificationRegistry = new XMLQuerySpecificationRegistry(
				(IContainer) null, selectedNode);
		XMLQuerySpecification2 specification = XMLQuerySpecification2
				.newDefaultQuerySpecification(containers);
		specification.setVisitor(visitor);
		specification.setRequestor(requestor);
		specification.setXPathProcessorId(xpathProcessorId);
		querySpecificationRegistry.register(specification, query,
				namespaceInfos);
		return search(querySpecificationRegistry, collector, reporter, monitor);
	}

	public IStatus search(IStorage storage, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	public IStatus search(IStorage[] storages, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}
}
