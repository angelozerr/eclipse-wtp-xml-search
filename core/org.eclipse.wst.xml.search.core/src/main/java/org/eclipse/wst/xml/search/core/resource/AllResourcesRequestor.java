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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public class AllResourcesRequestor extends AbstractResourceRequestor {

	public static final IResourceRequestor INSTANCE = new AllResourcesRequestor();

	@Override
	protected boolean accept(Object selectedNode, IResource rootContainer,
			IFile file, IURIResolver resolver, String matching,
			boolean fullMatch) {
		return true;
	}

}
