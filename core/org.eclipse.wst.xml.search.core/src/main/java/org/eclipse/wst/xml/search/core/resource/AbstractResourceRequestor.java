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
package org.eclipse.wst.xml.search.core.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;

public abstract class AbstractResourceRequestor implements IResourceRequestor {

	protected static final String DOT_FOLDER = ".";

	public boolean accept(Object selectedNode, IResource rootContainer,
			IResource resource, IURIResolver resolver, String matching,
			boolean fullMatch) {
		int resourceType = resource.getType();
		switch (resourceType) {
		case IResource.ROOT:
			return accept(selectedNode, rootContainer,
					(IWorkspaceRoot) resource, resolver, matching, fullMatch);
		case IResource.PROJECT:
			return accept(selectedNode, rootContainer, (IProject) resource,
					resolver, matching, fullMatch);
		case IResource.FOLDER:
			return accept(selectedNode, rootContainer, (IFolder) resource,
					resolver, matching, fullMatch);
		case IResource.FILE:
			return accept(selectedNode, rootContainer, (IFile) resource,
					resolver, matching, fullMatch);
		}
		return false;
	}

	public boolean accept(Object selectedNode, IResource rootContainer,
			IWorkspaceRoot workspaceRoot, IURIResolver resolver,
			String matching, boolean fullMatch) {
		return true;
	}

	public boolean accept(Object selectedNode, IResource rootContainer,
			IProject project, IURIResolver resolver, String matching,
			boolean fullMatch) {
		if (project.isOpen()) {
			return true;
		}
		return false;
	}

	public boolean accept(Object selectedNode, IResource rootContainer,
			IFolder folder, IURIResolver resolver, String matching,
			boolean fullMatch) {
		// ignore .svn folder....
		return !folder.getName().startsWith(DOT_FOLDER);
	}

	protected abstract boolean accept(Object selectedNode,
			IResource rootContainer, IFile file, IURIResolver resolver,
			String matching, boolean fullMatch);
	
	public boolean acceptContainer() {
		return false;
	}
	
	public boolean acceptFile() {
		return true;
	}
}
