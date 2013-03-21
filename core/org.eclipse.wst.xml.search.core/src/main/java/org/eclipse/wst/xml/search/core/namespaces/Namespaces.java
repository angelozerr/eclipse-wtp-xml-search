package org.eclipse.wst.xml.search.core.namespaces;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.w3c.dom.Node;

public class Namespaces {

	private Map<String, INamespaceMatcher> matchers;

	public Namespaces() {
		matchers = new HashMap<String, INamespaceMatcher>();
	}

	public boolean match(Node node) {
		// String prefix = null;
		INamespaceMatcher matcher = null;
		Set<Entry<String, INamespaceMatcher>> entries = matchers.entrySet();
		for (Entry<String, INamespaceMatcher> entry : entries) {
			matcher = entry.getValue();
			if (matcher.match(node)) {
				return true;
			}
		}
		return false;
	}

	public String format(String xpath, NamespaceInfos namespaceInfos) {
		String prefix = null;
		INamespaceMatcher matcher = null;
		Set<Entry<String, INamespaceMatcher>> entries = matchers.entrySet();
		for (Entry<String, INamespaceMatcher> entry : entries) {
			prefix = entry.getKey();
			matcher = entry.getValue();
			xpath = matcher.format(prefix, xpath, namespaceInfos);
		}
		return xpath;
	}

	public void addMatcher(String prefix, INamespaceMatcher matcher) {
		matchers.put(prefix, matcher);
	}

}
