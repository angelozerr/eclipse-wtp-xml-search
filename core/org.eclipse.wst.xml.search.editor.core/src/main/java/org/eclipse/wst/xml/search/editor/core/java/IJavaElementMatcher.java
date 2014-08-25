package org.eclipse.wst.xml.search.editor.core.java;

import org.eclipse.jdt.core.IJavaElement;

public interface IJavaElementMatcher {

	public static IJavaElementMatcher TRUE_MATCHER = new IJavaElementMatcher() {

		public boolean match(IJavaElement element) {
			return true;
		}
	};

	public static IJavaElementMatcher FALSE_MATCHER = new IJavaElementMatcher() {

		public boolean match(IJavaElement element) {
			return true;
		}
	};

	boolean match(IJavaElement element);
}
