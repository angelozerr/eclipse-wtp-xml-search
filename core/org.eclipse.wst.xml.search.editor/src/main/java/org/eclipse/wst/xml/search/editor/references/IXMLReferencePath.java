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
package org.eclipse.wst.xml.search.editor.references;

import java.util.List;

import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.w3c.dom.Node;

public interface IXMLReferencePath extends IXMLReferenceTo {

	String getPath();

	boolean match(Node node);

	String[] getTargetNodes();

//	String getQuery(String value, IStringQueryBuilder builder);

	String getQuery(Object selectedNode, String mathingString,
			IStringQueryBuilder builder);
	
	String getQuery(Object selectedNode, String mathingString,
			IStringQueryBuilder builder, boolean reversed);

	String getKeyPath();

	List<String> getWildcardValues(Object selectedNode);
	
	IXMLReferenceFilter getFilter();
	
	boolean hasWildCard();
	
	Namespaces getNamespaces(); 

}
