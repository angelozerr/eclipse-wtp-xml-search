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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.FileUtils;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesManager;
import org.eclipse.wst.xml.search.editor.internal.indexing.XMLReferencesIndexManager;

/**
 * Visitor which retrieves XML references files.
 */
class XMLReferencesFileVisitor implements IResourceProxyVisitor {

	private final IProgressMonitor monitor;
	private final Map<String, Collection<IFile>> files;

	public XMLReferencesFileVisitor(Map<String, Collection<IFile>> files,
			IProgressMonitor monitor) {
		this.monitor = monitor;
		this.files = files;
	}

	public boolean visit(IResourceProxy proxy) throws CoreException {

		// check job canceled
		if (this.monitor != null && this.monitor.isCanceled()) {
			// setCanceledState();
			return false;
		}

		if (proxy.getType() == IResource.FILE) {

			// https://w3.opensource.ibm.com/bugzilla/show_bug.cgi?id=3553
			// check this before description
			// check name before actually getting the file (less work)
			if (isXMLFile(proxy.getName())) {
				IResource resource = proxy.requestResource();
				String contentTypeId = isXMLReferenceResource(resource);
				if (contentTypeId != null) {
					IFile file = (IFile) resource;
					if (XMLReferencesIndexManager.DEBUG)
						System.out
								.println("(+) XMLReferencesFileVisitor adding file: " + file.getName()); //$NON-NLS-1$
					// this call will check the ContentTypeDescription,
					// so
					// don't need to do it here.
					addXMLFile(file, contentTypeId, files);
					// this.files.add(file);
					this.monitor.subTask(proxy.getName());

					// don't search deeper for files
					return false;
				}
			}
		}
		return true;
	}

	private boolean isXMLFile(String filename) {
		return FileUtils.isXMLFile(filename);
	}

	private String isXMLReferenceResource(IResource resource) {
		if (resource == null) {
			return null;
		}
		if (!resource.exists()) {
			return null;
		}
		if (resource.getType() != IResource.FILE) {
			return null;
		}
		Collection<String> contentTypeIds = XMLReferencesManager.getInstance()
				.getContentTypeIds();
		String contentTypeId = DOMUtils
				.getStructuredModelContentTypeId((IFile) resource);
		if (contentTypeId == null) {
			return null;
		}
		if (contentTypeIds.contains(contentTypeId)) {
			return contentTypeId;
		}
		return null;
	}

	public void addXMLFile(IFile file, String contentTypeId,
			Map<String, Collection<IFile>> indexedFiles) {
		if (!file.isAccessible()) {
			return;
		}
		// String contentTypeId =
		// DOMUtils.getStructuredModelContentTypeId(file);
		// if (contentTypeId == null) {
		// return;
		// }
		Collection<IFile> f = indexedFiles.get(contentTypeId);
		if (f == null) {
			f = new ArrayList<IFile>();
			indexedFiles.put(contentTypeId, f);
		}
		if (!f.contains(file)) {
			f.add(file);
		}
		if (XMLReferencesIndexManager.DEBUG)
			System.out.println("adding XML file:" + file.getFullPath()); //$NON-NLS-1$	
	}
}