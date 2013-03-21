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
package org.eclipse.wst.xml.search.core.storage;

import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public interface IStorageModelManager {

	IStructuredModel getModel(IStorage storage);
	
	IStructuredModel loadModel(IStorage storage);
	
	IStorage getStorage(IStructuredModel model);
	
	void setStorageLocationProvider(IStorageLocationProvider locationProvider);
}
