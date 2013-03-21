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
package org.eclipse.wst.xml.search.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.ui.util.DOMUtils;
import org.w3c.dom.Node;

/**
 * 
 * {@link LabelProvider} implementation for DOM-SSE Node {@link IDOMNode}.
 * 
 */
public class XMLLabelProvider extends LabelProvider implements
		IStyledLabelProvider {

	private final WorkbenchLabelProvider fLabelProvider;
	private final Image fAttributeMatchImage;
	private final Image fElementMatchImage;
	private final Image fTextMatchImage;

	public XMLLabelProvider() {
		this.fLabelProvider = new WorkbenchLabelProvider();
		this.fAttributeMatchImage = ImageResource
				.getImage(ImageResource.IMG_ATTRIBUTE_OBJ);
		this.fTextMatchImage = ImageResource
				.getImage(ImageResource.IMG_TEXT_OBJ);
		this.fElementMatchImage = ImageResource
				.getImage(ImageResource.IMG_ELEMENT_OBJ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		return getStyledText(object).getString();
	}

	public StyledString getStyledText(Object element) {
		if (element instanceof IResource) {
			IResource resource = (IResource) element;
			return new StyledString(resource.getName());
		}
		if (element instanceof IDOMNode) {
			IDOMNode node = (IDOMNode) element;
			return new StyledString(DOMUtils.toString(node));
		}
		return new StyledString(element.toString());
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Node) {
			// Element is DOM node
			Node node = (Node) element;
			switch (node.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
				return fAttributeMatchImage;
				// returns attribute icon
			case Node.TEXT_NODE:
				// returns text icon
				return fTextMatchImage;
			case Node.ELEMENT_NODE:
				// returns element icon
				return fElementMatchImage;
			}
		}
		if (!(element instanceof IResource))
			return null;

		// Returns icon according the IResource type.
		IResource resource = (IResource) element;
		Image image = fLabelProvider.getImage(resource);
		return image;
	}

}
