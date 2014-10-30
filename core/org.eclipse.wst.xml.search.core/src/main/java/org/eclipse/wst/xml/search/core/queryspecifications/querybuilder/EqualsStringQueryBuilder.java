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
package org.eclipse.wst.xml.search.core.queryspecifications.querybuilder;

import org.w3c.dom.Node;

/**
 * String query builder to use = XPath function.
 * 
 */
public class EqualsStringQueryBuilder extends AbstractStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new EqualsStringQueryBuilder();

	protected EqualsStringQueryBuilder() {

	}

	public String getId() {
		return EqualsStringQueryBuilder.class.getSimpleName();
	}

	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex, Object selectedNode) {
		String targetNode = null;
		for (int i = 0; i < targetNodes.length; i++) {
			targetNode = targetNodes[i];
			targetNode = getTargetNode(targetNode);
			xpath.append("[");
			xpath.append(targetNode);
			xpath.append("=\"{");
			xpath.append(i + startIndex);
			xpath.append("}\"]");
		}
	}

	@Override
	protected boolean addLastTargetNodeAtEnds() {
		return true;
	}

}
