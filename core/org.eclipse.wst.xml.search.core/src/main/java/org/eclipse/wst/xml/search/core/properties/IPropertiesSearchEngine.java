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
package org.eclipse.wst.xml.search.core.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * Interface of Properties Search engine.
 * 
 */
public interface IPropertiesSearchEngine {

	/**
	 * Search property which matches the given <code>matching</code> in files
	 * properties hosted in the list of the given <code>containers</code>.
	 * 
	 * @param selectedNode
	 *            the selected node which has started the search.
	 * @param containers
	 *            list of {@link IResource} where property must be searched in
	 *            files.
	 * @param requestor
	 *            property requestor used to.
	 * @param collector
	 * @param matching
	 * @param fullMatch
	 * @param monitor
	 * @return
	 */
	IStatus search(Object selectedNode, IResource[] containers,
			IPropertiesRequestor requestor, IPropertiesCollector collector,
			String matching, boolean fullMatch, IProgressMonitor monitor);

	/**
	 * 
	 * @param selectedNode
	 * @param container
	 * @param requestor
	 * @param collector
	 * @param matching
	 * @param fullMatch
	 * @param monitor
	 * @return
	 */
	IStatus search(Object selectedNode, IResource container,
			IPropertiesRequestor requestor, IPropertiesCollector collector,
			String matching, boolean fullMatch, IProgressMonitor monitor);
}
