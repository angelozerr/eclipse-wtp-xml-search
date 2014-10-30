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
package org.eclipse.wst.xml.search.core.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.xml.search.core.queryspecifications.IExecutableXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;

/**
 * Implementation of {@link IResourceProxyVisitor} to loop for list of
 * {@link IResource} registered in the {@link IXMLQuerySpecificationRegistry}
 * registry and visit each files to execute query.
 * 
 */
public class FilesOfScopeCalculator implements IResourceProxyVisitor {

	private final IXMLQuerySpecificationRegistry querySpecificationRegistry;
	private final MultiStatus fStatus;
	private List<IFile> fFiles;

	public FilesOfScopeCalculator(
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			MultiStatus status) {
		this.querySpecificationRegistry = querySpecificationRegistry;
		fStatus = status;
	}

	public boolean visit(IResourceProxy proxy) {
		boolean inScope = isInScope(proxy);
		if (inScope && proxy.getType() == IResource.FILE) {
			fFiles.add((IFile) proxy.requestResource());
		}
		return inScope;
	}

	public IFile[] process() {
		fFiles = new ArrayList<IFile>();
		try {
			Set<IResource> containers = querySpecificationRegistry
					.getQuerySpecificationsMap().keySet();
			for (IResource resource : containers) {
				switch (resource.getType()) {
				case IResource.FILE:
					resource.accept(this, 0);
					break;
				case IResource.ROOT:
				case IResource.PROJECT:
				case IResource.FOLDER:
					IContainer container = (IContainer) resource;
					IResource[] roots = container.members();
					for (int i = 0; i < roots.length; i++) {
						IResource r = roots[i];
						if (r.isAccessible()) {
							r.accept(this, 0);
						}
					}
					break;
				}
			}
			return (IFile[]) fFiles.toArray(new IFile[fFiles.size()]);
		} catch (CoreException ex) {
			// report and ignore
			fStatus.add(ex.getStatus());

		} finally {
			fFiles = null;
		}
		return null;
	}

	private boolean isInScope(IResourceProxy proxy) {
		Collection<Collection<IExecutableXMLQuerySpecification>> values = querySpecificationRegistry
				.getQuerySpecificationsMap().values();
		for (Collection<IExecutableXMLQuerySpecification> collection : values) {
			if (isInScope(proxy, collection)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInScope(IResourceProxy proxy,
			Collection<IExecutableXMLQuerySpecification> collection) {
		for (IExecutableXMLQuerySpecification executableXMLQuerySpecification : collection) {
			if (isInScope(proxy, executableXMLQuerySpecification)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInScope(IResourceProxy proxy,
			IExecutableXMLQuerySpecification executableXMLQuerySpecification) {
		IXMLSearchRequestor requestor = executableXMLQuerySpecification
				.getRequestor();
		return (requestor != null ? requestor.accept(proxy.requestResource(),
				null) : false);
	}
}
