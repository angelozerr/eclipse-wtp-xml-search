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
package org.eclipse.wst.xml.search.core.queryspecifications.container;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * Provider to returns the workspace root.
 */
public class WorkspaceContainerProvider implements IResourceProvider {

	public static final IResourceProvider INSTANCE = new WorkspaceContainerProvider();

	/**
	 * Returns the workspace roor.
	 * 
	 * @param selectedNode
	 *            the selected node which have launch the search.
	 * @param resource
	 *            the owner resource file of the selected node.
	 */
	public IResource getResource(Object selectedNode, IResource resource) {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
