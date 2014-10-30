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

import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchVisitorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.XPathNodeSetIgnoreSelectedNodeSearchVisitor;

public class Struts2PackageQuerySpecification extends Struts2QuerySpecification
		implements IXMLSearchVisitorProvider {

	public IXMLSearchDOMDocumentVisitor getVisitor() {
		return XPathNodeSetIgnoreSelectedNodeSearchVisitor.INSTANCE;
	}
}
