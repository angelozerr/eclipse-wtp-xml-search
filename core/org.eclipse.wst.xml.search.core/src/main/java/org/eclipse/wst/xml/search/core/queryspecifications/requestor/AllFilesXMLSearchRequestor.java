package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

public class AllFilesXMLSearchRequestor extends AbstractXMLSearchRequestor {

	public static final IXMLSearchRequestor INSTANCE = new AllFilesXMLSearchRequestor();

	@Override
	public boolean accept(IFile file, IResource rootResource) {
		return true;
	}

}
