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
package org.eclipse.wst.xml.search.editor.searchers.properties;

import org.eclipse.core.resources.IStorage;

public class PropertyInfo {

	private final IStorage propertiesFile;
	private final String key;
	private final String name;

	public PropertyInfo(IStorage propertiesFile, String key, String name) {
		this.propertiesFile = propertiesFile;
		this.key = key;
		this.name = name;
	}

	public IStorage getPropertiesFile() {
		return propertiesFile;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

}
