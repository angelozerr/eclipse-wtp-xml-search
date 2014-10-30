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
package org.eclipse.wst.xml.search.core.storage;

import org.eclipse.wst.xml.search.core.internal.storage.StorageModelManager;

final public class StructuredStorageModelManager {

	/**
	 * Do not allow instances to be created.
	 */
	private StructuredStorageModelManager() {
		super();
	}

	/**
	 * Provides access to the instance of {@link IStorageModelManager}.
	 * 
	 * @return {@link IStorageModelManager} - returns the one model manager for
	 *         structured model
	 */
	public static IStorageModelManager getModelManager() {
		return StorageModelManager.INSTANCE;
	}
}
