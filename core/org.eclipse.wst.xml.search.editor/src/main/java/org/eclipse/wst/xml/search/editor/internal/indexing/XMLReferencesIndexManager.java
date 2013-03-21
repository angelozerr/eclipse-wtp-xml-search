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
package org.eclipse.wst.xml.search.editor.internal.indexing;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;

public class XMLReferencesIndexManager {

	// for debugging
	static final boolean DEBUG;
	static {
		String value = Platform
				.getDebugOption("org.eclipse.wst.xml.search.editor/debug/indexmanager"); //$NON-NLS-1$
		DEBUG = value != null && value.equalsIgnoreCase("true"); //$NON-NLS-1$
	}

	public static XMLReferencesIndexManager INSTANCE = new XMLReferencesIndexManager();

	private Map<IProject, Map<String, Collection<IFile>>> indexedFiles = null;

	public static XMLReferencesIndexManager getDefault() {
		return INSTANCE;
	}

	public XMLReferencesIndexManager() {
		indexedFiles = new HashMap<IProject, Map<String, Collection<IFile>>>();
	}

	public Collection<IFile> getIndexedFiles(IProject project,
			String contentTypeId, IProgressMonitor monitor) {
		indexFilesIfNeeded(project, monitor);
		Map<String, Collection<IFile>> files = indexedFiles.get(project);
		if (files == null) {
			return Collections.emptyList();
		}
		Collection<IFile> f = files.get(contentTypeId);
		if (f == null) {
			return Collections.emptyList();
		}
		return f;
	}

	private void indexFilesIfNeeded(IProject project, IProgressMonitor monitor) {
		Map<String, Collection<IFile>> files = indexedFiles.get(project);
		if (files == null) {
			files = new HashMap<String, Collection<IFile>>();
			run(project, files, monitor);
			indexedFiles.put(project, files);
		}
	}

	public void flushIndexedFiles(IJavaProject javaProject) {
		if (javaProject == null) {
			indexedFiles.clear();
		} else {
			IProject project = javaProject.getProject();
			indexedFiles.remove(project);
			IProject[] referencingProjects = project.getReferencingProjects();
			for (IProject referencingProject : referencingProjects) {
				indexedFiles.remove(referencingProject);
			}
		}
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	private IStatus run(IProject project, Map<String, Collection<IFile>> files,
			IProgressMonitor monitor) {

		IStatus status = Status.OK_STATUS;

		if (monitor.isCanceled()) {
			// setCanceledState();
			return Status.CANCEL_STATUS;
		}

		if (DEBUG)
			System.out.println(" ^ IndexWorkspaceJob started: "); //$NON-NLS-1$

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		long start = System.currentTimeMillis();

		try {
			XMLReferencesFileVisitor visitor = new XMLReferencesFileVisitor(
					files, monitor);
			// collect all jsp files
			if (project != null) {
				project.accept(visitor, IResource.DEPTH_INFINITE);
				// IProject[] referencingProjects = project
				// .getReferencingProjects();
				// for (int i = 0; i < referencingProjects.length; i++) {
				// referencingProjects[i].accept(visitor,
				// IResource.DEPTH_INFINITE);
				// }
			} else {
				ResourcesPlugin.getWorkspace().getRoot().accept(visitor,
						IResource.DEPTH_INFINITE);
			}
			// request indexing
			// this is pretty much like faking an entire workspace resource
			// delta
			// XMLReferenceIndexManager.getInstance().indexFiles(
			// visitor.getFiles());
		} catch (CoreException e) {
			if (DEBUG)
				e.printStackTrace();
		} finally {
			monitor.done();
		}
		long finish = System.currentTimeMillis();
		if (DEBUG)
			System.out
					.println(" ^ IndexWorkspaceJob finished\n   total time running: " + (finish - start)); //$NON-NLS-1$

		return status;
	}

}
