/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsp.search.editor.queryspecifications;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jst.jsp.core.internal.provisional.contenttype.ContentTypeIdForJSP;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.ContentTypeXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;

public class JSPSearchRequestor extends ContentTypeXMLSearchRequestor {

	private static final String META_INF = "META-INF";
	private static final String WEB_INF = "WEB-INF";
	
	public static IXMLSearchRequestor INSTANCE = new JSPSearchRequestor();

	@Override
	protected Collection<String> getSupportedContentTypeIds() {
		Collection<String> contentTypeIds = new ArrayList<String>();
		contentTypeIds.add(ContentTypeIdForJSP.ContentTypeID_JSP);
		return contentTypeIds;
	}

	@Override
	public boolean accept(IFolder folder, IResource rootResource) {
		if (WEB_INF.equals(folder.getName())
				|| META_INF.equals(folder.getName())) {
			return false;
		}
		return super.accept(folder, rootResource);
	}

}
