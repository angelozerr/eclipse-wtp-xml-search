/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.util;

import static org.eclipse.wst.xml.search.core.util.DOMUtils.getAttrByOffset;
import static org.eclipse.wst.xml.search.core.util.DOMUtils.getNodeByOffset;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.search.ui.internal.Trace;
import org.w3c.dom.Node;

/**
 * Utility class for DOM-SSE node.
 * 
 */
public class DOMUtils {

	/**
	 * Returns the selected Node from the given text editor.
	 * 
	 * @param textEditor
	 * @return
	 */
	public static IDOMNode getSelectedNode(ITextEditor textEditor) {
		ISelection selection = textEditor.getSelectionProvider().getSelection();
		if (selection.isEmpty()) {
			// No selection is the text editor, return null.
			return null;
		}

		ITextSelection textSelection = (ITextSelection) textEditor
				.getSelectionProvider().getSelection();
		if (selection instanceof IStructuredSelection) {
			// Selection is done
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object selectedElement = structuredSelection.getFirstElement();
			if (selectedElement instanceof IDOMNode) {
				// The first selected element is DOM-SSE Node.
				// Returns the DOM attribute if can and DOM node otherwise.
				IDOMNode selectedNode = (IDOMNode) selectedElement;
				return getSelectedAttrIfCan(textSelection, selectedNode);
			}
		}

		// Selected Node is not opened in WTP XML Editor
		IEditorInput editorInput = textEditor.getEditorInput();
		if (!(editorInput instanceof IFileEditorInput)) {
			return null;
		}

		// Open the DOM SEE to get the IDocument
		IFile file = ((IFileEditorInput) textEditor.getEditorInput()).getFile();
		// Load SSE Document to get selected Node
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(file);
			if (model == null) {
				model = StructuredModelManager.getModelManager()
						.getModelForRead(file);
			}
			if (model == null) {
				return null;
			}
			IDOMNode selectedNode = getNodeByOffset(model,
					textSelection.getOffset());
			return getSelectedAttrIfCan(textSelection, selectedNode);

		} catch (Throwable e) {
			Trace.trace(Trace.WARNING, e.getMessage(), e);
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return null;
	}

	/**
	 * Returns the selected attribute of the given text selection if can and the
	 * selected node otherwise.
	 * 
	 * @param textSelection
	 * @param selectedNode
	 * @return
	 */
	private static IDOMNode getSelectedAttrIfCan(ITextSelection textSelection,
			IDOMNode selectedNode) {
		IDOMAttr attr = getAttrByOffset(selectedNode, textSelection.getOffset());
		if (attr != null) {
			return attr;
		}
		return selectedNode;
	}

	public static String toString(IDOMNode node) {
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			return DOMUtils.toString((IDOMAttr) node);
		case Node.TEXT_NODE:
			return DOMUtils.toString((IDOMText) node);
		case Node.ELEMENT_NODE:
			return DOMUtils.toString((IDOMElement) node);
		}
		return node.toString();
	}

	public static String toString(IDOMAttr attr) {
		StringBuilder buffer = new StringBuilder();
		buffer.append('@');
		buffer.append(attr.getNodeName());
		buffer.append('(');
		buffer.append(attr.getValue());
		buffer.append(") ");
		toRegionString(attr, buffer);

		IDOMElement element = (IDOMElement) attr.getOwnerElement();
		if (element != null) {
			buffer.append(" - Owner element: ");
			toString(element, buffer);
		}
		return buffer.toString();
	}

	public static String toString(IDOMText text) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(text.getNodeName());
		buffer.append('(');
		buffer.append(text.getData());
		buffer.append(") ");
		toRegionString(text, buffer);
		// IStructuredDocumentRegion flatNode =
		// text.getStructuredDocumentRegion();
		// if (flatNode != null) {
		// buffer.append('@');
		// buffer.append(flatNode.toString());
		// }
		Node parentNode = text.getParentNode();
		if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
			buffer.append(" - Owner element: ");
			toString((IDOMElement) parentNode, buffer);
		}
		return buffer.toString();
	}

	public static void toString(IDOMElement element, StringBuilder buffer) {
		String tagName = element.getTagName();
		if (element.hasStartTag())
			buffer.append(tagName);
		if (element.isEmptyTag())
			buffer.append('/');
		if (element.hasEndTag()) {
			buffer.append('/');
			buffer.append(tagName);
		}
		if (buffer.length() == 0)
			buffer.append(tagName);

		toRegionString(element, buffer);

	}

	public static String toString(IDOMElement element) {
		StringBuilder buffer = new StringBuilder();
		toString(element, buffer);
		return buffer.toString();
	}

	private static void toRegionString(IDOMNode element, StringBuilder buffer) {
		IStructuredDocumentRegion startStructuredDocumentRegion = element
				.getStartStructuredDocumentRegion();
		if (startStructuredDocumentRegion != null) {
			buffer.append('@');
			buffer.append(startStructuredDocumentRegion.toString());
		}
		IStructuredDocumentRegion endStructuredDocumentRegion = element
				.getEndStructuredDocumentRegion();
		if (endStructuredDocumentRegion != null) {
			buffer.append('@');
			buffer.append(endStructuredDocumentRegion.toString());
		}
	}

}
