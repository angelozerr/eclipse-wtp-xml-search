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
package org.eclipse.wst.xml.search.core.queryspecifications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.internal.queryspecifications.ExecutableXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;

/**
 * Registry of query {@link IExecutableXMLQuerySpecification} to execute.
 * 
 */
public class XMLQuerySpecificationRegistry implements
		IXMLQuerySpecificationRegistry {

	private final Map<IResource, Collection<IExecutableXMLQuerySpecification>> querySpecificationsMap;
	private final Collection<String> queries;
	private final IContainer container;
	private final IFile file;
	private final Object selectedNode;

	public XMLQuerySpecificationRegistry(IResource resource, Object selectedNode) {
		if (resource.getType() == IResource.FILE) {
			this.file = (IFile) resource;
			this.container = null;
		} else {
			this.file = null;
			this.container = (IContainer) resource;
		}
		this.selectedNode = selectedNode;
		this.querySpecificationsMap = new HashMap<IResource, Collection<IExecutableXMLQuerySpecification>>();
		this.queries = new ArrayList<String>();
	}

	public void register(IXMLQuerySpecification querySpecification,
			String query, Namespaces namespaceInfos) {
		if (querySpecification.isMultiResource() && file != null) {
			IResource[] containers = querySpecification.getResources(
					selectedNode, file);
			for (IResource container : containers) {
				register(querySpecification, query, container, namespaceInfos);
			}
		} else {
			IResource container = (file != null ? querySpecification
					.getResource(selectedNode, file) : this.container);
			if (container == null)
				return;
			register(querySpecification, query, container, namespaceInfos);
		}
	}

	public void register(IXMLQuerySpecification querySpecification,
			String query, IResource container, Namespaces namespaceInfos) {
		Collection<IExecutableXMLQuerySpecification> querySpecifications = querySpecificationsMap
				.get(container);
		if (querySpecifications == null) {
			querySpecifications = new ArrayList<IExecutableXMLQuerySpecification>();
			querySpecificationsMap.put(container, querySpecifications);
		}
		if (!hasQuery(querySpecifications, query)) {
			queries.add(query);
			querySpecifications.add(new ExecutableXMLQuerySpecification(this,
					querySpecification, query, namespaceInfos));
		}
	}

	private boolean hasQuery(
			Collection<IExecutableXMLQuerySpecification> querySpecifications,
			String query) {
		for (IExecutableXMLQuerySpecification querySpecification : querySpecifications) {
			if (querySpecification.getQuery().equals(query))
				return true;
		}
		return false;
	}

	public Map<IResource, Collection<IExecutableXMLQuerySpecification>> getQuerySpecificationsMap() {
		return querySpecificationsMap;
	}

	public Object getSelectedNode() {
		return selectedNode;
	}

	public Collection<String> getQueries() {
		return queries;
	}

	public String getQueriesLabel() {
		Collection<String> cache = new ArrayList<String>();
		StringBuilder label = new StringBuilder();
		int i = 0;
		for (String query : queries) {
			if (!cache.contains(query)) {
				if (i > 0) {
					label.append(", ");
				}
				label.append(query);
				i++;
				cache.add(query);
			}
		}
		return label.toString();
	}

}
