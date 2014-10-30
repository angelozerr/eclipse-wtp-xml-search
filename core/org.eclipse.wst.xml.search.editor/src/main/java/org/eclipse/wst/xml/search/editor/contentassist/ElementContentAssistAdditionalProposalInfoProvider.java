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

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Content assist additionnal proposal for {@link Element}.
 * 
 */
public abstract class ElementContentAssistAdditionalProposalInfoProvider extends
		NodeContentAssistAdditionalProposalInfoProvider {

	public String getTextInfo(Node node) {
		if (node == null) {
			return null;
		}
		Element element = DOMUtils.getOwnerElement(node);
		if (element != null) {
			return doGetTextInfo((IDOMElement) element);
		}
		return null;

	}

	protected abstract String doGetTextInfo(IDOMElement element);
}
