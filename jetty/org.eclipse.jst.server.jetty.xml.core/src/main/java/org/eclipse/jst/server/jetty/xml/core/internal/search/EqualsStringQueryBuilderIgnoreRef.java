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
package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.w3c.dom.Node;

public class EqualsStringQueryBuilderIgnoreRef extends EqualsStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new EqualsStringQueryBuilderIgnoreRef();
	
	public String getId() {
		return EqualsStringQueryBuilderIgnoreRef.class.getSimpleName();
	}
	
	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex, Object selectedNode) {		
		super.build(xpath, targetNodes, startIndex, selectedNode);
		xpath.append("[name() != \"Ref\"]");
	}
}
