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
package org.eclipse.wst.xml.search.editor.internal.style;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionCollection;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.ui.internal.style.IStyleConstantsXML;
import org.eclipse.wst.xml.ui.internal.style.LineStyleProviderForXML;
import org.w3c.dom.Node;

public class LineStyleProviderForXMLReferences extends LineStyleProviderForXML implements IReferencesStyleConstantsXML {

	private IDOMModel model;

	public LineStyleProviderForXMLReferences(IDOMModel model) {
		this.model = model;
	}

	@Override
	protected TextAttribute getAttributeFor(ITextRegionCollection collection,
			ITextRegion region) {
		if (!getXMLSearchEditorColorPreferences().getBoolean(ENABLED_COLOR)) {
			return super.getAttributeFor(collection, region);
		}
		
		if (region == null) {
			return (TextAttribute) getTextAttributes().get(
					IStyleConstantsXML.CDATA_TEXT);
		}
		String type = region.getType();
		if ((type == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE)) {
			IDOMNode node = DOMUtils.getNodeByOffset(model, collection
					.getStart());
			if (node != null) {
				IDOMAttr attr = DOMUtils.getAttrByOffset(node, collection
						.getStart()
						+ region.getStart());
				if (attr != null) {
					IXMLReference reference = XMLReferencesUtil
							.getXMLReference(attr, model
									.getContentTypeIdentifier());
					if (reference != null) {
						return (TextAttribute) getTextAttributes()
								.get(
										TAG_REFERENCED_ATTRIBUTE_VALUE);
					}
				}
			}
		} else if ((type == DOMRegionContext.XML_CONTENT)) {
			IDOMNode node = DOMUtils.getNodeByOffset(model, collection
					.getStart());
			if (node != null && node.getNodeType() == Node.TEXT_NODE) {
				IXMLReference reference = XMLReferencesUtil.getXMLReference(
						node, model.getContentTypeIdentifier());
				if (reference != null) {
					return (TextAttribute) getTextAttributes()
							.get(
									XML_REFERENCED_CONTENT);
				}
			}
		}
		return super.getAttributeFor(collection, region);
	}

	@Override
	protected void loadColors() {
		super.loadColors();
		addTextAttribute(TAG_REFERENCED_ATTRIBUTE_VALUE);
		addTextAttribute(XML_REFERENCED_CONTENT);
	}

	/**
	 * Looks up the colorKey in the preference store and adds the style
	 * information to list of TextAttributes
	 * 
	 * @param colorKey
	 */
	protected void addTextAttribute(String colorKey) {
		IPreferenceStore colorPreferences = null;
		if (TAG_REFERENCED_ATTRIBUTE_VALUE
				.equals(colorKey)
				|| XML_REFERENCED_CONTENT
						.equals(colorKey)) {
			colorPreferences = getXMLSearchEditorColorPreferences();
			;
		} else {
			colorPreferences = super.getColorPreferences();
		}
		if (colorPreferences != null) {

			String prefString = colorPreferences.getString(colorKey);
			String[] stylePrefs = ColorHelper
					.unpackStylePreferences(prefString);
			if (stylePrefs != null) {
				RGB foreground = ColorHelper.toRGB(stylePrefs[0]);
				RGB background = ColorHelper.toRGB(stylePrefs[1]);
				boolean bold = Boolean.valueOf(stylePrefs[2]).booleanValue();
				boolean italic = Boolean.valueOf(stylePrefs[3]).booleanValue();
				boolean strikethrough = Boolean.valueOf(stylePrefs[4])
						.booleanValue();
				boolean underline = Boolean.valueOf(stylePrefs[5])
						.booleanValue();
				int style = SWT.NORMAL;
				if (bold) {
					style = style | SWT.BOLD;
				}
				if (italic) {
					style = style | SWT.ITALIC;
				}
				if (strikethrough) {
					style = style | TextAttribute.STRIKETHROUGH;
				}
				if (underline) {
					style = style | TextAttribute.UNDERLINE;
				}

				TextAttribute createTextAttribute = createTextAttribute(
						foreground, background, style);
				getTextAttributes().put(colorKey, createTextAttribute);
			}
		}
	}

	protected IPreferenceStore getXMLSearchEditorColorPreferences() {
		return XMLSearchEditorPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected void registerPreferenceManager() {
		super.registerPreferenceManager();
		IPreferenceStore pref = getXMLSearchEditorColorPreferences();
		if (pref != null) {
			pref.addPropertyChangeListener(fPreferenceListener);
		}
	}

	@Override
	protected void unRegisterPreferenceManager() {
		super.unRegisterPreferenceManager();
		IPreferenceStore pref = getXMLSearchEditorColorPreferences();
		if (pref != null) {
			pref.removePropertyChangeListener(fPreferenceListener);
		}
	}

	@Override
	protected void handlePropertyChange(PropertyChangeEvent event) {
		String styleKey = null;

		if (event != null) {
			String prefKey = event.getProperty();
			// check if preference changed is a style preference
			if (TAG_REFERENCED_ATTRIBUTE_VALUE
					.equals(prefKey)) {
				styleKey = TAG_REFERENCED_ATTRIBUTE_VALUE;
			} else if (XML_REFERENCED_CONTENT
					.equals(prefKey)) {
				styleKey = XML_REFERENCED_CONTENT;
			} else if (ENABLED_COLOR
					.equals(prefKey)) {
				if (fRecHighlighter != null)
					fRecHighlighter.refreshDisplay();				
			}
		}

		if (styleKey != null) {
			// overwrite style preference with new value
			addTextAttribute(styleKey);
			// force a full update of the text viewer
			if (fRecHighlighter != null)
				fRecHighlighter.refreshDisplay();
		} else {
			super.handlePropertyChange(event);
		}
	}

}
