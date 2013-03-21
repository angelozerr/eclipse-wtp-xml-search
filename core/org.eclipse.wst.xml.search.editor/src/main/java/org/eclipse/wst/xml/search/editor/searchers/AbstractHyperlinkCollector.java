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
package org.eclipse.wst.xml.search.editor.searchers;

import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

public abstract class AbstractHyperlinkCollector {

	protected final List<IHyperlink> hyperLinks;
	protected final IRegion hyperlinkRegion;

	public AbstractHyperlinkCollector(IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks) {
		this.hyperlinkRegion = hyperlinkRegion;
		this.hyperLinks = hyperLinks;
	}

}