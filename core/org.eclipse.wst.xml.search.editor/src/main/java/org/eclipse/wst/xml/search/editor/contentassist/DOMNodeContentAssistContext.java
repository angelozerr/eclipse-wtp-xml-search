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

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.w3c.dom.Node;

/**
 * 
 * Default implementation of {@link IContentAssistContext} that is used on the
 * WTP XML editor
 * 
 * environment.
 * 
 * <p>
 * 
 * This implementation wraps the WTP internal class {@link ContentAssistRequest}.
 * 
 * 
 * @since 2.2.1
 */

@SuppressWarnings("restriction")
public class DOMNodeContentAssistContext implements IContentAssistContext {

	private final String matchString;

	private final ContentAssistRequest request;

	/**
	 * 
	 * Creates a new {@link DOMNodeContentAssistContext}
	 */
	public DOMNodeContentAssistContext(ContentAssistRequest request, String matchString) {
		this.request = request;
		this.matchString = matchString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.editor.contentassist.IContentAssistContext
	 * #getFile()
	 */
	public IFile getFile() {
		return DOMUtils.getFile((IDOMNode) getNode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.editor.contentassist.IContentAssistContext
	 * #getMatchString()
	 */
	public String getMatchString() {
		return matchString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.editor.contentassist.IContentAssistContext
	 * #getNode()
	 */
	public Node getNode() {
		return request.getNode();
	}

}
