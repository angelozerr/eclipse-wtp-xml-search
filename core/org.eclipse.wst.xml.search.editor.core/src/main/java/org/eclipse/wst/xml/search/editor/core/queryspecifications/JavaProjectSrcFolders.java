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
package org.eclipse.wst.xml.search.editor.core.queryspecifications;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.editor.core.util.JdtUtils;

public class JavaProjectSrcFolders implements IMultiResourceProvider {

	public static final IMultiResourceProvider INSTANCE = new JavaProjectSrcFolders();

	public IResource[] getResources(Object selectedNode, IResource resource) {
		IProject project = resource.getProject();
		return JdtUtils.getJavaProjectSrcFolders(project);
	}

}
