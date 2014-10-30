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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.core.internal.Trace;

public class ResourceSearchEngine implements IResourceSearchEngine {

	private static final IResourceSearchEngine INSTANCE = new ResourceSearchEngine();

	public static IResourceSearchEngine getDefault() {
		return INSTANCE;
	}

	public IStatus search(Object selectedNode, IResource[] containers,
			IResourceRequestor requestor, IResourceCollector collector,
			IURIResolver resolver, String matching, boolean fullMatch,
			IProgressMonitor monitor) {
		for (int i = 0; i < containers.length; i++) {
			IResource resource = containers[i];
			internalSearch(selectedNode, resource, containers[i], requestor,
					collector, resolver, matching, fullMatch, monitor);
		}
		return Status.OK_STATUS;
	}

	public IStatus search(Object selectedNode, IResource container,
			IResourceRequestor requestor, IResourceCollector collector,
			IURIResolver resolver, String matching, boolean fullMatch,
			IProgressMonitor monitor) {
		internalSearch(selectedNode, container, container, requestor,
				collector, resolver, matching, fullMatch, monitor);
		return Status.OK_STATUS;
	}

	private void internalSearch(Object selectedNode, IResource rootContainer,
			IResource container, IResourceRequestor requestor,
			IResourceCollector collector, IURIResolver resolver,
			String matching, boolean fullMatch, IProgressMonitor monitor) {
		if (!requestor.accept(selectedNode, rootContainer, container, resolver,
				matching, fullMatch))
			return;
		int resourceType = container.getType();
		switch (resourceType) {
		case IResource.FILE:
			if (requestor.acceptFile()) {
				collector.add(container, rootContainer, resolver);
			}
			break;
		case IResource.ROOT:
		case IResource.PROJECT:
		case IResource.FOLDER:
			try {
				if (requestor.acceptContainer()) {
					if (!fullMatch) {
						if (resolver
								.resolve(selectedNode, rootContainer, container)
								.toUpperCase()
								.startsWith(matching.toUpperCase())) {
							collector.add(container, rootContainer, resolver);
						}
					} else {
						if (resolver.resolve(selectedNode, rootContainer,
								container).equals(matching)) {
							collector.add(container, rootContainer, resolver);
						}
					}
				}
				internalSearch(selectedNode, rootContainer,
						((IContainer) container).members(), requestor,
						collector, resolver, matching, fullMatch, monitor);
			} catch (CoreException e) {
				Trace.trace(Trace.SEVERE, e.getMessage(), e);
			}
			break;
		}
	}

	private IStatus internalSearch(Object selectedNode,
			IResource rootContainer, IResource[] containers,
			IResourceRequestor requestor, IResourceCollector collector,
			IURIResolver resolver, String matching, boolean fullMatch,
			IProgressMonitor monitor) {
		for (int i = 0; i < containers.length; i++) {
			internalSearch(selectedNode, rootContainer, containers[i],
					requestor, collector, resolver, matching, fullMatch,
					monitor);
		}
		return Status.OK_STATUS;
	}
}
