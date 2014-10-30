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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public abstract class AbstractURIResolver implements IURIResolver {

	public boolean accept(Object selectedNode, IResource rootContainer,
			IResource file, String matching, boolean fullMatch) {
		if (!fullMatch) {
			matching = matching.toLowerCase();
			String fileName = file.getName();
			fileName = fileName.toLowerCase();
			if (fileName.startsWith(matching)) {
				return true;
			}
			return resolve(selectedNode, rootContainer, file).toLowerCase()
					.startsWith(matching);
		}
		return resolve(selectedNode, rootContainer, file).equals(matching);
	}

	public <T extends IResource> T getResource(IContainer container,
			String resourceName, Class<T> clazz) {
		if (clazz == IFile.class) {
			return (T) container.getFile(new Path(resourceName));
		}
		if (clazz == IFolder.class) {
			return (T) container.getFolder(new Path(resourceName));
		}
		return null;
	}

}
