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

import org.eclipse.wst.xml.search.core.namespaces.Namespaces;

/**
 * Executable XML query specification is {@link IXMLQuerySpecification} which
 * can be executed.
 * 
 */
public interface IExecutableXMLQuerySpecification extends
		IXMLQuerySpecification {

	/**
	 * Returns the query (XPath...) to execute.
	 * 
	 * @return
	 */
	String getQuery();

	/**
	 * Returns information about namespaces which must be used when XPath is
	 * executed.
	 * 
	 * @return
	 */
	Namespaces getNamespaces();

	/**
	 * Returns the selected node which has started the search.
	 * 
	 * @return
	 */
	Object getSelectedNode();
}
