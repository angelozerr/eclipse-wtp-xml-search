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
package org.eclipse.jst.jee.web.xml.internal.search.contentassist;

import org.eclipse.jst.jee.web.xml.internal.WebXMLPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.editor.contentassist.ElementContentAssistAdditionalProposalInfoProvider;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ServletContentAssistAdditionalProposalInfoProvider extends
		ElementContentAssistAdditionalProposalInfoProvider {

	public Image getImage(Node node) {
		return WebXMLPlugin.getDefault().getImageRegistry().get(
				WebXMLPlugin.SERVLET_IMG);
	}

	@Override
	protected String doGetTextInfo(IDOMElement servletNameElt) {
		IDOMElement element = (IDOMElement)servletNameElt.getParentNode();
		StringBuilder buf = new StringBuilder();
		buf.append("<b>------------------------ Servlet ------------------------</b> ");
		// description
		buf.append("<br><b>Description:</b> ");
		String description = getTextContent(element, "description");
		if (description != null) {
			buf.append(description);
		}
		// display-name
		buf.append("<br><b>Display name:</b> ");
		String displayName = getTextContent(element, "display-name");
		if (displayName != null) {
			buf.append(displayName);
		}
		// servlet-name
		buf.append("<br><b>Servlet name:</b> ");
		String servletName = getTextContent(element, "servlet-name");
		if (servletName != null) {
			buf.append(servletName);
		}
		// servlet-class
		buf.append("<br><b>Servlet class:</b> ");
		String servletClass = getTextContent(element, "servlet-class");
		if (servletClass != null) {
			buf.append(servletClass);
		}
		//buf.append("<br><b>File:</b> ");
		//String baseLocation = element.getModel().getBaseLocation();
		//buf.append(baseLocation);
		return buf.toString();

	}

	private String getTextContent(IDOMElement element, String elementName) {
		NodeList nodes = element.getElementsByTagName(elementName);
		if (nodes.getLength() < 1) {
			return "";
		}
		Element childElement = (Element)nodes.item(0);
		Text text = (Text) childElement.getFirstChild();
		if (text == null) {
			return "";
		}
		return text.getData();
	}

}
