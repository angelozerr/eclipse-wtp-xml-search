package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class RefElementCollector implements IXMLSearchDOMNodeCollector {

	private IDOMElement refElement = null;

	public boolean add(IDOMNode node) {
		if (refElement == null) {
			refElement = getOwnerElement(node);			
		}
		return false;
	}
	
	private IDOMElement getOwnerElement(IDOMNode node) {
		int nodeType = node.getNodeType() ;
		switch(nodeType) {
		case Node.ELEMENT_NODE:
			return (IDOMElement) node;
		case Node.ATTRIBUTE_NODE:
			return (IDOMElement) ((Attr)node).getOwnerElement();
		case Node.TEXT_NODE:
			return (IDOMElement) ((Text)node).getParentNode();
		}
		return null;
	}

	public IDOMElement getRefElement() {
		return refElement;
	};

}
