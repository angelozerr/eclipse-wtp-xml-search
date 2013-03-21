/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.internal;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.IXMLSearchEngine;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;
import org.eclipse.wst.xml.search.ui.XMLMatch;

/**
 * {@link ISearchQuery} implementation for DOM-SSE.
 * 
 */
public class XMLSearchQuery implements ISearchQuery, IXMLSearchDOMNodeCollector {

	private IContainer container;
	private IXMLSearchRequestor scope;
	private IXMLSearchDOMDocumentVisitor visitor;
	private String query;
	private String xpathEvaluatorId;
	private Namespaces namespaceInfos;
	private ISearchResult fResult;
	private IDOMNode selectedNode;
	private IXMLSearchEngine engine;
	private IXMLQuerySpecificationRegistry querySpecificationRegistry;
	private long startTime = 0;
	private long endTime = 0;
	private IXMLSearchReporter reporter;

	public XMLSearchQuery(IContainer container, IXMLSearchRequestor scope,
			IXMLSearchDOMDocumentVisitor visitor, String query,
			String xpathEvaluatorId, Namespaces namespaceInfos,
			IDOMNode selectedNode, IXMLSearchEngine engine,
			IXMLSearchReporter reporter) {
		this.container = container;
		this.scope = scope;
		this.visitor = visitor;
		this.query = query;
		this.namespaceInfos = namespaceInfos;
		this.xpathEvaluatorId = xpathEvaluatorId;
		this.selectedNode = selectedNode;
		this.engine = engine;
		this.reporter = reporter;
	}

	public XMLSearchQuery(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchEngine engine, IXMLSearchReporter reporter) {
		this.engine = engine;
		this.querySpecificationRegistry = querySpecificationRegistry;
		this.reporter = reporter;
	}

	public boolean canRerun() {
		return true;
	}

	public boolean canRunInBackground() {
		return true;
	}

	public String getLabel() {
		return Messages.XMLSearchQuery_label;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getResultLabel(int nMatches) {
		long time = 0;
		if (startTime > 0) {
			if (endTime > 0) {
				time = endTime - startTime;
			} else {
				time = System.currentTimeMillis() - startTime;
			}
		}
		if (querySpecificationRegistry != null) {
			String label = querySpecificationRegistry.getQueriesLabel();
			Object[] values = { label, nMatches, time };
			return NLS.bind(Messages.XMLSearchQuery_xpathPattern, values);
		}
		Object[] values = { query, nMatches, time };
		return NLS.bind(Messages.XMLSearchQuery_xpathPattern, values);
	}

	public ISearchResult getSearchResult() {
		if (fResult == null) {
			XMLSearchResult result = new XMLSearchResult(this);
			// TODO : manage SearchResultUpdater
			// new SearchResultUpdater(result);
			fResult = result;
		}
		return fResult;
	}

	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		startTime = System.currentTimeMillis();
		XMLSearchResult xmlResult = (XMLSearchResult) getSearchResult();
		xmlResult.removeAll();
		if (querySpecificationRegistry != null) {
			return engine.search(querySpecificationRegistry, this, reporter,
					monitor);
		}
		try {
			return engine.search(container, scope, visitor, query,
					xpathEvaluatorId, namespaceInfos, this, selectedNode,
					reporter, monitor);
		} finally {
			endTime = System.currentTimeMillis();
		}
	}

	public boolean add(IDOMNode node) {
		XMLSearchResult xmlResult = (XMLSearchResult) getSearchResult();
		xmlResult.addMatch(new XMLMatch(node));
		return true;
	}

}
