package org.eclipse.wst.xml.search.core.namespaces;

import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.w3c.dom.Node;

public interface INamespaceMatcher {

	boolean match(Node node);

	String format(String prefix, String xpath, NamespaceInfos namespaceInfos);

}
