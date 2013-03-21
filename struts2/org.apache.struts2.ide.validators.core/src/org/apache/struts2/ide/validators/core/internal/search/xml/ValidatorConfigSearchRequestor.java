package org.apache.struts2.ide.validators.core.internal.search.xml;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts2.ide.validators.core.XWorkValidatorConstants;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.ContentTypeXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;

public class ValidatorConfigSearchRequestor extends ContentTypeXMLSearchRequestor
		implements XWorkValidatorConstants {

	public static IXMLSearchRequestor INSTANCE = new ValidatorConfigSearchRequestor();

	@Override
	protected Collection<String> getSupportedContentTypeIds() {
		Collection<String> contentTypeIds = new ArrayList<String>();
		contentTypeIds.add(VALIDATOR_CONFIG_CONTENT_TYPE);
		return contentTypeIds;
	}

}
