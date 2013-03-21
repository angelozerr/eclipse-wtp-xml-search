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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;

/**
 * JFace {@link IHyperlink} implementation for {@link IResource}.
 * 
 */
public class ResourceHyperlink implements IHyperlink {

	private final IRegion region;
	private final IResource resource;
	private final int startOffset;
	private final int length;

	public ResourceHyperlink(IRegion region, IResource file, int startOffset,
			int length) {
		this.region = region;
		this.resource = file;
		this.startOffset = startOffset;
		this.length = length;
	}

	public ResourceHyperlink(IRegion region, IResource file) {
		this(region, file, 0, 0);
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return (new StringBuilder("Open '"))
				.append("/"
						+ resource.getProject().getName()
						+ "/"
						+ resource.getProjectRelativePath().toString()
								.toString()).append("'").toString();
	}

	public void open() {
		switch (resource.getType()) {
		case IResource.FILE:
			EditorUtils.openInEditor((IFile) resource, startOffset, length,
					true);
			break;
		}
		// TODO : manage another resource type.

	}

}
