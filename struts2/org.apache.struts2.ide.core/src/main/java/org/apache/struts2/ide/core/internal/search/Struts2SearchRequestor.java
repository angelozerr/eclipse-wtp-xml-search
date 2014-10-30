/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
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
