package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.FolderContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.IContainerProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLSearchRequestorProvider;


public class JettyQuerySpecification implements IXMLSearchRequestorProvider,
		IContainerProvider {

	public IXMLSearchRequestor getRequestor() {
		return JettySearchRequestor.INSTANCE;
	}

	public IContainer getContainer(IFile file) {
		return FolderContainerProvider.INSTANCE.getContainer(file);
	}

}
