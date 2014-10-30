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
package org.apache.struts2.ide.core.internal.search;

import org.apache.struts2.ide.core.Struts2Constants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.resource.AbstractResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.FileUtils;

public class Struts2IncludeFileRequestor extends AbstractResourceRequestor
		implements Struts2Constants {

	public static IResourceRequestor INSTANCE = new Struts2IncludeFileRequestor();

	@Override
	protected boolean accept(Object selectedNode, IResource rootContainer,
			IFile file, IURIResolver resolver, String matching,
			boolean fullMatch) {
		if (FileUtils.isXMLFile(file)
				&& DOMUtils.isContentTypeId(file, STRUTS2_CONFIG_CONTENT_TYPE)) {
			if (file.equals(DOMUtils.getFile((IDOMNode)selectedNode))) {
				return false;
			}
			return resolver.accept(selectedNode, rootContainer, file, matching,
					fullMatch);

		}
		return false;
	}
}
