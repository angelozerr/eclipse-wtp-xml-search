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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.ui.ImageResource;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Default content assist additionnal proposal for {@link Node}.
 *
 */
public class DefaultDOMContentAssistAdditionalProposalInfoProvider extends
		NodeContentAssistAdditionalProposalInfoProvider {

	public static final IContentAssistAdditionalProposalInfoProvider<Node> INSTANCE = new DefaultDOMContentAssistAdditionalProposalInfoProvider();

	public Image getImage(Node node) {
		if (node == null) {
			return null;
		}
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ImageResource.getImage(ImageResource.IMG_ATTRIBUTE_OBJ);
		case Node.TEXT_NODE:
			return ImageResource.getImage(ImageResource.IMG_TEXT_OBJ);
		case Node.ELEMENT_NODE:
			return ImageResource.getImage(ImageResource.IMG_ELEMENT_OBJ);
		}
		return null;
	}

	@Override
	public String getDisplayText(String displayText, Node node) {
		String fileName = DOMUtils.getFileName((IDOMNode) node);
		return super
				.getDisplayText(displayText + " - [" + fileName + "]", node);
	}

	public String getTextInfo(Node node) {
		int nodeType = node.getNodeType();
		StringBuilder buf = new StringBuilder();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			Attr attr = (Attr) node;
			buf.append("<b>Node type:</b> Attribute");
			buf.append("<br><b>Attr name: </b>");
			buf.append(attr.getName());
			buf.append("<br><b>Attr value: </b>");
			buf.append(attr.getValue());
			break;
		case Node.TEXT_NODE:
			Text text = (Text) node;
			buf.append("<b>Node type:</b> Text");
			buf.append("<br><b>Text content: </b>");
			buf.append(DOMUtils.getTextContent(text));
			break;
		case Node.ELEMENT_NODE:
			Element element = (Element) node;
			buf.append("<b>Node type:</b> Element");
			buf.append("<br><b>Element name: </b>");
			buf.append(element.getNodeName());
			break;
		}
		Element element = DOMUtils.getOwnerElement(node);
		if (element != null) {
			buf.append("<br><b>Owner element:</b><ul> ");
			Attr attr = null;
			NamedNodeMap attributes = element.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				attr = (Attr) attributes.item(i);
				buf.append("<li><b>@");
				buf.append(attr.getName());
				buf.append(": </b>");
				buf.append(attr.getValue());
				buf.append("</li>");
			}
			buf.append("</ul>");
		}
		buf.append("<br><b>XPath:</b> ");
		buf.append(XPathManager.getManager().computeBasicXPath(node,
				XPathManager.getManager().getNamespaceInfo(node)));
		buf.append("<br><b>File:</b> ");
		String baseLocation = ((IDOMNode) node).getModel().getBaseLocation();
		buf.append(baseLocation);
		return buf.toString();
	}

}
