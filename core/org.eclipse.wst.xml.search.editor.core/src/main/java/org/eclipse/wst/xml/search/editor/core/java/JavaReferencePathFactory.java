package org.eclipse.wst.xml.search.editor.core.java;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferencePathFactory;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;

public class JavaReferencePathFactory {

	private static final String MATCHER_ID = "matcherId";
	public static final JavaReferencePathFactory INSTANCE = new JavaReferencePathFactory();

	public IJavaReference createReference(IConfigurationElement element) {
		IJavaElementMatcher matcher = getMatcher(element);
		return new JavaReference(matcher);
	}

	private IJavaElementMatcher getMatcher(IConfigurationElement element) {
		String matcherId = element.getAttribute(MATCHER_ID);
		return JavaReferencesMatchersManager.getInstance().getMatcher(matcherId);
	}

	public IXMLReferenceTo createTo(IConfigurationElement element,
			IJavaReference reference) {
		return XMLReferencePathFactory.INSTANCE.createTo(element, reference);
	}

}
