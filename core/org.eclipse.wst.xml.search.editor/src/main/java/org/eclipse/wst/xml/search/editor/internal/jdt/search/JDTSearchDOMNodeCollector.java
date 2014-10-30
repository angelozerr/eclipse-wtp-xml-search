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
package org.eclipse.wst.xml.search.editor.internal.jdt.search;

import org.eclipse.jdt.ui.search.ISearchRequestor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.ui.XMLMatch;

public class JDTSearchDOMNodeCollector implements IXMLSearchDOMNodeCollector {

	private final ISearchRequestor requestor;

	public JDTSearchDOMNodeCollector(final ISearchRequestor requestor) {
		this.requestor = requestor;
	}

	public boolean add(IDOMNode node) {
		requestor.reportMatch(new XMLMatch(node));
		return true;
	}
}
