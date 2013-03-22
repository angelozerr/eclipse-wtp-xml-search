package org.apache.struts2.ide.ui.internal.contentassist;

import org.apache.struts2.ide.ui.internal.ImageResource;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.search.editor.contentassist.ResourceContentAssistAdditionalProposalInfoProvider;

public class Struts2FileContentAssistAdditionalProposalInfoProvider extends
		ResourceContentAssistAdditionalProposalInfoProvider {

	@Override
	public Image getImage(IResource resource) {
		return ImageResource.getImage(ImageResource.IMG_STRUTS2_OBJ);
	}
}
