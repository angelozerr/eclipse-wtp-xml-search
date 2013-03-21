package org.apache.struts2.ide.core.internal.search.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.editor.queryspecifications.JavaProjectSrcFolders;

public class ApplicationResourcesQuerySpecification implements IMultiResourceProvider {

	public IResource[] getResources(Object selectedNode, IResource resource) {
		return JavaProjectSrcFolders.INSTANCE.getResources(selectedNode,
				resource);
	}
}
