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

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;

/**
 * Registry of query {@link IExecutableXMLQuerySpecification} to execute.
 * 
 */
public interface IXMLQuerySpecificationRegistry {

	void register(IXMLQuerySpecification querySpecification, String query,
			Namespaces namespaceInfos);

	void register(IXMLQuerySpecification xmlQuerySpecification, String query,
			IResource container, Namespaces namespaceInfos);

	Map<IResource, Collection<IExecutableXMLQuerySpecification>> getQuerySpecificationsMap();

	Object getSelectedNode();

	Collection<String> getQueries();

	String getQueriesLabel();

}
