package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public abstract class ContentTypeXMLSearchRequestor extends
		AbstractXMLSearchRequestor {

	private Collection<String> supportedContentTypeIds;

	public boolean accept(IFile file, IResource rootResource) {
		for (String id : internalGetSupportedContentTypeIds()) {
			IContentType contentType = Platform.getContentTypeManager()
					.getContentType(id);
			if (contentType != null) {
				if (contentType.isAssociatedWith(file.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean accept(IStructuredModel model) {
		String contentTypeIdentifier = model.getContentTypeIdentifier();
		return internalGetSupportedContentTypeIds().contains(
				contentTypeIdentifier);
	}

	private Collection<String> internalGetSupportedContentTypeIds() {
		if (supportedContentTypeIds == null) {
			supportedContentTypeIds = getSupportedContentTypeIds();
		}
		return supportedContentTypeIds;
	}

	protected abstract Collection<String> getSupportedContentTypeIds();

}
