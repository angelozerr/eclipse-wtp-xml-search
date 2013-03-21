package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.wst.xml.search.core.queryspecifications.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.StartsWithStringQueryBuilder;

public class StartsWithStringQueryBuilderIgnoreRef extends StartsWithStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new StartsWithStringQueryBuilderIgnoreRef();
	
	public String getId() {
		return StartsWithStringQueryBuilderIgnoreRef.class.getSimpleName();
	}
	
	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex) {		
		super.build(xpath, targetNodes, startIndex);
		xpath.append("[name() != \"Ref\"]");
	}
}
