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

import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.searchers.DefaultTextInfo;

public class TextInfoForStatic extends DefaultTextInfo<IStaticValue> implements
		IStaticValueCollector {

	public TextInfoForStatic(
			IContentAssistAdditionalProposalInfoProvider<IStaticValue> provider) {
		super(provider);
	}

	@Override
	protected String getTextInfo(IStaticValue value) {
		if (provider == null) {
			return value.getDescription();
		}
		return super.getTextInfo(value);
	}

}