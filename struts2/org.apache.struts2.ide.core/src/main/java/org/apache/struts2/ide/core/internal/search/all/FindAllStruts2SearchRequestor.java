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
package org.apache.struts2.ide.core.internal.search.all;

import org.apache.struts2.ide.core.internal.search.Struts2SearchRequestor;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;

public class FindAllStruts2SearchRequestor extends Struts2SearchRequestor {

	public static final IXMLSearchRequestor INSTANCE = new FindAllStruts2SearchRequestor();

	@Override
	public boolean accept(IFolder folder, IResource rootResource) {
		if (super.accept(folder, rootResource)) {
			// Test if folder is Java output location (rootResource)
			return (folder.equals(rootResource));
		}
		return false;
	}
}
