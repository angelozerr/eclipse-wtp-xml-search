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

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.internal.contentassist.XMLReferencesContentAssistCalculator;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.w3c.dom.Node;

public class XMLReferencesContentAssistUtils {

	public static void addAttributeValueProposals(ContentAssistRequest request) {
		IDOMNode node = (IDOMNode) request.getNode();

		// Find the attribute
		IDOMAttr selectedNode = DOMUtils.getAttrByRegion(node,
				request.getRegion());
		if (selectedNode == null) {
			return;
		}
		String matchString = EditorUtils.prepareMatchString(request);
		IXMLReference reference = XMLReferencesUtil.getXMLReference(
				selectedNode, selectedNode.getModel()
						.getContentTypeIdentifier());
		if (reference != null) {
			IContentAssistCalculator calculator = new XMLReferencesContentAssistCalculator(
					reference, selectedNode);
			if (calculator != null) {
				IContentAssistContext context = new DOMNodeContentAssistContext(
						request, matchString);
				IContentAssistProposalRecorder recorder = new DOMAttrContentAssistProposalRecorder(
						request);
				calculator.computeProposals(context, recorder);
			}
		}
	}

	public static void addEntityProposals(
			IContentAssistProcessor delegatingContentAssistProcessor,
			ContentAssistRequest request) {

		String textContent = "";
		IDOMNode node = (IDOMNode) request.getNode();
		IDOMText text = getDOMText(node);
		String matchString = "";
		int startOffset = request.getStartOffset();
		if (text != null) {
			matchString = text.getData();
			textContent = matchString;
			int replacementBeginPosition = request
					.getReplacementBeginPosition();
			startOffset = text.getStartOffset();
			int l = replacementBeginPosition - startOffset;
			matchString = matchString.replaceAll("\r", "");
			matchString = matchString.trim();
			if (matchString.length() > l) {
				matchString = matchString.substring(0, l);
			}

			node = text;
		}

		String prefix = null;
		String namespace = null;
		if (prefix == null && namespace == null) {
			prefix = node.getPrefix();
			namespace = node.getNamespaceURI();
		}
		computeDOMTextProposals(request, node, matchString, namespace, prefix,
				textContent, startOffset);
	}

	private static IDOMText getDOMText(IDOMNode node) {
		if (node == null) {
			return null;
		}
		if (node.getNodeType() == Node.TEXT_NODE) {
			return (IDOMText) node;
		}
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			IDOMNode firstChild = (IDOMNode) node.getFirstChild();
			return getDOMText(firstChild);
		}
		return null;
	}

	private static void computeDOMTextProposals(ContentAssistRequest request,
			IDOMNode node, String matchString, String namespace, String prefix,
			String textContent, int startOffset) {
		IDOMNode selectedNode = node;
		IXMLReference reference = XMLReferencesUtil.getXMLReference(
				selectedNode, EditorUtils.getFile(request));
		if (reference != null) {
			IContentAssistCalculator calculator = new XMLReferencesContentAssistCalculator(
					reference, selectedNode);
			if (calculator != null) {
				IContentAssistContext context = new DOMNodeContentAssistContext(
						request, matchString);
				IContentAssistProposalRecorder recorder = new DOMTextContentAssistProposalRecorder(
						request, textContent, startOffset);
				calculator.computeProposals(context, recorder);
			}
		}
	}

}
