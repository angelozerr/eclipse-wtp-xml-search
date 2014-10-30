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
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public interface IURIResolver {

	String resolve(Object selectedNode, IResource rootContainer,
			IResource file);

	boolean accept(Object selectedNode, IResource rootContainer,
			IResource file, String matching, boolean fullMatch);

	<T extends IResource> T getResource(IContainer container,
			String resourceName, Class<T> clazz);

}
