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
package org.eclipse.wst.xml.search.editor.searchers.xml;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.searchers.DefaultTextInfo;

public class TextInfoForXML extends DefaultTextInfo<IDOMNode> implements
		IXMLSearchDOMNodeCollector {

	public TextInfoForXML(
			IContentAssistAdditionalProposalInfoProvider<IDOMNode> provider) {
		super(provider);
	}

}