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
package org.eclipse.jst.jsp.search.editor.queryspecifications;

import org.eclipse.core.resources.IResource;
import org.eclipse.jst.jsp.search.editor.util.WebProjectUtils;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;

public class WebContentFolderProvider implements IResourceProvider {

	public static final IResourceProvider INSTANCE = new WebContentFolderProvider();;

	public IResource getResource(Object selectedNode, IResource resource) {
		return WebProjectUtils.getWebContentRootFolder(resource.getProject());
	}

}
