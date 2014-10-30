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
package org.apache.struts2.ide.core.internal.search.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.editor.queryspecifications.JavaProjectSrcFolders;

public class ApplicationResourcesQuerySpecification implements IMultiResourceProvider {

	public IResource[] getResources(Object selectedNode, IResource resource) {
		return JavaProjectSrcFolders.INSTANCE.getResources(selectedNode,
				resource);
	}
}
