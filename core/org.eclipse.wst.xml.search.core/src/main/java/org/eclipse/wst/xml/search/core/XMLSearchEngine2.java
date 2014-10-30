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

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.xml.search.core.internal.FilesOfScopeCalculator;
import org.eclipse.wst.xml.search.core.internal.Messages;
import org.eclipse.wst.xml.search.core.queryspecifications.IExecutableXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;

public class XMLSearchEngine2 extends XMLSearchEngine {

	private int fNumberOfFilesToScan;
	private IFile fCurrentFile;
	private int fNumberOfScannedFiles;

	@Override
	protected void search(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector,
			final IProgressMonitor monitor, MultiStatus status) {

		IFile[] files = evaluateFilesInScope(querySpecificationRegistry, status);
		if (files == null) {
			return;
		}
		fNumberOfScannedFiles = 0;
		fNumberOfFilesToScan = files.length;
		fCurrentFile = null;

		Job monitorUpdateJob = new Job(
				Messages.XMLSearchEngine2_progress_updating_job) {
			private int fLastNumberOfScannedFiles = 0;

			public IStatus run(IProgressMonitor inner) {
				while (!inner.isCanceled()) {
					IFile file = fCurrentFile;
					if (file != null) {
						String fileName = file.getName();
						Object[] args = { fileName,
								new Integer(fNumberOfScannedFiles),
								new Integer(fNumberOfFilesToScan) };
						monitor.subTask(NLS.bind(
								Messages.XMLSearchEngine2_scanning, args));
						int steps = fNumberOfScannedFiles
								- fLastNumberOfScannedFiles;
						monitor.worked(steps);
						fLastNumberOfScannedFiles += steps;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						return Status.OK_STATUS;
					}
				}
				return Status.OK_STATUS;
			}
		};

		try {
			String taskName = NLS.bind(
					Messages.XMLSearchEngine2_textsearch_task_label,
					querySpecificationRegistry.getQueriesLabel());
			// SearchMessages.TextSearchVisitor_filesearch_task_label
			// :
			// Messages.format(SearchMessages.TextSearchVisitor_textsearch_task_label,
			// fMatcher.pattern().pattern());
			monitor.beginTask(taskName, fNumberOfFilesToScan);
			monitorUpdateJob.setSystem(true);
			monitorUpdateJob.schedule();
			try {
				// fCollector.beginReporting();
				// processFiles(files);
				// return fStatus;
				processFiles(files, null, querySpecificationRegistry,
						collector, monitor, status);
			} finally {
				monitorUpdateJob.cancel();
			}
		} finally {
			monitor.done();
			// fCollector.endReporting();
		}

	}

	private void processFiles(IFile[] files, IResource rootResource,
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector, IProgressMonitor monitor,
			MultiStatus status) {
		for (int i = 0; i < files.length; i++) {
			fCurrentFile = files[i];
			boolean res = processFile(fCurrentFile, rootResource,
					querySpecificationRegistry, collector, monitor, status);
			if (!res)
				break;
		}

	}

	private boolean processFile(IFile file, IResource rootResource,
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector, IProgressMonitor monitor,
			MultiStatus status) {

		try {
			Collection<Collection<IExecutableXMLQuerySpecification>> all = querySpecificationRegistry
					.getQuerySpecificationsMap().values();
			for (Collection<IExecutableXMLQuerySpecification> collection : all) {
				super.processFile(file, rootResource, collection, collector,
						status);
			}
		} finally {
			fNumberOfScannedFiles++;
		}
		if (monitor.isCanceled())
			throw new OperationCanceledException(
					Messages.XMLSearchEngine2_canceled);
		return true;
	}

	/**
	 * Evaluates all files in this scope.
	 * 
	 * @param status
	 *            a {@link MultiStatus} to collect the error status that
	 *            occurred while collecting resources.
	 * @return returns the files in the scope.
	 */
	protected IFile[] evaluateFilesInScope(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			MultiStatus status) {
		return new FilesOfScopeCalculator(querySpecificationRegistry, status)
				.process();
	}

}
