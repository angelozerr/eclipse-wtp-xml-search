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
package org.eclipse.wst.xml.search.editor.searchers.statics;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.ResourceHyperlink;
import org.eclipse.wst.xml.search.editor.searchers.AbstractHyperlinkCollector;
import org.eclipse.wst.xml.search.editor.statics.StaticValueDocument;

public class HyperlinkCollectorForStatics extends AbstractHyperlinkCollector
		implements IStaticValueCollector {

	public HyperlinkCollectorForStatics(IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks) {
		super(hyperlinkRegion, hyperLinks);
	}

	public boolean add(IStaticValue value) {
		if (value instanceof StaticValueDocument) {
			StaticValueDocument document = (StaticValueDocument) value;
			IResource file = document.getResource();
			hyperLinks.add(new ResourceHyperlink(hyperlinkRegion, file,
					document.getStartOffset(), document.getLength()));
		}
		return true;
	}

}