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
package org.eclipse.wst.xml.search.core.queryspecifications;

import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilderProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchVisitorProvider;

/**
 * XML Query specification contains the all informations to execute search DOM Nodes from
 * DOM Document coming from workspace or JAR.
 * 
 */
public interface IXMLQuerySpecification extends IResourceProvider,
		IMultiResourceProvider, IXMLSearchRequestorProvider,
		IXMLSearchVisitorProvider, IStringQueryBuilderProvider,
		IXPathProcessorIdProvider, IStorageProvider, IMultiStorageProvider {

	/**
	 * Returns true if DOM Document to search is done with multiple resources
	 * (several IFile, IContainer....) and false otherwise (one IFile, one
	 * IContainer...).
	 * 
	 * @return
	 */
	boolean isMultiResource();

	/**
	 * Returns true if DOM Document to search is done with a simple storage (XML
	 * file coming from JAR) and false otherwise.
	 * 
	 * @return
	 */
	boolean isSimpleStorage();

	/**
	 * Returns true if DOM Document to search is done with several storages (XML
	 * files coming from JAR) and false otherwise.
	 * 
	 * @return
	 */
	boolean isMultiStorage();

}
