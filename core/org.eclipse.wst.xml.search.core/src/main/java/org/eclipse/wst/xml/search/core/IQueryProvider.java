package org.eclipse.wst.xml.search.core;


import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;

public interface IQueryProvider {

	String getQuery(Object selectedNode, IStringQueryBuilder builder,
			String[] values, Namespaces namespaces, IDOMDocument document);
}
