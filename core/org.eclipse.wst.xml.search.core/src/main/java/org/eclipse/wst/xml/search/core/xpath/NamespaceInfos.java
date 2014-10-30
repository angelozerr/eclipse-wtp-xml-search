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
package org.eclipse.wst.xml.search.core.xpath;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;

public class NamespaceInfos extends ArrayList<NamespaceInfo> implements
		NamespaceContext {

	public String getPrefix(String namespaceURI) {
		int size = super.size();
		for (int i = 0; i < size; i++) {
			NamespaceInfo namespaceInfo = super.get(i);
			if (namespaceURI.equals(namespaceInfo.uri)) {
				return getPrefix(namespaceInfo);
			}
		}
		return null;
	}

	public String getNamespaceURI(String prefix) {
		if ("ns".equals(prefix)) {
			prefix = "";
		}
		int size = super.size();
		for (int i = 0; i < size; i++) {
			NamespaceInfo namespaceInfo = super.get(i);
			if (prefix.equals(namespaceInfo.prefix)) {				
				return namespaceInfo.uri;
			}
		}
		return null;
	}

	public Iterator getPrefixes(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getPrefix(NamespaceInfo namespaceInfo) {
		if ("".equals(namespaceInfo.prefix)) {
			return "ns";
		}
		return namespaceInfo.prefix;
	}
}
