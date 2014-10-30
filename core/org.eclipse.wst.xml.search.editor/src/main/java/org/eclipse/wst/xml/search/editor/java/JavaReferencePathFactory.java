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
package org.eclipse.wst.xml.search.editor.java;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferencePathFactory;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;

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
