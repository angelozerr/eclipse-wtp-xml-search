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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.Match;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.IXMLSearchEngine;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;
import org.eclipse.wst.xml.search.ui.XMLMatch;
import org.eclipse.wst.xml.search.ui.internal.participant.SearchParticipantDescriptor;
import org.eclipse.wst.xml.search.ui.internal.participant.SearchParticipantsExtensionPoint;
import org.eclipse.wst.xml.search.ui.participant.IMatchPresentation;
import org.eclipse.wst.xml.search.ui.participant.IQueryParticipant;
import org.eclipse.wst.xml.search.ui.participant.ISearchRequestor;

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
	private final SearchParticipantDescriptor[] participantDescriptors;
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
		this.participantDescriptors = null;
	}

	public XMLSearchQuery(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			SearchParticipantDescriptor[] participantDescriptors,
			IXMLSearchEngine engine, IXMLSearchReporter reporter) {
		this.engine = engine;
		this.querySpecificationRegistry = querySpecificationRegistry;
		this.participantDescriptors = participantDescriptors;
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

	private static class SearchRequestor implements ISearchRequestor {
		private IQueryParticipant fParticipant;
		private XMLSearchResult fSearchResult;

		public void reportMatch(Match match) {
			IMatchPresentation participant = fParticipant.getUIParticipant();
			if (participant == null || match.getElement() instanceof IDOMNode
					|| match.getElement() instanceof IResource) {
				fSearchResult.addMatch(match);
			} else {
				fSearchResult.addMatch(match, participant);
			}
		}

		protected SearchRequestor(IQueryParticipant participant,
				XMLSearchResult result) {
			super();
			fParticipant = participant;
			fSearchResult = result;
		}
	}

	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		startTime = System.currentTimeMillis();
		XMLSearchResult xmlResult = (XMLSearchResult) getSearchResult();
		xmlResult.removeAll();

		if (querySpecificationRegistry != null) {
			// Search from Editor

			final Object selectedNode = querySpecificationRegistry
					.getSelectedNode();

			if (participantDescriptors != null) {
				final int[] ticks = new int[participantDescriptors.length];
				for (int i = 0; i < participantDescriptors.length; i++) {
					final int iPrime = i;
					ISafeRunnable runnable = new ISafeRunnable() {
						public void handleException(Throwable exception) {
							ticks[iPrime] = 0;
							String message = Messages.XMLSearchQuery_error_participant_estimate;
							XMLSearchUIPlugin.log(new Status(IStatus.ERROR,
									XMLSearchUIPlugin.PLUGIN_ID, 0, message,
									exception));
						}

						public void run() throws Exception {
							ticks[iPrime] = 1;// participantDescriptors[iPrime].getParticipant().estimateTicks(fPatternData);
						}
					};

					SafeRunner.run(runnable);
					// totalTicks+= ticks[i];
				}
			}

			IStatus status = engine.search(querySpecificationRegistry, this,
					reporter, monitor);

			if (participantDescriptors != null) {

				for (int i = 0; i < participantDescriptors.length; i++) {
					final ISearchRequestor requestor = new SearchRequestor(
							participantDescriptors[i].getParticipant(),
							xmlResult);
					final IProgressMonitor participantPM = new SubProgressMonitor(
							monitor, 1);

					final int iPrime = i;
					ISafeRunnable runnable = new ISafeRunnable() {
						public void handleException(Throwable exception) {
							participantDescriptors[iPrime].disable();
							String message = Messages.XMLSearchQuery_error_participant_search;
							XMLSearchUIPlugin.log(new Status(IStatus.ERROR,
									XMLSearchUIPlugin.PLUGIN_ID, 0, message,
									exception));
						}

						public void run() throws Exception {

							final IQueryParticipant participant = participantDescriptors[iPrime]
									.getParticipant();

							// final PerformanceStats stats=
							// PerformanceStats.getStats(PERF_SEARCH_PARTICIPANT,
							// participant);
							// stats.startRun();

							participant.search(requestor, selectedNode,
									participantPM);

							// stats.endRun();
						}
					};

					SafeRunner.run(runnable);
				}
			}
			return status;
		}
		try {
			// Search from UI Dialog
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
