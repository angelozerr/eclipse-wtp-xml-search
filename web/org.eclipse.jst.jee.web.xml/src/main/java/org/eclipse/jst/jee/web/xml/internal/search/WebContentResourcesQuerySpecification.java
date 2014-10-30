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
package org.eclipse.jst.jee.web.xml.internal.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestorProvider;

public class WebContentResourcesQuerySpecification implements
		IResourceProvider, IResourceRequestorProvider {

	public IResource getResource(Object selectedNode, IResource resource) {
		return resource.getParent().getParent();
	}

	public IResourceRequestor getRequestor() {
		return WebContentResourcesRequestor.INSTANCE;
	}

}
