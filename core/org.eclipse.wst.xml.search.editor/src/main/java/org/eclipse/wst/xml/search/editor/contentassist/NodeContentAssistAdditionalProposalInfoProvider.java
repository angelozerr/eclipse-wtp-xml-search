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

import org.w3c.dom.Node;

public abstract class NodeContentAssistAdditionalProposalInfoProvider implements
		IContentAssistAdditionalProposalInfoProvider<Node> {

	public String getDisplayText(String displayText, Node node) {
		return displayText;
	}
}
