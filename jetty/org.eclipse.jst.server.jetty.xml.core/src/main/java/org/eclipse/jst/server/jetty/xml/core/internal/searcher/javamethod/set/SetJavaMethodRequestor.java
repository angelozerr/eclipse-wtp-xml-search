package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod.set;

import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.BeansJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;

public class SetJavaMethodRequestor extends BeansJavaMethodRequestor {

	public static final IJavaMethodRequestor INSTANCE = new SetJavaMethodRequestor();

	public SetJavaMethodRequestor() {
		super(SET_PREFIX, false);
	}

}
