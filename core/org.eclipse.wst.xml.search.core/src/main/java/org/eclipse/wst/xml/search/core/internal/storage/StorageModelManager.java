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
package org.eclipse.wst.xml.search.core.internal.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.search.core.internal.Trace;
import org.eclipse.wst.xml.search.core.storage.IStorageLocationProvider;
import org.eclipse.wst.xml.search.core.storage.IStorageModelManager;

/**
 * 
 * {@link IStorageModelManager} implementation.
 * 
 */
public class StorageModelManager implements IStorageModelManager {

	public static final IStorageModelManager INSTANCE = new StorageModelManager();

	private Map<IStorage, ModelInfo> modelInfoMap = new HashMap<IStorage, ModelInfo>();

	private IStorageLocationProvider locationProvider = null;

	/**
	 * Collection of info that goes with a model.
	 */
	private class ModelInfo {

		public final IStructuredModel structuredModel;
		public final IStorage storage;

		public ModelInfo(IStructuredModel structuredModel, IStorage storage) {
			this.structuredModel = structuredModel;
			this.storage = storage;
		}
	}

	public IStructuredModel getModel(IStorage storage) {
		ModelInfo info = getModelInfoFor(storage);
		if (info != null) {
			return info.structuredModel;
		}
		IStructuredModel model = loadModel(storage);
		if (model != null) {
			modelInfoMap.put(storage, new ModelInfo(model, storage));
		}
		return model;
	}

	private IStructuredModel loadModel(IStorage storage) {
		String id = calculateID(storage);
		if (id == null) {
			return null;
		}
		InputStream contents = null;
		IStructuredModel model = null;
		try {
			contents = storage.getContents();
			// first parameter must be unique
			model = StructuredModelManager.getModelManager().getModelForRead(
					id, contents, null);
			model.setBaseLocation(calculateBaseLocation(storage));
		} catch (Exception e) {
			// if (logExceptions)
			// Logger.logException(NLS.bind(SSEUIMessages._32concat_EXC_, new
			// Object[]{input}), e);
		} finally {
			if (contents != null) {
				try {
					contents.close();
				} catch (IOException e) {
					// nothing
				} catch (Exception e) {
					Trace.trace(Trace.SEVERE, "StorageModelManager#loadModel",
							e);
				}
			}
		}
		return model;

	}

	String calculateBaseLocation(IStorage storage) {
		String location = null;
		if (locationProvider != null) {
			location = locationProvider.getLocation(storage);
		}
		if (location != null) {
			return location;
		}
		try {
			if (storage != null) {
				IPath storagePath = storage.getFullPath();
				String name = storage.getName();
				if (storagePath != null) {
					// If they are different, the IStorage contract is not
					// being honored
					// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=73098).
					// Favor the name.
					if (!storagePath.lastSegment().equals(name)) {
						IPath workingPath = storagePath.addTrailingSeparator();
						location = workingPath.append(name).toString();
					} else {
						location = storagePath.makeAbsolute().toString();
					}
				}
				if (location == null)
					location = name;
			}
		} catch (Throwable e) {
			Trace.trace(Trace.SEVERE,
					"StorageModelManager#calculateBaseLocation", e);
		} finally {
			if (location == null)
				location = storage.getName();
		}
		return location;
	}

	String calculateID(IStorage storage) {
		/**
		 * Typically CVS will return a path of "filename.ext" and the input's
		 * name will be "filename.ext version". The path must be used to load
		 * the model so that the suffix will be available to compute the
		 * contentType properly. The editor input name can then be set as the
		 * base location for display on the editor title bar.
		 * 
		 */
		String path = null;
		try {
			if (storage != null) {
				IPath storagePath = storage.getFullPath();
				String name = storage.getName();
				if (storagePath != null) {
					// If they are different, the IStorage contract is not
					// being honored
					// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=73098).
					// Favor the name.
					if (!storagePath.lastSegment().equals(name)) {
						IPath workingPath = storagePath.addTrailingSeparator();
						path = workingPath.append(name).toString();
					} else {
						path = storagePath.makeAbsolute().toString();
					}
				}
				if (path == null)
					path = name;
			}
		} catch (Throwable e) {
			Trace.trace(Trace.SEVERE, "StorageModelManager#calculateID", e);
		} finally {
			if (path == null)
				path = ""; //$NON-NLS-1$
		}
		/*
		 * Prepend the hash to the path value so that we have a 1:1:1 match
		 * between editor inputs, element info, and models. The editor manager
		 * should help prevent needlessly duplicated models as long as two
		 * editor inputs from the same storage indicate they're equals().
		 */
		path = storage.hashCode() + "#" + path; //$NON-NLS-1$
		return path;
	}

	public IStorage getStorage(IStructuredModel model) {
		ModelInfo info = getModelInfoFor(model);
		if (info != null) {
			return info.storage;
		}
		return null;
	}

	private ModelInfo getModelInfoFor(IStorage storage) {
		ModelInfo result = (ModelInfo) modelInfoMap.get(storage);
		return result;
	}

	private ModelInfo getModelInfoFor(IStructuredModel structuredModel) {
		ModelInfo result = null;
		if (structuredModel != null) {
			ModelInfo[] modelInfos = (ModelInfo[]) modelInfoMap.values()
					.toArray(new ModelInfo[0]);
			for (int i = 0; i < modelInfos.length; i++) {
				ModelInfo info = modelInfos[i];
				if (structuredModel.equals(info.structuredModel)) {
					result = info;
					break;
				}
			}
		}
		return result;
	}

	public void setStorageLocationProvider(
			IStorageLocationProvider locationProvider) {
		this.locationProvider = locationProvider;
	}
}
