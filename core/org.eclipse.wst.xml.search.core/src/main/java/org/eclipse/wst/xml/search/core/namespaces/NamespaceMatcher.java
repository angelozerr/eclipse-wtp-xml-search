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
