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
package org.eclipse.wst.xml.search.core.internal;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.search.core.ISimpleXMLSearchEngine;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AllFilesXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;
import org.eclipse.wst.xml.search.core.storage.StructuredStorageModelManager;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;

/**
 * 
 * Abstract class for XML Search engine which implements
 * {@link ISimpleXMLSearchEngine}.
 * 
 */
public abstract class AbstractXMLSearchEngine implements ISimpleXMLSearchEngine {

	/**
	 * Returns true if reporter is enabled and false otherwise.
	 * 
	 * @param reporter
	 *            the XML reporter or null otherwise.
	 * @return
	 */
	protected boolean isReporterEnabled(IXMLSearchReporter reporter) {
		return reporter != null && reporter.isEnabled();
	}

	/**
	 * Returns true if the given <code>resource</code> must be accepted and
	 * false Otherwise.
	 * 
	 * @param resource
	 * @param rootResource
	 * @param requestor
	 * @return
	 */
	protected boolean acceptResource(IResource resource,
			IResource rootResource, IXMLSearchRequestor requestor) {
		return requestor == null ? false : requestor.accept(resource,
				rootResource);
	}

	/**
	 * Process search DOM nodes in the given <code>file</code>.
	 * 
	 * @param file
	 *            used to process the search.
	 * @param rootResource
	 *            the root resources where search is started.
	 * @param requestor
	 *            used to know if search must be done for the given file.
	 * @param visitor
	 *            used to execute XPath query for the current file which is a
	 *            DOM Document.
	 * @param query
	 *            the XPath to execute.
	 * @param xpathProcessorId
	 *            the XPath processor id to use to execute XPath.
	 * @param namespaceInfos
	 *            namespace infos used by the XPath processor to execute the
	 *            given XPath query.
	 * @param collector
	 *            the collector used to collect DOM Node.
	 * @param selectedNode
	 *            the selected node which start the search.
	 * @return the status of the search.
	 */
	protected void processFile(IFile file, IResource rootResource,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathProcessorId, Namespaces namespaces,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			MultiStatus status) {
		if (acceptResource(file, rootResource, requestor)) {
			IDOMModel domModel = null;
			try {
				// Load DOM Document
				domModel = getDOMModel(file, status);
				if (domModel != null && requestor.accept(domModel)) {
					// DOM Model was loaded, visit the DOM Document
					try {
						process(visitor, query, xpathProcessorId, namespaces,
								collector, selectedNode, domModel, domModel);
					} catch (Throwable e) {
						// Error while visting DOM Document
						status.add(XMLSearchCorePlugin.createStatus(
								IStatus.ERROR,
								NLS.bind(
										Messages.searchEngineDOMDocumentVisitedError,
										file.getLocation().toString()), e));
					}
				}
			} finally {
				if (domModel != null) {
					domModel.releaseFromRead();
				}
			}
		}
	}

	/**
	 * Returns the DOM model for read from the given file.
	 * 
	 * @param file
	 *            the file of the DOM node.
	 * @param status
	 *            status errors.
	 * @return the DOM model for read from the given file.
	 * @throws IOException
	 * @throws CoreException
	 */
	private IDOMModel getDOMModel(IFile file, MultiStatus status) {
		try {
			IStructuredModel model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(file);
			if (model == null) {
				model = StructuredModelManager.getModelManager()
						.getModelForRead(file);
			}
			if (model instanceof IDOMModel) {
				return (IDOMModel) model;
			}
		} catch (Exception e) {
			status.add(XMLSearchCorePlugin.createStatus(IStatus.ERROR,
					e.getMessage(), e));
		}
		return null;
	}

	/**
	 * Process search DOM nodes in the given <code>storage</code>.
	 * 
	 * @param storage
	 *            used to process the search.
	 * @param rootStorage
	 *            the root storage where search is started.
	 * @param requestor
	 *            used to know if search must be done for the given file.
	 * @param visitor
	 *            used to execute XPath query for the current file which is a
	 *            DOM Document.
	 * @param query
	 *            the XPath to execute.
	 * @param xpathProcessorId
	 *            the XPath processor id to use to execute XPath.
	 * @param namespaceInfos
	 *            namespace infos used by the XPath processor to execute the
	 *            given XPath query.
	 * @param collector
	 *            the collector used to collect DOM Node.
	 * @param selectedNode
	 *            the selected node which start the search.
	 * @return the status of the search.
	 */
	protected void processStorage(IStorage storage, IStorage rootStorage,
			IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathProcessorId, Namespaces namespaces,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			MultiStatus status) {
		// Load DOM Document from the storage (JAR)
		IDOMModel domModel = getDOMModel(storage, status);
		if (domModel != null) {
			// DOM Model was loaded, visit the DOM Document
			try {
				process(visitor, query, xpathProcessorId, namespaces,
						collector, selectedNode, domModel, domModel);
			} catch (Throwable e) {
				// Error while visting DOM Document
				status.add(XMLSearchCorePlugin.createStatus(IStatus.ERROR, NLS
						.bind(Messages.searchEngineDOMDocumentVisitedError,
								storage.getFullPath().toString()), e));
			}
		}
	}

