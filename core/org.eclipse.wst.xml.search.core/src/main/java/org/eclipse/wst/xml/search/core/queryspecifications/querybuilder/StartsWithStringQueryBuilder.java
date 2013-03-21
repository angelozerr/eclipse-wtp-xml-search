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
package org.eclipse.wst.xml.search.core.queryspecifications.querybuilder;

import org.w3c.dom.Node;

/**
 * String query builder to use fn:starts-with() XPath function.
 * 
 */
public class StartsWithStringQueryBuilder extends AbstractStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new StartsWithStringQueryBuilder();

	protected StartsWithStringQueryBuilder() {

	}

	public String getId() {
		return StartsWithStringQueryBuilder.class.getSimpleName();
	}

	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex, Object selectedNode) {
		String attrName = null;
		for (int i = 0; i < targetNodes.length; i++) {
			attrName = targetNodes[i];
			// Attribute exists
			xpath.append("[");
			xpath.append(attrName);
			xpath.append(" and ");
			// starts-with
			xpath.append("starts-with(");
			xpath.append(getTargetNode(attrName));
			xpath.append(",\"{");
			xpath.append(i + startIndex);
			xpath.append("}\")");
			xpath.append("]");
		}
	}

	

}
