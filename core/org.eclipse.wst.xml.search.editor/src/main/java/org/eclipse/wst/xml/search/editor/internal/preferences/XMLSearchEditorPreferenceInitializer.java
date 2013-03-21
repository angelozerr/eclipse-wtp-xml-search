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
package org.eclipse.wst.xml.search.editor.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.editor.internal.style.IReferencesStyleConstantsXML;

public class XMLSearchEditorPreferenceInitializer extends
		AbstractPreferenceInitializer implements IReferencesStyleConstantsXML {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = XMLSearchEditorPlugin.getDefault()
				.getPreferenceStore();
		ColorRegistry registry = PlatformUI.getWorkbench().getThemeManager()
				.getCurrentTheme().getColorRegistry();

		// tagReferencedAttributeValue
		String BOLD_AND_ITALIC = " | null | true | true"; //$NON-NLS-1$
		String styleValue = ColorHelper.findRGBString(registry,
				TAG_REFERENCED_ATTRIBUTE_VALUE,
				42, 0, 255)
				+ BOLD_AND_ITALIC;
		store.setDefault(
				TAG_REFERENCED_ATTRIBUTE_VALUE,
				styleValue);

		// xmlReferencedContent
		styleValue = "null | null | true | false";
		store.setDefault(
				IReferencesStyleConstantsXML.XML_REFERENCED_CONTENT,
				styleValue);

		// enabledColor 
		store.setDefault(ENABLED_COLOR, true);
	}
}
