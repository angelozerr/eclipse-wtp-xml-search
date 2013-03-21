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
package org.eclipse.wst.xml.search.editor.internal.hyperlink;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;

/**
 * JFace {@link IHyperlink} implementation for DOM-SSE Node {@link IDOMNode}.
 * 
 */
public class DOMNodeHyperlink implements IHyperlink, IRegion {

	private int offset;
	private int length;
	private final IRegion region;
	private final IDOMNode node;

	public DOMNodeHyperlink(IRegion region, IDOMNode node, int startOffset,
			int endOffset) {
		this.region = region;
		this.node = node;
		this.offset = region.getOffset();
		if (startOffset != -1) {
			this.offset += startOffset;
		}
		this.length = region.getLength();
		if (endOffset != -1) {
			this.length = endOffset - startOffset;
		}
	}

	public IRegion getHyperlinkRegion() {
		return this;
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return (new StringBuilder("Open '")).append(node.getLocalName())
				.append("' - ").append(node.getModel().getBaseLocation())
				.toString();
	}

	public void open() {
		EditorUtils.openInEditor(node);
	}

	public int getLength() {
		return length;
	}

	public int getOffset() {
		return offset;
	}

}
