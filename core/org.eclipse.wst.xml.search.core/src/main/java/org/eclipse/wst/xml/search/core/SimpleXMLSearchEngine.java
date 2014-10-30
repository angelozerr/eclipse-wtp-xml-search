/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.xml.search.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.core.internal.AbstractXMLSearchEngine;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.internal.reporter.XMLSearchReporterIdProvider;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;

public class SimpleXMLSearchEngine extends AbstractXMLSearchEngine {

	private static final ISimpleXMLSearchEngine INSTANCE = new SimpleXMLSearchEngine();

	public static ISimpleXMLSearchEngine getDefault() {
		return INSTANCE;
	}

	public IStatus search(IResource container, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		long startTime = -1;
		int searchId = -1;
		if (isReporterEnabled(reporter)) {
			searchId = XMLSearchReporterIdProvider.getSearchId();
			startTime = System.currentTimeMillis();
			Collection<String> queries = new ArrayList<String>();
			queries.add(query);
			Map<IResource, Collection<String>> containersReporter = new HashMap<IResource, Collection<String>>();
			containersReporter.put(container, queries);
			reporter.beginSearch(searchId, containersReporter);
		}
		MultiStatus status = createStatus();
		try {
			search(container, container, requestor, visitor, query,
					xpathEvaluatorId, namespaceInfos, collector, selectedNode,
					monitor, status);
		} finally {
			if (searchId != -1) {
				reporter.endSearch(searchId, System.currentTimeMillis()
						- startTime);
			}
		}
		return status;
	}

	public IStatus search(IResource[] containers,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		long startTime = -1;
		int searchId = -1;
		if (isReporterEnabled(reporter)) {
			searchId = XMLSearchReporterIdProvider.getSearchId();
			startTime = System.currentTimeMillis();
			Map<IResource, Collection<String>> containersReporter = new HashMap<IResource, Collection<String>>();
			if (containers != null) {
				Collection<String> queries = new ArrayList<String>();
				queries.add(query);
				for (int i = 0; i < containers.length; i++) {
					containersReporter.put(containers[i], queries);
				}
			}
			reporter.beginSearch(searchId, containersReporter);
		}
		MultiStatus status = createStatus();
		try {
			if (containers == null) {
				return Status.CANCEL_STATUS;
			}

			IResource container = null;
			for (int i = 0; i < containers.length; i++) {
				container = containers[i];
				search(container, container, requestor, visitor, query,
						xpathEvaluatorId, namespaceInfos, collector,
						selectedNode, monitor, status);
			}
		} finally {
			if (searchId != -1) {
				reporter.endSearch(searchId, System.currentTimeMillis()
						- startTime);
			}
		}
		return status;
	}

	private void search(IResource resource, IResource rootResource,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor, MultiStatus status) {
		if (acceptResource(resource, rootResource, requestor)) {
			int resourceType = resource.getType();
			switch (resourceType) {
			case IResource.FILE:
				IFile file = (IFile) resource;
				processFile(file, rootResource, requestor, visitor, query,
						xpathEvaluatorId, namespaceInfos, collector,
						selectedNode, status);
				break;
			case IResource.ROOT:
			case IResource.PROJECT:
			case IResource.FOLDER:
				try {
					IResource[] resources = ((IContainer) resource).members();
					search(resources, rootResource, requestor, visitor, query,
							xpathEvaluatorId, namespaceInfos, collector,
							selectedNode, monitor, status);
				} catch (CoreException e) {
					// Error while loop for files
					status.add(XMLSearchCorePlugin.createStatus(IStatus.ERROR,
							e.getMessage(), e));
				}
				break;
			}
		}
	}

	private void search(IResource[] resources, IResource rootResource,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor, MultiStatus status) {
		for (int i = 0; i < resources.length; i++) {
			search(resources[i], rootResource, requestor, visitor, query,
					xpathEvaluatorId, namespaceInfos, collector, selectedNode,
					monitor, status);
		}
	}

	public IStatus search(IStorage storage, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		long startTime = -1;
		int searchId = -1;
		if (isReporterEnabled(reporter)) {
			searchId = XMLSearchReporterIdProvider.getSearchId();
			startTime = System.currentTimeMillis();

		}
		MultiStatus status = createStatus();
		try {
			if (storage == null) {
				return Status.CANCEL_STATUS;
			}
			super.processStorage(storage, storage, requestor, visitor, query,
					xpathEvaluatorId, namespaceInfos, collector, selectedNode,
					status);

		} finally {
			if (searchId != -1) {
				reporter.endSearch(searchId, System.currentTimeMillis()
						- startTime);
			}
		}
		return status;
	}

	private void search(IStorage storage, IStorage rootStorage,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor, MultiStatus status) {
		super.processStorage(storage, rootStorage, requestor, visitor, query,
				xpathEvaluatorId, namespaceInfos, collector, selectedNode,
				status);

	}

	public IStatus search(IStorage[] storages, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor) {
		long startTime = -1;
		int searchId = -1;
		if (isReporterEnabled(reporter)) {
			searchId = XMLSearchReporterIdProvider.getSearchId();
			startTime = System.currentTimeMillis();

		}
		MultiStatus status = createStatus();
		try {
			if (storages == null) {
				return Status.CANCEL_STATUS;
			}

			IStorage storage = null;
			for (int i = 0; i < storages.length; i++) {
				storage = storages[i];
				super.processStorage(storage, storage, requestor, visitor,
						query, xpathEvaluatorId, namespaceInfos, collector,
						selectedNode, status);
			}
		} finally {
			if (searchId != -1) {
				reporter.endSearch(searchId, System.currentTimeMillis()
						- startTime);
			}
		}
		return status;
	}
}
