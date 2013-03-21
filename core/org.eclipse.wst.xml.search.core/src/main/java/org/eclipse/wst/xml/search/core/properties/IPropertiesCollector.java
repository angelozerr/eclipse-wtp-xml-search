/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.core.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;

/**
 * A collector to collect {@link IResource} found with the search.
 * 
 */
public interface IPropertiesCollector {

	/**
	 * Collect the storage.
	 * 
	 * @param storage
	 * @param key
	 * @param name
	 * @return
	 */
	boolean add(IStorage storage, String key, String name);
}
