package org.apache.struts2.ide.core.internal.search;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts2.ide.core.Struts2Constants;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.ContentTypeXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;

public class Struts2SearchRequestor extends ContentTypeXMLSearchRequestor
		implements Struts2Constants {

	public static IXMLSearchRequestor INSTANCE = new Struts2SearchRequestor();

	@Override
	protected Collection<String> getSupportedContentTypeIds() {
		Collection<String> contentTypeIds = new ArrayList<String>();
		contentTypeIds.add(STRUTS2_CONFIG_CONTENT_TYPE);
		return contentTypeIds;
	}

}
