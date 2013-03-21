package org.apache.struts2.ide.ui.internal.contentassist;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.contentassist.ElementContentAssistAdditionalProposalInfoProvider;
import org.w3c.dom.Node;

public abstract class Struts2ContentAssistAdditionalProposalInfoProvider extends
		ElementContentAssistAdditionalProposalInfoProvider {

	@Override
	public String getDisplayText(String displayText, Node node) {
		String fileName = DOMUtils.getFileName((IDOMNode) node);
		return super
				.getDisplayText(displayText + " - [" + fileName + "]", node);
	}
}
