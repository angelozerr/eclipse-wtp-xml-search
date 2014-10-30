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
package org.apache.struts2.ide.core.internal.search.webcontent;

import org.apache.struts2.ide.core.Struts2Constants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.resource.DefaultResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WebContentResourceRequestor extends DefaultResourceRequestor implements Struts2Constants {

	public static final IResourceRequestor INSTANCE = new WebContentResourceRequestor();

	@Override
	protected boolean accept(Object selectedNode, IResource rootContainer,
			IFile file, IURIResolver resolver, String matching,
			boolean fullMatch) {
		if (matching.startsWith("/")) {
			matching = matching.substring(1, matching.length());
		}
		if( super.accept(selectedNode, rootContainer, file, resolver,
				matching, fullMatch)) {
			return true;
		}
		Node resultElement = ((Node)selectedNode).getParentNode();
		if (resultElement == null) {
			return false;
		}
		Node actionElement = resultElement.getParentNode();
		if (actionElement == null) {
			return false;
		}
		Element packageElement = (Element)actionElement.getParentNode();
		if (packageElement == null) {
			return false;
		}
		String namespace = packageElement.getAttribute(NAMESPACE_ATTR);
		if (StringUtils.isEmpty(namespace)) {
			return false;
		}
		if (namespace.startsWith("/")) {
			namespace = namespace.substring(1, namespace.length());
		}
		return super.accept(selectedNode, rootContainer, file, resolver,
				namespace + "/" + matching, fullMatch);
	}
}
