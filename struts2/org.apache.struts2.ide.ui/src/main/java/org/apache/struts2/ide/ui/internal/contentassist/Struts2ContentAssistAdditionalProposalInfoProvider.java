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
