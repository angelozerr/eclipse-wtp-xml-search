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
package org.eclipse.wst.xml.search.core.resource;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

public class ResourceBaseURIResolver extends AbstractURIResolver {

	public static final ResourceBaseURIResolver INSTANCE = new ResourceBaseURIResolver();

	public String resolve(Object selectedNode, IResource rootContainer,
			IResource file) {
		IContainer container = null;
		if (rootContainer.getType() == IResource.FILE) {
			container = ((IFile) rootContainer).getParent();
		} else {
			container = ((IContainer) rootContainer);
		}
		return file
				.getProjectRelativePath()
				.removeFirstSegments(
						container.getProjectRelativePath().segmentCount())
				.toString();
	}

}
