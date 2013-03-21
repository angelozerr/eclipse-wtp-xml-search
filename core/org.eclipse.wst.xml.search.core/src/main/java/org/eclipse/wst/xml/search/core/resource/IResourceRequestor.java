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
package org.eclipse.wst.xml.search.core.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;

/**
 * Resource requestor is used when search is started. Search loop for each
 * container and files and call a requestor to know if the resource is accepted
 * for the search result or not. If the resource is accepted, it is added by
 * using a a {@link IResourceCollector}.
 * 
 * @author Angelo ZERR
 * 
 */
public interface IResourceRequestor {

	/**
	 * Returns true if resource can be accepted or not.
	 * 
	 * @param selectedNode
	 *            the selected node which has start the search.
	 * @param rootResource
	 *            the root resource where search start (which is returned with
	 *            {@link IResourceProvider}).
	 * @param resource
	 *            the resource to accept or not.
	 * @param resolver
	 *            the resolver.
	 * @param matching
	 *            the matching string used for the search.
	 * @param fullMatch
	 *            true if full match must be done (ex : for validation,
	 *            hyperlink) and false otherwise (ex : autocompletion).
	 * @return
	 */
	boolean accept(Object selectedNode, IResource rootResource,
			IResource resource, IURIResolver resolver, String matching,
			boolean fullMatch);

	/**
	 * Returns true if files can be added to the collector
	 * {@link IResourceCollector} and false otherwise.
	 * 
	 * @return
	 */
	boolean acceptFile();

	/**
	 * Returns true if container (folder, project...) can be added to the
	 * collector {@link IResourceCollector} and false otherwise.
	 * 
	 * @return
	 */
	boolean acceptContainer();
}
