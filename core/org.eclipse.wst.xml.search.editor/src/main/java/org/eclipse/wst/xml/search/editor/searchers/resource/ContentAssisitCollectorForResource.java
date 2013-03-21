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
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToResource;

public class ContentAssisitCollectorForResource<T> implements IResourceCollector {

	private final T selectedNode;
	private IContentAssistProposalRecorder recorder;
	private final IContentAssistAdditionalProposalInfoProvider<IResource> provider;
	
	public ContentAssisitCollectorForResource(T selectedNode,
			IContentAssistProposalRecorder recorder,
			IContentAssistAdditionalProposalInfoProvider<IResource> provider) {
		this.selectedNode = selectedNode;
		this.recorder = recorder;
		this.provider = provider;
	}

	public boolean add(IResource file, IResource rootContainer,
			IURIResolver resolver) {
		Image image = null;
		int relevance = 1;
		String value = resolver.resolve(selectedNode, rootContainer, file);
		String displayText = value;
		String replaceText = value;
		Object proposedObject = null;

		if (provider != null) {
			String newDisplayText = provider.getDisplayText(displayText,
					file);
			if (!StringUtils.isEmpty(newDisplayText)) {
				displayText = newDisplayText;
			}
			image = provider.getImage(file);
			proposedObject = provider.getTextInfo(file);
		}
		recorder.recordProposal(image, relevance, displayText, replaceText,
				proposedObject);

		return true;
	}
}