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
package org.eclipse.wst.xml.search.editor.hyperlink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLReferencesHyperlinkDetector extends AbstractHyperlinkDetector {

	public static final IHyperlink[] EMPTY_HYPERLINK = new IHyperlink[0];

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		if (region == null || textViewer == null)
			return EMPTY_HYPERLINK;
		IDocument document = textViewer.getDocument();
		// Get the selected Node.
		Node currentNode = DOMUtils.getNodeByOffset(document,
				region.getOffset());
		if (currentNode == null) {
			return EMPTY_HYPERLINK;
		}
		int nodeType = currentNode.getNodeType();
		switch (nodeType) {
		case Node.ELEMENT_NODE:
			// Attribute node from element is selected
			return detectHyperlinks((Element) currentNode, textViewer, region,
					canShowMultipleHyperlinks);
		case Node.TEXT_NODE:
			// Text node is selected
			return detectHyperlinks((Text) currentNode, textViewer, region,
					canShowMultipleHyperlinks);
		}
		return EMPTY_HYPERLINK;
	}

	private IHyperlink[] detectHyperlinks(Element element,
			ITextViewer textViewer, IRegion region,
			boolean canShowMultipleHyperlinks) {
		// Get selected attribute
		IDOMAttr attr = DOMUtils.getAttrByOffset(element, region.getOffset());
		if (attr == null) {
			return EMPTY_HYPERLINK;
		}
		return detectHyperlinks(attr, textViewer, region,
				canShowMultipleHyperlinks);
	}

	private IHyperlink[] detectHyperlinks(Text text, ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		return detectHyperlinks((IDOMNode) text, textViewer, region,
				canShowMultipleHyperlinks);
	}

	private IHyperlink[] detectHyperlinks(IDOMNode selectedNode,
			ITextViewer textViewer, IRegion region,
			boolean canShowMultipleHyperlinks) {
		// Get XML reference for the selected attribute
		IXMLReference reference = XMLReferencesUtil.getXMLReference(
				selectedNode, selectedNode.getModel()
						.getContentTypeIdentifier());
		if (reference == null) {
			return EMPTY_HYPERLINK;
		}

		IFile file = DOMUtils.getFile(selectedNode);
		IRegion hyperlinkRegion = HyperlinkUtils
				.getHyperlinkRegion(selectedNode);
		int offset = region.getOffset() - hyperlinkRegion.getOffset();
		List<IHyperlink> hyperLinks = new ArrayList<IHyperlink>();
		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);

		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLSearcher searcher = expression.getSearcher();
			if (searcher != null) {
				searcher.searchForHyperlink(selectedNode, offset,
						DOMUtils.getNodeValue(selectedNode), -1, -1, file,
						expression, hyperlinkRegion, hyperLinks, textEditor);
			}
		} else {

			Collection<IXMLReferenceTo> toPath = reference.getTo();
			for (IXMLReferenceTo referenceTo : toPath) {
				IXMLSearcher searcher = referenceTo.getSearcher();
				if (searcher != null) {
					searcher.searchForHyperlink(selectedNode, offset,
							DOMUtils.getNodeValue(selectedNode), -1, -1, file,
							referenceTo, hyperlinkRegion, hyperLinks,
							textEditor);
				}
			}
		}
		if (hyperLinks.size() == 0) {
			return EMPTY_HYPERLINK;
		}
		return hyperLinks.toArray(new IHyperlink[hyperLinks.size()]);
	}
}
