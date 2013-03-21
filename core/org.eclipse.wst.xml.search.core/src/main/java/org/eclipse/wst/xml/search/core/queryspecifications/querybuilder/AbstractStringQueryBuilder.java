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

import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.w3c.dom.Node;

/**
 * Abstract class for {@link IStringQueryBuilder}.
 * 
 */
public abstract class AbstractStringQueryBuilder implements IStringQueryBuilder {

	private static final String NORMALIZE_SPACE_TEXT = "normalize-space(text())";
	private static final String TEXT_TARGETNODE = "text()";

	public String getNodeValue(Node node) {
		return DOMUtils.getNodeValue(node);
	}

	public String build(String baseQuery, String[] targetNodes,
			Object selectedNode) {
		return build(baseQuery, targetNodes, 0, selectedNode);
	}

	public String build(String baseQuery, String[] targetNodes, int startIndex,
			Object selectedNode) {
		if (targetNodes == null) {
			targetNodes = StringUtils.EMPTY_ARRAY;
		}
		baseQuery = baseQuery.replaceAll("'", "''");
		StringBuilder xpath = new StringBuilder(baseQuery);
		if (baseQuery.endsWith("//")) {
			xpath.append("*");
		}
		build(xpath, targetNodes, startIndex, selectedNode);
		if (targetNodes.length > 0 && addLastTargetNodeAtEnds()) {
			xpath.append("/");
			xpath.append(targetNodes[targetNodes.length - 1]);
		}
		return xpath.toString();
	}

	protected String getTargetNode(String targetNode) {
		if (TEXT_TARGETNODE.equals(targetNode)) {
			targetNode = NORMALIZE_SPACE_TEXT;
		}
		return targetNode;
	}

	protected boolean addLastTargetNodeAtEnds() {
		return true;
	}

	protected abstract void build(StringBuilder xpath, String[] targetNodes,
			int startIndex, Object selectedNode);
}
