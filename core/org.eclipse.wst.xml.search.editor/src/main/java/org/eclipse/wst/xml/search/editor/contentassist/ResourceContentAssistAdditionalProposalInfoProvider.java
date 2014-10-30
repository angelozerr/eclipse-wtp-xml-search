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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.xml.search.core.util.StringUtils;

public class ResourceContentAssistAdditionalProposalInfoProvider implements
		IContentAssistAdditionalProposalInfoProvider<IResource> {

	private static ILabelProvider WORKBENCH_LABEL_PROVIDER = null;
	public static final IContentAssistAdditionalProposalInfoProvider<IResource> INSTANCE = new ResourceContentAssistAdditionalProposalInfoProvider();

	public Image getImage(IResource resource) {
		if (WORKBENCH_LABEL_PROVIDER == null) {
			WORKBENCH_LABEL_PROVIDER = new WorkbenchLabelProvider();
		}
		return WORKBENCH_LABEL_PROVIDER.getImage(resource);
	}

	public String getDisplayText(String displayText, IResource resource) {
		return displayText;
	}

	public String getTextInfo(IResource resource) {
		StringBuilder buf = new StringBuilder();
		buf.append("<b>Path:</b> ");
		buf.append("/" + resource.getProject().getName() + "/"
				+ resource.getProjectRelativePath().toString());
		buf.append("<br><b>Type:</b> ");
		switch (resource.getType()) {
		case IResource.FILE:
			buf.append("File");
			String type = getType((IFile) resource);
			if (!StringUtils.isEmpty(type)) {
				buf.append(" (");
				buf.append(type);
				buf.append(")");
			}
			break;
		case IResource.PROJECT:
			buf.append("Project");
			break;
		case IResource.FOLDER:
			buf.append("Folder");
			break;
		}
		buf.append("<br><b>Location:</b> ");
		buf.append(resource.getLocation().toString());
		return buf.toString();
	}

	protected String getType(IFile file) {
		try {
			IContentDescription contentDescription = file
					.getContentDescription();
			if (contentDescription != null) {
				IContentType contentType = contentDescription.getContentType();
				if (contentType != null) {
					return contentType.getName();
				}
			}
		} catch (CoreException e) {
			// ignore error
		}
		return null;
	}
}
