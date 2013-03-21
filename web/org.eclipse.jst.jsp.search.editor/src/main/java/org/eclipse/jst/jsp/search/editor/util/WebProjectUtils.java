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
package org.eclipse.jst.jsp.search.editor.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.jsp.core.internal.util.FacetModuleCoreSupport;

public class WebProjectUtils {

	public static IFolder getWebContentRootFolder(IProject project) {
		IPath webContentPath = FacetModuleCoreSupport.getWebContentRootPath(project);
		if(webContentPath != null) {
			return (IFolder)ResourcesPlugin.getWorkspace().getRoot().findMember(webContentPath);
		}
		return null;
	}
}
