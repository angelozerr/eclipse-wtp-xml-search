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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts2.ide.core.IStruts2Model;
import org.apache.struts2.ide.core.Struts2Constants;
import org.apache.struts2.ide.core.Struts2CorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.xml.search.core.queryspecifications.container.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import org.eclipse.wst.xml.search.core.resource.DOMNodeBaseURIResolver;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Struts2QuerySpecification implements IXMLSearchRequestorProvider,
		IMultiResourceProvider, IMultiStorageProvider, Struts2Constants {

	public IXMLSearchRequestor getRequestor() {
		return Struts2SearchRequestor.INSTANCE;
	}

	public IResource[] getResources(Object selectedNode, IResource resource) {
		final Collection<IResource> resources = new ArrayList<IResource>();
		if ("struts.xml".equals(resource.getName())) {
			// Add the owner XML struts2 file
			resources.add(resource);
			// Test if the struts2 XML file has include element declared
			Collection<Element> includes = DOMUtils
					.getFirstChildElementsByTagName(((Node)selectedNode)
							.getOwnerDocument().getDocumentElement(),
							INCLUDE_ELT);
			for (Element include : includes) {
				String fileName = include.getAttribute(FILE_ATTR);
				if (!StringUtils.isEmpty(fileName)) {
					IFile file = (IFile)DOMNodeBaseURIResolver.INSTANCE.getResource(
							resource.getParent(), fileName, IFile.class);
					if (file != null && file.exists()) {
						resources.add(file);
					}
				}
			}
		} else {
			resources.add(FolderContainerProvider.INSTANCE.getResource(
					selectedNode, resource));
		}

		// for (int i = 0; i < r.length; i++) {
		// IResource s = r[i];
		// if (s.getName().startsWith("struts2")) {
		// System.err.println(s);
		// }
		// }

		return resources.toArray(IMultiResourceProvider.EMPTY_RESOURCE);
	}

	
	public IStorage[] getStorages(Object selectedNode, IResource resource) {
		IStruts2Model model = Struts2CorePlugin.getStruts2Model();
		return model.getStrutsXMLFromJAR(resource.getProject());
	}
	
}
