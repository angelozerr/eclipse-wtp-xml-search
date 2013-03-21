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
package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

/**
 * XML search requestor interface used to filter XML files and folder when
 * search is done.
 * 
 */
public interface IXMLSearchRequestor {

	/**
	 * Returns true if resource (file, folder, project) must be accepted while
	 * searching and false otherwise. When file is accepted, DOM document from
	 * the file is loaded and
	 * {@link IXMLSearchRequestor#accept(IStructuredModel)} is called to know if
	 * DOM Document is visited to collect Node.
	 * 
	 * @param resource
	 *            the resource to accept
	 * @param resource
	 *            the root resource where search start.
	 * @return
	 */
	boolean accept(IResource resource, IResource rootResource);

	/**
	 * Returns true if DOM Document (file, folder, project) must be accepted
	 * while searching and false otherwise. When DOM Document is accepted, it is
	 * visited to collect Node.
	 * 
	 * @param model
	 *            the SSE Model to accept
	 * @return
	 */
	boolean accept(IStructuredModel model);
}
