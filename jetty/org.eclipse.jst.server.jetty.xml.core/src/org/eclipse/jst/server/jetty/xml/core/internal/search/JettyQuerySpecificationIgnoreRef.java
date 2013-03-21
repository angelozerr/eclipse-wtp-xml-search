package org.eclipse.jst.server.jetty.xml.core.internal.search;

import org.eclipse.wst.xml.search.core.queryspecifications.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.IStringQueryBuilderProvider;

public class JettyQuerySpecificationIgnoreRef extends JettyQuerySpecification
		implements IStringQueryBuilderProvider {

	public IStringQueryBuilder getEqualsStringQueryBuilder() {
		return EqualsStringQueryBuilderIgnoreRef.INSTANCE;
	}

	public IStringQueryBuilder getStartsWithStringQueryBuilder() {
		return StartsWithStringQueryBuilderIgnoreRef.INSTANCE;
	}

}
