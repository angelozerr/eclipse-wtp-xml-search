package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.wst.xml.search.editor.statics.StaticValueDocument;

public abstract class StaticValueDocumentContentAssistAdditionalProposalInfoProvider
		extends
		StaticValueContentAssistAdditionalProposalInfoProvider<StaticValueDocument> {

	@Override
	public String getDisplayText(String displayText, StaticValueDocument value) {
		String fileName = value.getResource().getName();
		return displayText + " - [" + fileName + "]";
	}

}
