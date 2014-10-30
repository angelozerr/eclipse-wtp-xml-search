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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;

public interface ISimpleXMLSearchEngine {

	IStatus search(IResource container, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor);

	IStatus search(IResource[] containers, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor);

	IStatus search(IStorage storage, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor);

	IStatus search(IStorage[] storages, IXMLSearchRequestor requestor,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IXMLSearchDOMNodeCollector collector, Object selectedNode,
			IXMLSearchReporter reporter, IProgressMonitor monitor);
}
