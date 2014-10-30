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
package org.eclipse.wst.xml.search.editor.searchers.statics;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.searchers.AbstractContentAssisitCollector;

public class ContentAssisitCollectorForStatics extends
		AbstractContentAssisitCollector<IXMLReferenceToStatic> implements
		IStaticValueCollector {

	public ContentAssisitCollectorForStatics(String forceBeforeText,
			String forceEndText, IXMLReferenceToStatic referencePath,
			IContentAssistProposalRecorder recorder) {
		super(forceBeforeText, forceEndText, referencePath, recorder);
	}

	public boolean add(IStaticValue value) {
		collect(recorder, value, referencePath);
		return false;
	}

	private void collect(final IContentAssistProposalRecorder recorder,
			IStaticValue staticValue, IXMLReferenceToStatic referenceToStatic) {
		String value = staticValue.getKey();
		if (value == null) {
			return;
		}

		Image image = null;
		int relevance = 1000;
		String displayText = value;
		String replaceText = getReplaceText(value);
		Object proposedObject = staticValue.getDescription();

		IContentAssistAdditionalProposalInfoProvider provider = referenceToStatic
				.getAdditionalProposalInfoProvider();
		if (provider != null) {
			String newDisplayText = provider.getDisplayText(displayText,
					staticValue);
			if (!StringUtils.isEmpty(newDisplayText)) {
				displayText = newDisplayText;
			}
			image = provider.getImage(staticValue);
			proposedObject = provider.getTextInfo(staticValue);
		}
		int cursorPosition = getCursorPosition(value);

		recorder.recordProposal(image, relevance, displayText, replaceText,
				cursorPosition, proposedObject);
	}

}
