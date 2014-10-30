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

/**
 * 
 * Provider to get several {@link IResource} (IFile, IContainer....) used to
 * search DOM Document to use to search DOM Nodes.
 * 
 * <ul>
 * <li>when {@link IResource} returned is IFile, it use DOM Document loaded from
 * the IFile.</li>
 * <li>when {@link IResource} returned is IContainer, it loops for each IFile
 * and IContainer and load several DOM Document where search Nodes must be done.
 * </li>
 * </ul>
 */
public interface IMultiResourceProvider {

	public static final IResource[] EMPTY_RESOURCE = new IResource[0];

	/**
	 * Returns array of {@link IResource} (IFile, IContainer....) used to search
	 * DOM Document to use to search DOM Nodes. If null is returned, search is
	 * stopped.
	 * 
	 * <ul>
	 * <li>when {@link IResource} returned is IFile, it use DOM Document loaded
	 * from the IFile.</li>
	 * <li>when {@link IResource} returned is IContainer, it loops for each
	 * IFile and IContainer and load several DOM Document where search Nodes
	 * must be done.</li>
	 * 
	 * @param selectedNode
	 *            the selected node which have launch the search.
	 * @param resource
	 *            the owner resource file of the selected node.
	 * @return
	 */
	IResource[] getResources(Object selectedNode, IResource resource);
}
