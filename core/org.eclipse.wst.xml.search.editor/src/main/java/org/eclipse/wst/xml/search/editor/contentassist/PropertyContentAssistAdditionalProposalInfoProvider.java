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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.core.resources.IStorage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.search.editor.internal.ImageResource;
import org.eclipse.wst.xml.search.editor.searchers.properties.PropertyInfo;

public class PropertyContentAssistAdditionalProposalInfoProvider implements
		IContentAssistAdditionalProposalInfoProvider<PropertyInfo> {

	public static final IContentAssistAdditionalProposalInfoProvider<PropertyInfo> INSTANCE = new PropertyContentAssistAdditionalProposalInfoProvider();

	public String getDisplayText(String displayText, PropertyInfo node) {
		String fileName = node.getPropertiesFile().getName();
		return displayText + " - [" + fileName + "]";
	}

	public Image getImage(PropertyInfo node) {
		return ImageResource.getImage(ImageResource.IMG_PROPERTY_OBJ);
	}

	public String getTextInfo(PropertyInfo node) {
		StringBuilder buf = new StringBuilder();
		buf.append("<b>Property name:</b> ");
		buf.append(node.getKey());
		buf.append("<br><b>Property value:</b> ");
		buf.append(node.getName());
		IStorage propertiesFiles = node.getPropertiesFile();
		buf.append("<br><b>File name:</b> ");
		buf.append(propertiesFiles.getName().toString());
		buf.append("<br><b>Location:</b> ");
		buf.append(propertiesFiles.getFullPath().toString());
		return buf.toString();
	}

}
