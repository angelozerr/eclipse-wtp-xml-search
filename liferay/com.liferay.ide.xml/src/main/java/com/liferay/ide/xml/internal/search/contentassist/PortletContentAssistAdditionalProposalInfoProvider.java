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
package com.liferay.ide.xml.internal.search.contentassist;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.editor.contentassist.ElementContentAssistAdditionalProposalInfoProvider;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.liferay.ide.xml.internal.LiferayXMLPlugin;

/**
 * Custom content assist proposal info for portlet.
 *
 */
public class PortletContentAssistAdditionalProposalInfoProvider extends
		ElementContentAssistAdditionalProposalInfoProvider {

	public Image getImage(Node node) {
		return LiferayXMLPlugin.getDefault().getImageRegistry().get(
				LiferayXMLPlugin.PORTLET_IMG);
	}

	@Override
	protected String doGetTextInfo(IDOMElement portletNameElt) {
		IDOMElement portletElt = (IDOMElement)portletNameElt.getParentNode();
		StringBuilder buf = new StringBuilder();
		buf.append("<b>------------------------ Portlet ------------------------</b> ");
		// description
		buf.append("<br><b>Portlet name:</b> ");
		String portletName = getTextContent(portletElt, "portlet-name");
		if (portletName != null) {
			buf.append(portletName);
		}
		// display-name
		buf.append("<br><b>Display name:</b> ");
		String displayName = getTextContent(portletElt, "display-name");
		if (displayName != null) {
			buf.append(displayName);
		}
		// portlet-class
		buf.append("<br><b>Portlet class:</b> ");
		String portletClass = getTextContent(portletElt, "portlet-class");
		if (portletClass != null) {
			buf.append(portletClass);
		}
		buf.append("<br><b>File:</b> ");
		String baseLocation = portletElt.getModel().getBaseLocation();
		buf.append(baseLocation);
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
