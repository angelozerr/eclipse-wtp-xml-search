package org.eclipse.jst.server.jetty.xml.ui.internal.contentassist;

import org.eclipse.jst.server.jetty.xml.core.JettyConstants;
import org.eclipse.jst.server.jetty.xml.ui.internal.ImageResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.contentassist.ElementContentAssistAdditionalProposalInfoProvider;
import org.w3c.dom.Node;

public class JettyContentAssistAdditionalProposalInfoProvider extends
		ElementContentAssistAdditionalProposalInfoProvider implements
		JettyConstants {

	@Override
	public String getDisplayText(String displayText, Node node) {
		String fileName = DOMUtils.getFileName((IDOMNode) node);
		return super
				.getDisplayText(displayText + " - [" + fileName + "]", node);
	}

	public Image getImage(Node node) {
		return ImageResource.getImage(ImageResource.IMG_NEW_ELT);
	}

	@Override
	protected String doGetTextInfo(IDOMElement element) {
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
