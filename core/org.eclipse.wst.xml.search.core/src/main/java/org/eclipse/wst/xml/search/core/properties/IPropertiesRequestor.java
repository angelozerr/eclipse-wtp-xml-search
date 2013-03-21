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
package org.eclipse.wst.xml.search.core.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;

/**
 * Resource requestor is used when search is started. Search loop for each
 * container and files and call a requestor to know if the resource is accepted
 * for the search result or not. If the resource is accepted, it is added by
 * using a a {@link IResourceCollector}.
 * 
 * @author Angelo ZERR
 * 
 */
public interface IPropertiesRequestor {

	/**
	 * Returns true if resource (file, folder, project) must be accepted while
	 * searching and false otherwise.
	 * 
	 * @param resource
	 *            the resource to accept
	 * @param resource
	 *            the root resource where search start.
	 * @return
	 */
	boolean accept(IResource resource, IResource rootResource);
}
