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
package org.eclipse.wst.xml.search.editor.internal.jdt;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.wst.xml.search.core.storage.IStorageLocationProvider;

public class JDTStorageLocationProvider implements IStorageLocationProvider {

	public String getLocation(IStorage storage) {
		if (storage instanceof IJarEntryResource) {
			StringBuilder location = new StringBuilder("/");
			location.append(((IJarEntryResource) storage)
					.getPackageFragmentRoot().getPath());
			location.append("/");
			location.append(storage.getName());
			return location.toString();
		}
		return null;
	}

}
