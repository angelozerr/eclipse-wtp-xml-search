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
package org.eclipse.jst.jsp.search.editor.internal.contentassist.util;

import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDAttributeDeclaration;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDElementDeclaration;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.ssemodelquery.ModelQueryAdapter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TaglibUtils {

	public static String getTagURI(Node node) {
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			Attr attr = (Attr) node;
			return getTagURI(attr);
		case Node.ELEMENT_NODE:
			Element element = (Element) node;
			return getTagURI(element);
		}
		return null;
	}

	public static String getTagURI(Attr attr) {
		CMAttributeDeclaration decl = getAttributeDeclaration(attr);
		if (decl == null) {
			return null;
		}
		if (decl instanceof TLDAttributeDeclaration) {
			CMDocument doc = ((TLDAttributeDeclaration) decl)
					.getOwnerDocument();
			if (doc instanceof TLDDocument) {
				return ((TLDDocument) doc).getUri();
			}
		}
		return null;
	}

	public static String getTagURI(Element element) {
		CMElementDeclaration decl = getElementDeclaration(element);
		if (decl == null) {
			return null;
		}
		if (decl instanceof TLDElementDeclaration) {
			CMDocument doc = ((TLDElementDeclaration) decl).getOwnerDocument();
			if (doc instanceof TLDDocument) {
				return ((TLDDocument) doc).getUri();
			}
		}
		return null;
	}

	/**
	 * Get attribute declaration of specified element
	 * 
	 * @param attr
	 * @return null if can't get it.
	 */
	public static CMAttributeDeclaration getAttributeDeclaration(Attr attr) {
		if (attr == null) {
			return null;
		}
		INodeNotifier notifier = (INodeNotifier) attr.getOwnerDocument();
		if (notifier == null) {
			return null;
		}
		ModelQueryAdapter mqa = (ModelQueryAdapter) notifier
				.getAdapterFor(ModelQueryAdapter.class);
		if (mqa == null) {
			return null;
		}
		return mqa.getModelQuery().getCMAttributeDeclaration(attr);
	}

	/**
	 * get element declaration of specified element
	 * 
	 * @param element
	 * @return null if can't get it.
	 */
	public static CMElementDeclaration getElementDeclaration(Element element) {
		if (element == null) {
			return null;
		}
		INodeNotifier notifier = (INodeNotifier) element.getOwnerDocument();
		if (notifier == null) {
			return null;
		}
		ModelQueryAdapter mqa = (ModelQueryAdapter) notifier
				.getAdapterFor(ModelQueryAdapter.class);
		if (mqa == null) {
			return null;
		}
		return mqa.getModelQuery().getCMElementDeclaration(element);
	}

}
