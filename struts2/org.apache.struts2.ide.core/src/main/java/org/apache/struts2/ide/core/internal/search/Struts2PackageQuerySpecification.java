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
