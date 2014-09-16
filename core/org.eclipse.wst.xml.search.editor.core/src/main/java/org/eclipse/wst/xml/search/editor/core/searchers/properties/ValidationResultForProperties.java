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
package org.eclipse.wst.xml.search.editor.core.searchers.properties;

import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.xml.search.core.properties.IPropertiesCollector;
import org.eclipse.wst.xml.search.editor.core.validation.AbstractValidationResult;

public class ValidationResultForProperties extends AbstractValidationResult
		implements IPropertiesCollector {
	
	public boolean add(IStorage storage, String key, String name) {
		nbElements++;
		return true;
	}

}