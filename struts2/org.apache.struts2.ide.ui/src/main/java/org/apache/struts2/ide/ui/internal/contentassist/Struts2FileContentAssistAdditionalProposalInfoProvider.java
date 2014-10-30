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
