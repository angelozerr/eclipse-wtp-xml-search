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
package org.eclipse.wst.xml.search.core.resource;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public interface IResourceSearchEngine {

	IStatus search(Object selectedNode, IResource[] containers, IResourceRequestor requestor,
			IResourceCollector collector, IURIResolver resolver, String matching, boolean fullMatch,
			IProgressMonitor monitor);
	
	IStatus search(Object selectedNode, IResource container, IResourceRequestor requestor,
			IResourceCollector collector, IURIResolver resolver, String matching, boolean fullMatch,
			IProgressMonitor monitor);

}
