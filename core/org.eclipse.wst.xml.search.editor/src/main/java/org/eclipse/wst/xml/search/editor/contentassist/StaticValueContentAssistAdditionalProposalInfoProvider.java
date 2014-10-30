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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.wst.xml.search.core.statics.IStaticValue;

public abstract class StaticValueContentAssistAdditionalProposalInfoProvider<T extends IStaticValue>
		implements IContentAssistAdditionalProposalInfoProvider<T> {

	public String getDisplayText(String displayText, T value) {
		return value.getKey();
	}

	public String getTextInfo(T value) {
		return value.getDescription();
	}
}
