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
package org.eclipse.wst.xml.search.editor.internal.jdt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeHierarchyChangedListener;
import org.eclipse.jdt.core.JavaModelException;

public class SuperTypeHierarchyCache {

	private static final int CACHE_SIZE = 50;
	private static List<HierarchyCacheEntry> HIERACHY_CACHE = new ArrayList<HierarchyCacheEntry>(
			50);

	public SuperTypeHierarchyCache() {
	}

	private static void addTypeHierarchyToCache(ITypeHierarchy hierarchy) {
		synchronized (HIERACHY_CACHE) {
			int nEntries = HIERACHY_CACHE.size();
			if (nEntries >= 50) {
				HierarchyCacheEntry oldest = null;
				List<HierarchyCacheEntry> obsoleteHierarchies = new ArrayList<HierarchyCacheEntry>(
						50);
				for (int i = 0; i < nEntries; i++) {
					HierarchyCacheEntry entry = HIERACHY_CACHE.get(i);
					ITypeHierarchy curr = entry.getTypeHierarchy();
					if (!curr.exists() || hierarchy.contains(curr.getType()))
						obsoleteHierarchies.add(entry);
					else if (oldest == null
							|| entry.getLastAccess() < oldest.getLastAccess())
						oldest = entry;
				}

				if (!obsoleteHierarchies.isEmpty()) {
					for (int i = 0; i < obsoleteHierarchies.size(); i++)
						removeHierarchyEntryFromCache((HierarchyCacheEntry) obsoleteHierarchies
								.get(i));

				} else if (oldest != null)
					removeHierarchyEntryFromCache(oldest);
			}
			HierarchyCacheEntry newEntry = new HierarchyCacheEntry(hierarchy);
			HIERACHY_CACHE.add(newEntry);
		}
	}

	private static ITypeHierarchy findTypeHierarchyInCache(IType type) {
		synchronized (HIERACHY_CACHE) {
			for (int i = HIERACHY_CACHE.size() - 1; i >= 0; i--) {
				HierarchyCacheEntry curr = (HierarchyCacheEntry) HIERACHY_CACHE
						.get(i);
				ITypeHierarchy hierarchy = curr.getTypeHierarchy();
				if (!hierarchy.exists()) {
					removeHierarchyEntryFromCache(curr);
				} else {
					if (hierarchy.contains(type)) {
						curr.markAsAccessed();
						return hierarchy;
					}
				}
			}
		}
		return null;
	}

	public static ITypeHierarchy getTypeHierarchy(IType type)
			throws JavaModelException {
		return getTypeHierarchy(type, null);
	}

	public static ITypeHierarchy getTypeHierarchy(IType type,
			IProgressMonitor progressMonitor) throws JavaModelException {
		ITypeHierarchy hierarchy = findTypeHierarchyInCache(type);
		if (hierarchy == null) {
			hierarchy = type.newTypeHierarchy(progressMonitor);
			addTypeHierarchyToCache(hierarchy);
		}
		return hierarchy;
	}

	public static boolean hasInCache(IType type) {
		return findTypeHierarchyInCache(type) != null;
	}

	private static void removeHierarchyEntryFromCache(HierarchyCacheEntry entry) {
		synchronized (HIERACHY_CACHE) {
			entry.dispose();
			HIERACHY_CACHE.remove(entry);
		}
	}

	private static class HierarchyCacheEntry implements
			ITypeHierarchyChangedListener {

		public void dispose() {
			typeHierarchy.removeTypeHierarchyChangedListener(this);
			typeHierarchy = null;
		}

		public long getLastAccess() {
			return lastAccess;
		}

		public ITypeHierarchy getTypeHierarchy() {
			return typeHierarchy;
		}

		public void markAsAccessed() {
			lastAccess = System.currentTimeMillis();
		}

		public void typeHierarchyChanged(ITypeHierarchy typeHierarchy) {
			SuperTypeHierarchyCache.removeHierarchyEntryFromCache(this);
		}

		private long lastAccess;
		private ITypeHierarchy typeHierarchy;

		public HierarchyCacheEntry(ITypeHierarchy hierarchy) {
			typeHierarchy = hierarchy;
			typeHierarchy.addTypeHierarchyChangedListener(this);
			markAsAccessed();
		}
	}

}
