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
package org.eclipse.wst.xml.search.core.queryspecifications.requestor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

public class AllFilesXMLSearchRequestor extends AbstractXMLSearchRequestor {

	public static final IXMLSearchRequestor INSTANCE = new AllFilesXMLSearchRequestor();

	@Override
	public boolean accept(IFile file, IResource rootResource) {
		return true;
	}

}
