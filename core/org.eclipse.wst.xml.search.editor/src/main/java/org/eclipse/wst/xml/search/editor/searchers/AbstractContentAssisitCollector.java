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

import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;

public class AbstractContentAssisitCollector<T extends IXMLReferenceTo> {

	protected final IContentAssistProposalRecorder recorder;
	protected final T referencePath;
	protected final String forceBeforeText;
	protected final String forceEndText;

	public AbstractContentAssisitCollector(String forceBeforeText,
			String forceEndText, T referencePath,
			IContentAssistProposalRecorder recorder) {
		this.forceBeforeText = forceBeforeText;
		this.forceEndText = forceEndText;
		this.recorder = recorder;
		this.referencePath = referencePath;
	}

	protected String getReplaceText(String replaceText) {
		if (forceBeforeText != null) {
			replaceText = forceBeforeText + replaceText;
		}
		if (forceEndText != null) {
			replaceText = replaceText + forceEndText;
		}
		return replaceText;
	}

	protected int getCursorPosition(String replaceText) {
		if (forceBeforeText != null) {
			return forceBeforeText.length() + replaceText.length();
		}
		return replaceText.length();
	}

}
