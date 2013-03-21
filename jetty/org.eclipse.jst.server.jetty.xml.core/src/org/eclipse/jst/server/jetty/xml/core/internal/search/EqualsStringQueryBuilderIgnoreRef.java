package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.wst.xml.search.core.queryspecifications.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.IStringQueryBuilder;

public class EqualsStringQueryBuilderIgnoreRef extends EqualsStringQueryBuilder {

	public static IStringQueryBuilder INSTANCE = new EqualsStringQueryBuilderIgnoreRef();
	
	public String getId() {
		return EqualsStringQueryBuilderIgnoreRef.class.getSimpleName();
	}
	
	@Override
	protected void build(StringBuilder xpath, String[] targetNodes,
			int startIndex) {		
		super.build(xpath, targetNodes, startIndex);
		xpath.append("[name() != \"Ref\"]");
	}
}
