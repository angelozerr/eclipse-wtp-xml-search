package org.eclipse.jst.server.jetty.xml.core.internal.search;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jst.server.jetty.xml.core.JettyConstants;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.ContentTypeXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;

public class JettySearchRequestor extends ContentTypeXMLSearchRequestor
		implements JettyConstants {

	public static IXMLSearchRequestor INSTANCE = new JettySearchRequestor();

	@Override
	protected Collection<String> getSupportedContentTypeIds() {
		Collection<String> contentTypeIds = new ArrayList<String>();
		contentTypeIds.add(JETTY_CONFIG_CONTENT_TYPE);
		return contentTypeIds;
	}
}
