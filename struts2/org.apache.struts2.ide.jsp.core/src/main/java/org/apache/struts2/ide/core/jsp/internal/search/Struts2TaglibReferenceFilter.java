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
package org.apache.struts2.ide.core.jsp.internal.search;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jst.jsp.search.editor.references.filters.AbstractTaglibReferenceFilter;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;

public class Struts2TaglibReferenceFilter extends AbstractTaglibReferenceFilter {

	public static final IXMLReferenceFilter INSTANCE = new Struts2TaglibReferenceFilter();

	@Override
	protected Collection<String> getTaglibURIs() {
		Collection<String> uris = new ArrayList<String>();
		uris.add("/struts-tags");
		return uris;
	}
}
