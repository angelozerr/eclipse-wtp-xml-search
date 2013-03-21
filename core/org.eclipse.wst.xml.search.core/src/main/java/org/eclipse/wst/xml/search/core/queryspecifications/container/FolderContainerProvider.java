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
package org.eclipse.wst.xml.search.core.queryspecifications.container;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;

/**
 * Provider to returns the {@link IContainer} of the file of the selected Node
 * which launch the search.
 */
public class FolderContainerProvider implements IResourceProvider {

	public static IResourceProvider INSTANCE = new FolderContainerProvider();

	/**
	 * Returns the {@link IContainer} of the file of the selected Node which
	 * launch the search.
	 * 
	 * @param selectedNode
	 *            the selected node which have launch the search.
	 * @param resource
	 *            the owner resource file of the selected node.
	 */
	public IResource getResource(Object selectedNode, IResource resource) {
		if (resource.getType() == IResource.PROJECT) {
			return (IProject) resource;
		}
		if (resource.getType() == IResource.ROOT) {
			return (IWorkspaceRoot) resource;
		}
		return resource.getParent();
	}

}
