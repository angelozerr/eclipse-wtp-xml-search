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

/**
 * Default string query builder provider.
 * 
 */
public class DefaultStringQueryBuilderProvider implements
		IStringQueryBuilderProvider {

	public static final IStringQueryBuilderProvider INSTANCE = new DefaultStringQueryBuilderProvider();

	public IStringQueryBuilder getEqualsStringQueryBuilder() {
		return EqualsStringQueryBuilder.INSTANCE;
	}

	public IStringQueryBuilder getStartsWithStringQueryBuilder() {
		return StartsWithStringQueryBuilder.INSTANCE;
	}

}
