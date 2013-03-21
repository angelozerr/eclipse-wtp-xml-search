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
package org.eclipse.wst.xml.search.editor.searchers.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.searchers.DefaultTextInfo;

public class TextHoverForResource extends DefaultTextInfo<IResource> implements
		IResourceCollector {

	public TextHoverForResource(
			IContentAssistAdditionalProposalInfoProvider<IResource> provider) {
		super(provider);
	}

	public boolean add(IResource file, IResource rootContainer,
			IURIResolver resolver) {
		return super.add(file);
	}

}