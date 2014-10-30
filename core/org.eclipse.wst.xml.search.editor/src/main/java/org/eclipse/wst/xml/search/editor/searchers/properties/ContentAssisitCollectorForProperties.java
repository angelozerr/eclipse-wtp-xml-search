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
package org.eclipse.wst.xml.search.editor.searchers.properties;

import org.eclipse.core.resources.IStorage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.search.core.properties.IPropertiesCollector;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToProperty;

public class ContentAssisitCollectorForProperties implements
		IPropertiesCollector {

	private final Object selectedNode;
	private IContentAssistProposalRecorder recorder;
	private IXMLReferenceToProperty referenceToProperty;

	public ContentAssisitCollectorForProperties(Object selectedNode,
			IXMLReferenceToProperty referenceToProperty,
			IContentAssistProposalRecorder recorder) {
		this.selectedNode = selectedNode;
		this.recorder = recorder;
		this.referenceToProperty = referenceToProperty;
	}

	public boolean add(IStorage propertiesFile, String key, String name) {
		// TODO Auto-generated method stub
		Image image = null;
		int relevance = 1000;
		String value = key;// resolver.resolve(selectedNode, rootContainer,
							// file);
		String displayText = value;
		String replaceText = value;
		Object proposedObject = null;

		IContentAssistAdditionalProposalInfoProvider<PropertyInfo> provider = (IContentAssistAdditionalProposalInfoProvider<PropertyInfo>) referenceToProperty
				.getAdditionalProposalInfoProvider();
		if (provider != null) {
			PropertyInfo info = new PropertyInfo(propertiesFile, key, name);
			String newDisplayText = provider.getDisplayText(displayText, info);
			if (!StringUtils.isEmpty(newDisplayText)) {
				displayText = newDisplayText;
			}
			image = provider.getImage(info);
			proposedObject = provider.getTextInfo(info);
		}
		recorder.recordProposal(image, relevance, displayText, replaceText,
				proposedObject);

		return true;
	}
}