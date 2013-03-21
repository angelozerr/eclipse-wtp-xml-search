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
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;

public class DOMNodeBaseURIResolver extends AbstractURIResolver {

	public static final IURIResolver INSTANCE = new DOMNodeBaseURIResolver();

	public String resolve(Object selectedNode, IResource rootContainer,
			IResource file) {
		IContainer container = DOMUtils.getFile((IDOMNode) selectedNode)
				.getParent();
		return file
				.getProjectRelativePath()
				.removeFirstSegments(
						container.getProjectRelativePath().segmentCount())
				.toString();
	}

}
