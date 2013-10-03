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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchResult;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchResultPage;
import org.eclipse.wst.xml.search.ui.participant.IMatchPresentation;
import org.eclipse.wst.xml.search.ui.util.DOMUtils;
import org.w3c.dom.Node;

/**
 * 
 * {@link LabelProvider} implementation for DOM-SSE Node {@link IDOMNode}.
 * 
 */
public class XMLLabelProvider extends LabelProvider implements
		IStyledLabelProvider {

	private final XMLSearchResultPage fPage;
	private final WorkbenchLabelProvider fLabelProvider;
	private final Image fAttributeMatchImage;
	private final Image fElementMatchImage;
	private final Image fTextMatchImage;
	private final Map<IMatchPresentation, ILabelProvider> fLabelProviderMap;
	private final ListenerList fListeners;

	public XMLLabelProvider(XMLSearchResultPage page) {
		this.fPage = page;
		this.fLabelProvider = new WorkbenchLabelProvider();
		this.fAttributeMatchImage = ImageResource
				.getImage(ImageResource.IMG_ATTRIBUTE_OBJ);
		this.fTextMatchImage = ImageResource
				.getImage(ImageResource.IMG_TEXT_OBJ);
		this.fElementMatchImage = ImageResource
				.getImage(ImageResource.IMG_ELEMENT_OBJ);
		this.fLabelProviderMap = new HashMap<IMatchPresentation, ILabelProvider>(
				5);
		this.fListeners = new ListenerList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof IResource) {
			IResource resource = (IResource) element;
			return resource.getName();
		}
		if (element instanceof IDOMNode) {
			IDOMNode node = (IDOMNode) element;
			return DOMUtils.toString(node);
		}
		return getParticipantText(element);
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
		return getStyledParticipantText(element);
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
		if (!(element instanceof IResource)) {
			return getParticipantImage(element);
		}

		// Returns icon according the IResource type.
		IResource resource = (IResource) element;
		Image image = fLabelProvider.getImage(resource);
		return image;
	}

	protected String getParticipantText(Object element) {
		ILabelProvider labelProvider = getLabelProvider(element);
		if (labelProvider != null)
			return labelProvider.getText(element);
		return ""; //$NON-NLS-1$

	}

	protected StyledString getStyledParticipantText(Object element) {
		ILabelProvider labelProvider = getLabelProvider(element);
		if (labelProvider instanceof IStyledLabelProvider)
			return ((IStyledLabelProvider) labelProvider)
					.getStyledText(element);
		if (labelProvider != null)
			return new StyledString(labelProvider.getText(element));
		return new StyledString();
	}

	protected Image getParticipantImage(Object element) {
		ILabelProvider lp = getLabelProvider(element);
		if (lp == null)
			return null;
		return lp.getImage(element);
	}

	private ILabelProvider getLabelProvider(Object element) {
		AbstractTextSearchResult input = fPage.getInput();
		if (!(input instanceof XMLSearchResult))
			return null;

		IMatchPresentation participant = ((XMLSearchResult) input)
				.getSearchParticpant(element);
		if (participant == null)
			return null;

		ILabelProvider lp = fLabelProviderMap.get(participant);
		if (lp == null) {
			lp = participant.createLabelProvider();
			fLabelProviderMap.put(participant, lp);

			Object[] listeners = fListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				lp.addListener((ILabelProviderListener) listeners[i]);
			}
		}
		return lp;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		super.addListener(listener);
		for (Iterator<ILabelProvider> labelProviders = fLabelProviderMap
				.values().iterator(); labelProviders.hasNext();) {
			ILabelProvider labelProvider = labelProviders.next();
			labelProvider.addListener(listener);
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		/*
		 * if (PROPERTY_MATCH_COUNT.equals(property)) return true;
		 */
		return getLabelProvider(element).isLabelProperty(element, property);
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		super.removeListener(listener);
		for (Iterator<ILabelProvider> labelProviders = fLabelProviderMap
				.values().iterator(); labelProviders.hasNext();) {
			ILabelProvider labelProvider = labelProviders.next();
			labelProvider.removeListener(listener);
		}
	}

}
