package org.apache.struts2.ide.ui.internal.contentassist;

import org.apache.struts2.ide.core.Struts2Constants;
import org.apache.struts2.ide.ui.internal.ImageResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Struts2InterceptorStackContentAssistAdditionalProposalInfoProvider
		extends Struts2ContentAssistAdditionalProposalInfoProvider implements
		Struts2Constants {

	public Image getImage(Node node) {
		return ImageResource.getImage(ImageResource.IMG_INTERCEPTOR_STACK_OBJ);
	}

	@Override
	protected String doGetTextInfo(IDOMElement element) {
		StringBuilder buf = new StringBuilder();
		buf.append("<b>Interceptor Stack:</b> ");
		buf.append(element.getAttribute(NAME_ATTR));

		buf.append("<br><ul><b>Interceptor-ref lists:</b> ");
		Element interceptorRef = null;
		NodeList interceptorRefs = element
				.getElementsByTagName("interceptor-ref");
		for (int i = 0; i < interceptorRefs.getLength(); i++) {
			interceptorRef = (Element) interceptorRefs.item(i);
			buf.append("<li><b>Interceptor-ref:</b> ");
			buf.append(interceptorRef.getAttribute(NAME_ATTR));
			buf.append("</li>");
		}
		buf.append("</ul>");

		buf.append("<br><b>File:</b> ");
		String baseLocation = element.getModel().getBaseLocation();
		buf.append(baseLocation);
		return buf.toString();
	}

}
