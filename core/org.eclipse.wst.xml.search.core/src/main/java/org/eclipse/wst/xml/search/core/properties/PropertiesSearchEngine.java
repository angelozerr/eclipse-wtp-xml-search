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
package org.eclipse.wst.xml.search.core.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.core.internal.Trace;

/**
 * {@link IPropertiesSearchEngine} implementation.
 * 
 */
public class PropertiesSearchEngine implements IPropertiesSearchEngine {

	private static final IPropertiesSearchEngine INSTANCE = new PropertiesSearchEngine();

	public static IPropertiesSearchEngine getDefault() {
		return INSTANCE;
	}

	public IStatus search(Object selectedNode, IResource[] containers,
			IPropertiesRequestor requestor, IPropertiesCollector collector,
			String matching, boolean fullMatch, IProgressMonitor monitor) {
		for (int i = 0; i < containers.length; i++) {
			IResource resource = containers[i];
			internalSearch(selectedNode, resource, containers[i], requestor,
					collector, matching, fullMatch, monitor);
		}
		return Status.OK_STATUS;
	}

	public IStatus search(Object selectedNode, IResource container,
			IPropertiesRequestor requestor, IPropertiesCollector collector,
			String matching, boolean fullMatch, IProgressMonitor monitor) {
		internalSearch(selectedNode, container, container, requestor,
				collector, matching, fullMatch, monitor);
		return Status.OK_STATUS;
	}

	private void internalSearch(Object selectedNode, IResource rootContainer,
			IResource container, IPropertiesRequestor requestor,
			IPropertiesCollector collector, String matching, boolean fullMatch,
			IProgressMonitor monitor) {
		if (!requestor.accept(container, rootContainer))
			return;
		int resourceType = container.getType();
		switch (resourceType) {
		case IResource.FILE:
			IFile file = (IFile) container;
			processFile(file, collector, matching, fullMatch);
			break;
		case IResource.ROOT:
		case IResource.PROJECT:
		case IResource.FOLDER:
			try {

				// if (!fullMatch) {
				// if (resolver
				// .resolve(selectedNode, rootContainer, container)
				// .toUpperCase().startsWith(matching.toUpperCase())) {
				// collector.add(container, rootContainer, resolver);
				// }
				// } else {
				// if (resolver
				// .resolve(selectedNode, rootContainer, container)
				// .equals(matching)) {
				// collector.add(container, rootContainer, resolver);
				// }
				// }

				internalSearch(selectedNode, rootContainer,
						((IContainer) container).members(), requestor,
						collector, matching, fullMatch, monitor);
			} catch (CoreException e) {
				Trace.trace(Trace.SEVERE, e.getMessage(), e);
			}
			break;
		}
	}

	private IStatus internalSearch(Object selectedNode,
			IResource rootContainer, IResource[] containers,
			IPropertiesRequestor requestor, IPropertiesCollector collector,
			String matching, boolean fullMatch, IProgressMonitor monitor) {
		for (int i = 0; i < containers.length; i++) {
			internalSearch(selectedNode, rootContainer, containers[i],
					requestor, collector, matching, fullMatch, monitor);
		}
		return Status.OK_STATUS;
	}

	private void processFile(IFile file, IPropertiesCollector collector,
			String matching, boolean fullMatch) {
		InputStream contents = null;

		try {
			Properties properties = new Properties();

			contents = file.getContents();

			properties.load(contents);

			if (fullMatch) {
				if (properties.containsKey(matching)) {
					String value = properties.getProperty(matching);
					collector.add(file, matching, value);
				}
			} else {
				String matchingUpperCased = matching.toUpperCase();
				String key = null;
				String value = null;
				Set<Entry<Object, Object>> entries = properties.entrySet();
				for (Entry<Object, Object> entry : entries) {
					key = (String) entry.getKey();
					value = (String) entry.getValue();
					if (key.toUpperCase().startsWith(matchingUpperCased)) {
						collector.add(file, key, value);
					}
				}
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(contents != null) {
				try {
					contents.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
