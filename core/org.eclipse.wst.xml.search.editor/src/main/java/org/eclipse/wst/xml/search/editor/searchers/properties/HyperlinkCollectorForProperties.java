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
package org.eclipse.wst.xml.search.editor.searchers.properties;

import java.util.List;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xml.search.core.properties.IPropertiesCollector;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.PropertiesFileHyperlink;

public class HyperlinkCollectorForProperties implements IPropertiesCollector {

	private List<IHyperlink> hyperLinks;
	private IRegion hyperlinkRegion;
	private IEditorPart editor;

	public HyperlinkCollectorForProperties(IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, IEditorPart editor) {
		this.hyperlinkRegion = hyperlinkRegion;
		this.hyperLinks = hyperLinks;
		this.editor = editor;
	}

	public boolean add(IStorage propertiesFile, String key, String name) {
		hyperLinks.add(new PropertiesFileHyperlink(hyperlinkRegion,
				propertiesFile, key, editor));
		return true;
	}
}