package org.eclipse.jst.server.jetty.xml.core.internal.searcher.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.wst.xml.search.editor.searchers.java.IExtendedClassProvider;

public class JavaQuerySpecification implements IExtendedClassProvider {

	public IType[] getExtends(Object selectedNode, IFile file) {
		return null;
	}

}
