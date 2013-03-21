package org.eclipse.wst.xml.search.editor.java;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.wst.xml.search.editor.references.AbstractReference;

public class JavaReference extends AbstractReference implements IJavaReference {

	private final IJavaElementMatcher matcher;

	public JavaReference(IJavaElementMatcher matcher) {
		this.matcher = matcher;
	}

	public boolean match(IJavaElement element) {
		return matcher.match(element);
	}

}
