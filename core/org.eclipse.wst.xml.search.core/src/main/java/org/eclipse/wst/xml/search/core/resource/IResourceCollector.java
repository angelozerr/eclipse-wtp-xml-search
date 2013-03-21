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

/**
 * A collector to collect {@link IResource} founded with the search.
 * 
 */
public interface IResourceCollector {

	/**
	 * This method is called when the resource match the search.
	 * 
	 * @param resource
	 *            which match the search.
	 * @param rootResource
	 *            the root (container, file....) used when search is started.
	 * @param resolver
	 *            to use to resolve value path fo the {@link IResource}.
	 * @return
	 */
	boolean add(IResource resource, IResource rootResource,
			IURIResolver resolver);
}
