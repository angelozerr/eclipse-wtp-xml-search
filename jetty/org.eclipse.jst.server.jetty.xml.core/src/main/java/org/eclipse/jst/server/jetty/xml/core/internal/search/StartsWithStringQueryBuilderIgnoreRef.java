package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.StartsWithStringQueryBuilder;
import org.w3c.dom.Node;

public class StartsWithStringQueryBuilderIgnoreRef extends
		StartsWithStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new StartsWithStringQueryBuilderIgnoreRef();

	public String getId() {
		return StartsWithStringQueryBuilderIgnoreRef.class.getSimpleName();
	}

	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex, Object selectedNode) {
		super.build(xpath, targetNodes, startIndex, selectedNode);
		xpath.append("[name() != \"Ref\"]");
	}
}
