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
package org.eclipse.wst.xml.search.core.namespaces;

import java.util.List;

import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.w3c.dom.Node;

public abstract class NamespaceMatcher implements INamespaceMatcher {

	public String format(String prefix, final String xpath,
			NamespaceInfos namespaceInfos) {
		String newXpath = xpath;
		String namespaceURI = null;
		String p = null;
		boolean formatted = false;
		List<NamespaceInfo> infos = namespaceInfos;
		for (NamespaceInfo namespaceInfo : infos) {
			namespaceURI = namespaceInfo.uri;
			if (namespaceURI != null && isMatchedNamespace(namespaceURI)) {
				p = namespaceInfo.prefix;
				newXpath = xpath.replaceAll(prefix + ":", p + ":");
				formatted = true;
			}
		}
		if (!formatted) {
			newXpath = xpath.replaceAll(prefix + ":", "");
			newXpath += "[starts-with(name(.), \"" + prefix + "\")]";
		}
		return newXpath;
	}

	public boolean match(Node node) {
		String namespaceURI = node.getNamespaceURI();
		if (namespaceURI != null) {
			return isMatchedNamespace(namespaceURI);
		} else {
			return isMatchedPrefix(node.getPrefix());
		}
	}

	protected abstract boolean isMatchedNamespace(String namespaceURI);

	protected abstract boolean isMatchedPrefix(String prefix);

}
