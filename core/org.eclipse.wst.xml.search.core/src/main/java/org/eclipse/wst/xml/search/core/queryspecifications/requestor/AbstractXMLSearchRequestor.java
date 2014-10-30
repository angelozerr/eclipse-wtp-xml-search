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
package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public abstract class AbstractXMLSearchRequestor implements IXMLSearchRequestor {

	protected static final String DOT_FOLDER = ".";

	public boolean accept(IResource resource, IResource rootResource) {
		int resourceType = resource.getType();
		switch (resourceType) {
		case IResource.ROOT:
			return accept((IWorkspaceRoot) resource, rootResource);
		case IResource.PROJECT:
			return accept((IProject) resource, rootResource);
		case IResource.FOLDER:
			return accept((IFolder) resource, rootResource);
		case IResource.FILE:
			return accept((IFile) resource, rootResource);
		}
		return false;
	}

	public boolean accept(IWorkspaceRoot workspaceRoot, IResource rootResource) {
		return true;
	}

	public boolean accept(IProject project, IResource rootResource) {
		if (project.isOpen()) {
			return true;
		}
		return false;
	}

	public boolean accept(IFolder folder, IResource rootResource) {
		// ignore .svn folder....
		return !folder.getName().startsWith(DOT_FOLDER);
	}

	public boolean accept(IStructuredModel model) {
		return true;
	}

	protected abstract boolean accept(IFile file, IResource rootResource);
}
