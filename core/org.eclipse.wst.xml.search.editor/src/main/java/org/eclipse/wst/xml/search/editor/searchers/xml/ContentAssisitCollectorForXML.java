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
package org.eclipse.wst.xml.search.editor.searchers.xml;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToXML;
import org.eclipse.wst.xml.search.editor.internal.contentassist.ContentAssistBindingsManager;
import org.eclipse.wst.xml.search.editor.searchers.AbstractContentAssisitCollector;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class ContentAssisitCollectorForXML extends
		AbstractContentAssisitCollector<IXMLReferenceToXML> implements
		IXMLSearchDOMNodeCollector {

	public ContentAssisitCollectorForXML(String forceBeforeText,
			String forceEndText, IXMLReferenceToXML referencePath,
			IContentAssistProposalRecorder recorder) {
		super(forceBeforeText, forceEndText, referencePath, recorder);
	}

	public boolean add(IDOMNode node) {
		collect(recorder, node, referencePath);
		return true;
	}

	private void collect(final IContentAssistProposalRecorder recorder,
			IDOMNode node, IXMLReferenceToXML referencePath) {
		String value = null;
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			value = ((Attr) node).getValue();
			break;
		case Node.TEXT_NODE:
			value = DOMUtils.getTextContent((Text) node);
			break;
		case Node.ELEMENT_NODE:
			String targetNode = referencePath.getTargetNodes()[0];
			if (targetNode.startsWith("@")) {
				String attrName = targetNode.substring(1, targetNode.length());
				String attrValue = ((Element) node).getAttribute(attrName);
				value = attrValue;
			} else {
				// text node
				Element element = ((Element) node);
				Node firstChild = element.getFirstChild();
				if (firstChild != null
						&& firstChild.getNodeType() == Node.TEXT_NODE) {
					value = DOMUtils.getTextContent((Text) firstChild);
				}
			}
			break;
		}
		if (value == null) {
			return;
		}

		Image image = null;
		int relevance = 1000;
		String displayText = value;
		String replaceText = getReplaceText(value);

		Object proposedObject = null;

		IContentAssistAdditionalProposalInfoProvider provider =
                ContentAssistBindingsManager.getDefault().getProvider(referencePath);
		if (provider != null) {
			String newDisplayText = provider.getDisplayText(displayText, node);
			if (!StringUtils.isEmpty(newDisplayText)) {
				displayText = newDisplayText;
			}
			image = provider.getImage(node);
			proposedObject = provider.getTextInfo(node);
		}
		int cursorPosition = getCursorPosition(value);
		recorder.recordProposal(image, relevance, displayText, replaceText,
				cursorPosition, proposedObject);
	}

}
