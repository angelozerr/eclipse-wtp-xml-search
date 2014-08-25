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
package org.eclipse.wst.xml.search.editor.hover;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesManager;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.searchers.XMLAssistSearcherBindingsManager;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.taginfo.XMLTagInfoHoverProcessor;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class XMLReferencesInfoHoverProcessor extends XMLTagInfoHoverProcessor {

	/**
	 * Retrieves documentation to display in the hover help popup.
	 * 
	 * @return String any documentation information to display <code>null</code>
	 *         if there is nothing to display.
	 * 
	 */
	protected String computeHoverHelp(ITextViewer textViewer,
			int documentPosition) {
		String result = null;

		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer,
				documentPosition);
		if (treeNode == null) {
			return null;
		}
		Node node = (Node) treeNode;

		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE)
				&& (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode parentNode = (IDOMNode) node;

		IStructuredDocumentRegion flatNode = ((IStructuredDocument) textViewer
				.getDocument()).getRegionAtCharacterOffset(documentPosition);
		if (flatNode != null) {
			ITextRegion region = flatNode
					.getRegionAtCharacterOffset(documentPosition);
			if (region != null) {
				result = computeRegionHelp(treeNode, parentNode, flatNode,
						region, documentPosition);
			}
		}

		return result;
	}

	/**
	 * Computes the hoverhelp based on region
	 * 
	 * @return String hoverhelp
	 */
	// @Override
	protected String computeRegionHelp(IndexedRegion treeNode,
			IDOMNode parentNode, IStructuredDocumentRegion flatNode,
			ITextRegion region, int documentPosition) {
		String result = null;
		if (region == null) {
			return null;
		}
		String regionType = region.getType();
		if (regionType == DOMRegionContext.XML_TAG_NAME) {
			result = computeTagNameHelp((IDOMNode) treeNode, parentNode,
					flatNode, region);
		} else if (regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
			result = computeTagAttNameHelp((IDOMNode) treeNode, parentNode,
					flatNode, region);
		} else if (regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {
			int offset = documentPosition - flatNode.getStart()
					- region.getStart();
			result = computeTagAttValueHelp((IDOMNode) treeNode, parentNode,
					flatNode, region);
		}
		if (regionType == DOMRegionContext.XML_CONTENT) {
			int offset = documentPosition - flatNode.getStart();
			result = computeXMLContentValueHelp((IDOMNode) treeNode,
					parentNode, flatNode, region);
		}
		return result;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		if ((textViewer == null) || (textViewer.getDocument() == null)) {
			return null;
		}

		IStructuredDocumentRegion flatNode = ((IStructuredDocument) textViewer
				.getDocument()).getRegionAtCharacterOffset(offset);
		ITextRegion region = null;

		if (flatNode != null) {
			region = flatNode.getRegionAtCharacterOffset(offset);
		}

		if (region != null) {
			// only supply hoverhelp for tag name, attribute name, or
			// attribute value
			String regionType = region.getType();
			if ((regionType == DOMRegionContext.XML_TAG_NAME)
					|| (regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME)
					|| (regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE || regionType == DOMRegionContext.XML_CONTENT)) {
				try {
					// check if we are at whitespace before or after line
					IRegion line = textViewer.getDocument()
							.getLineInformationOfOffset(offset);
					if ((offset > (line.getOffset()))
							&& (offset < (line.getOffset() + line.getLength()))) {
						// check if we are in region's trailing whitespace
						// (whitespace after relevant info)
						if (offset < flatNode.getTextEndOffset(region)) {
							return new Region(flatNode.getStartOffset(region),
									region.getTextLength());
						}
					}
				} catch (BadLocationException e) {
					Logger.logException(e);
				}
			}
		}
		return null;
	}

	@Override
	protected String computeTagAttValueHelp(IDOMNode xmlnode,
			IDOMNode parentNode, IStructuredDocumentRegion flatNode,
			ITextRegion region) {
		IDOMAttr attr = DOMUtils.getAttrByRegion(xmlnode, region);
		if (attr != null) {
			String textInfo = getTextInfo(attr);
			if (!StringUtils.isEmpty(textInfo)) {
				return textInfo;
			}
		}
		return super.computeTagAttValueHelp(xmlnode, parentNode, flatNode,
				region);
	}

	private String getTextInfo(IDOMNode selectedNode) {
		IXMLReference reference = XMLReferencesManager.getInstance()
				.getXMLReference(selectedNode,
						selectedNode.getModel().getContentTypeIdentifier());
		if (reference != null) {
			StringBuilder infos = null;
			IFile file = DOMUtils.getFile(selectedNode);
			if (reference.isExpression()) {
				IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
				IXMLAssistSearcher searcher = XMLAssistSearcherBindingsManager.getDefault().
				                getXMLAssistSearcher(reference);
				if (searcher != null) {
					String textInfo = searcher.searchForTextHover(selectedNode,
							-1, DOMUtils.getNodeValue(selectedNode), -1, -1,
							file, expression);
					infos = getTextHover(infos, textInfo);
				}
			} else {
				Collection<IXMLReferenceTo> to = reference.getTo();
				for (IXMLReferenceTo referenceTo : to) {
					IXMLAssistSearcher searcher = XMLAssistSearcherBindingsManager.
					                getDefault().getXMLAssistSearcher(referenceTo);
					if (searcher != null) {
						String textInfo = searcher.searchForTextHover(
								selectedNode, -1,
								DOMUtils.getNodeValue(selectedNode), -1, -1,
								file, referenceTo);
						infos = getTextHover(infos, textInfo);
					}
				}
			}

			if (infos != null && infos.length() > 0) {
				return infos.toString();
			}
		}
		return null;
	}

	private StringBuilder getTextHover(StringBuilder infos, String textInfo) {
		if (!StringUtils.isEmpty(textInfo)) {
			if (infos == null) {
				infos = new StringBuilder();
			} else {
				infos.append("<br /><br />");
			}
			infos.append(textInfo);
		}
		return infos;
	}

	private String computeXMLContentValueHelp(IDOMNode xmlnode,
			IDOMNode parentNode, IStructuredDocumentRegion flatNode,
			ITextRegion region) {
		return getTextInfo(xmlnode);
	}
}
