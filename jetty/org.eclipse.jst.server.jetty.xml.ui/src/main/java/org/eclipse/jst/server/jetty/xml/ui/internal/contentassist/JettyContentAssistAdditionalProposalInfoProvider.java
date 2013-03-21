package org.eclipse.jst.server.jetty.xml.ui.internal.contentassist;

import org.eclipse.jst.server.jetty.xml.core.JettyConstants;
import org.eclipse.jst.server.jetty.xml.ui.internal.ImageResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.editor.contentassist.NodeContentAssistAdditionalProposalInfoProvider;
import org.w3c.dom.Node;

public class JettyContentAssistAdditionalProposalInfoProvider extends
		NodeContentAssistAdditionalProposalInfoProvider implements
		JettyConstants {

	public Image getImage(Node node) {
		return ImageResource.getImage(ImageResource.IMG_NEW_ELT);
	}

	public String getTextInfo(Node node) {
		if (node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		IDOMElement element = (IDOMElement) node;
		StringBuilder buf = new StringBuilder();
		buf.append("<b>Name:</b> ");
		buf.append(element.getNodeName());
		buf.append("<br><b>Class:</b> ");
		buf.append(element.getAttribute(CLASS_ATTR));
		buf.append("<br><b>File:</b> ");
		String baseLocation = element.getModel().getBaseLocation();
		buf.append(baseLocation);
		return buf.toString();

	}

}
