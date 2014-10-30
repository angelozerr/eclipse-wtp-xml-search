/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.xml.search.editor.searchers.resource;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.ResourceHyperlink;
import org.eclipse.wst.xml.search.editor.searchers.AbstractHyperlinkCollector;

public class HyperlinkCollectorForResource extends AbstractHyperlinkCollector
		implements IResourceCollector {

	public HyperlinkCollectorForResource(IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks) {
		super(hyperlinkRegion, hyperLinks);
	}

	public boolean add(IResource file, IResource rootContainer,
			IURIResolver resolver) {
		hyperLinks.add(new ResourceHyperlink(hyperlinkRegion, file));
		return true;
	}
}