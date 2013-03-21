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

import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;

public class DefaultTextInfo<T extends Object> {

	private StringBuilder result = null;
	protected final IContentAssistAdditionalProposalInfoProvider<T> provider;

	public DefaultTextInfo(
			IContentAssistAdditionalProposalInfoProvider<T> provider) {
		this.provider = provider;
	}

	public boolean add(T node) {
		String textInfo = getTextInfo(node);
		if (StringUtils.isEmpty(textInfo)) {
			return true;
		}
		if (result == null) {
			result = new StringBuilder();
		} else {
			result.append("<br/><br/>");
		}
		result.append(textInfo);
		return true;
	}
	
	protected String getTextInfo(T node) {
		return provider.getTextInfo(node);
	}

	public StringBuilder getResult() {
		return result;
	}

	public String getTextInfo() {
		return (result != null ? result.toString() : null);
	}
}
