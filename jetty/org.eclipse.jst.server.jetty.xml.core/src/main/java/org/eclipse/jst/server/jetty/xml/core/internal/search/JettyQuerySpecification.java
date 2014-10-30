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
package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;

public class JettyQuerySpecification implements IXMLSearchRequestorProvider,
		IResourceProvider {

	public IXMLSearchRequestor getRequestor() {
		return JettySearchRequestor.INSTANCE;
	}

	public IResource getResource(Object selectedNode, IResource resource) {
		return FolderContainerProvider.INSTANCE.getResource(selectedNode,
				resource);
	}

}
