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
package org.eclipse.wst.xml.search.core.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

/**
 * 
 * Utilities for {@link IFile}.
 * 
 */
public class FileUtils {

	/**
	 * Returns true if file name is associated with XML content type
	 * "org.eclipse.core.runtime.xml" and false otherwise.
	 * 
	 * @param filename
	 *            file name to test.
	 * @return true if file name is associated with XML content type and false
	 *         otherwise.
	 */
	public static boolean isXMLFile(String fileName) {
		IContentType contentType = Platform.getContentTypeManager()
				.getContentType(ContentTypeIdForXML.ContentTypeID_XML);
		return contentType.isAssociatedWith(fileName);
	}

	/**
	 * Returns true if file is associated with XML content type
	 * "org.eclipse.core.runtime.xml" and false otherwise.
	 * 
	 * @param file
	 *            file to test.
	 * @return true if file is associated with XML content type and false
	 *         otherwise.
	 */
	public static boolean isXMLFile(IFile file) {
		return isXMLFile(file.getName());
	}

	public static String getContentTypeId(IFile file) {
		try {
			IContentDescription contentDescription = file
					.getContentDescription();
			if (contentDescription == null) {
				return null;
			}
			IContentType contentType = contentDescription.getContentType();
			if (contentType == null) {
				return null;
			}
			return contentType.getId();

		} catch (CoreException e) {
		}
		return null;
	}
}
