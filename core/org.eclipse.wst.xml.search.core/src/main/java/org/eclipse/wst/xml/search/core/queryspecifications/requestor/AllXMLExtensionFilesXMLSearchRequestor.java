package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

public class AllXMLExtensionFilesXMLSearchRequestor extends AbstractXMLSearchRequestor {

	public static final IXMLSearchRequestor INSTANCE = new AllXMLExtensionFilesXMLSearchRequestor();
	private static final String XML_EXT = "xml";
	
	@Override
	public boolean accept(IFile file, IResource rootResource) {
		return XML_EXT.equals(file.getFileExtension());
	}

}