	/**
	 * Returns the DOM model for read from the given storage (ex : JAR).
	 * 
	 * @param storage
	 *            the file of the DOM node.
	 * @param status
	 *            status errors.
	 * @return the DOM model for read from the given file.
	 * @throws IOException
	 * @throws CoreException
	 */
	private IDOMModel getDOMModel(IStorage storage, MultiStatus status) {
		try {
			IStructuredModel model = StructuredStorageModelManager
					.getModelManager().getModel(storage);
			if (model instanceof IDOMModel) {
				return (IDOMModel) model;
			}
		} catch (Exception e) {
			status.add(XMLSearchCorePlugin.createStatus(IStatus.ERROR,
					e.getMessage(), e));
		}
		return null;
	}

	private void process(IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathProcessorId, Namespaces namespaces,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IStructuredModel model, IDOMModel domModel) {
		if (model != null) {
			IDOMDocument document = domModel.getDocument();
			if (namespaces != null) {
				NamespaceInfos namespaceInfos = XPathManager.getManager()
						.getNamespaceInfo(document);
				query = namespaces.format(query, namespaceInfos);
			}
			visitor.visit(document, query, xpathProcessorId, collector,
					selectedNode);
		}
	}

	/**
	 * Execute the XPath query for the given <code>files</code> and collect
	 * nodes result in the given <code>collector</code> and return the status of
	 * the search.
	 * 
	 * @param files
	 *            list of files where the query must be executed to collect DOM
	 *            nodes (if file is a DOM Document).
	 * @param visitor
	 *            used to execute XPath query for the current file which is a
	 *            DOM Document.
	 * @param query
	 *            the XPath to execute.
	 * @param xpathProcessorId
	 *            the XPath processor id to use to execute XPath.
	 * @param namespaceInfos
	 *            namespace infos used by the XPath processor to execute the
	 *            given XPath query.
	 * @param collector
	 *            the collector used to collect DOM Node.
	 * @param selectedNode
	 *            the selected node which start the search.
	 * @param monitor
	 *            the progress monitor.
	 * @return the status of the search.
	 */
	public IStatus search(IFile files[], IXMLSearchDOMDocumentVisitor visitor,
			String query, String xpathProcessorId, Namespaces namespaces,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor) {
		// Create status instance
		MultiStatus status = createStatus();
		// Execute search
		search(files, visitor, query, xpathProcessorId, namespaces, collector,
				selectedNode, monitor, status);
		// Returns status
		return status;
	}

	/**
	 * Execute the XPath query for the given <code>files</code> and collect
	 * nodes result in the given <code>collector</code> populate the given
	 * <code>status</code> of the search.
	 * 
	 * @param files
	 *            list of files where the query must be executed to collect DOM
	 *            nodes (if file is a DOM Document).
	 * @param visitor
	 *            used to execute XPath query for the current file which is a
	 *            DOM Document.
	 * @param query
	 *            the XPath to execute.
	 * @param xpathProcessorId
	 *            the XPath processor id to use to execute XPath.
	 * @param namespaceInfos
	 *            namespace infos used by the XPath processor to execute the
	 *            given XPath query.
	 * @param collector
	 *            the collector used to collect DOM Node.
	 * @param selectedNode
	 *            the selected node which start the search.
	 * @param monitor
	 *            the progress monitor.
	 * @param status
	 *            the status to populate while searching.
	 */
	protected void search(IFile files[], IXMLSearchDOMDocumentVisitor visitor,
			String query, String xpathProcessorId, Namespaces namespace,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IProgressMonitor monitor, MultiStatus status) {
		IFile file = null;
		// Loop for files
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			// process search for the current file
			processFile(file, file, AllFilesXMLSearchRequestor.INSTANCE,
					visitor, query, xpathProcessorId, namespace, collector,
					selectedNode, status);
		}
	}

	/**
	 * Create an instance of {@link MultiStatus}.
	 * 
	 * @return
	 */
	protected MultiStatus createStatus() {
		return new MultiStatus(XMLSearchCorePlugin.PLUGIN_ID, IStatus.OK,
				Messages.XMLSearchEngine_statusMessage, null);
	}
}
