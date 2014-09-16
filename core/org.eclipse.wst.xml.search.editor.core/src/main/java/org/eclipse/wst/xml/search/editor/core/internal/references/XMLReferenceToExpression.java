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
package org.eclipse.wst.xml.search.editor.core.internal.references;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.IXMLExpressionParser;

public class XMLReferenceToExpression extends XMLReference implements
		IXMLReferenceToExpression {

	private final IXMLExpressionParser parser;
	private final Map<String, List<IXMLReferenceTo>> tokens;

	public XMLReferenceToExpression(String referenceId, String fromPath, String fromTargetNodes,
			Namespaces namespaces, String querySpecificationId,
			String[] contentTypeIds, IXMLReferenceFilter filter,
			IXMLReferenceValidator validator, IXMLExpressionParser parser) {
		super(referenceId, fromPath, fromTargetNodes, namespaces, querySpecificationId,
				contentTypeIds, filter, validator);
		this.parser = parser;
		this.tokens = new HashMap<String, List<IXMLReferenceTo>>();
	}

	public ToType getType() {
		return ToType.EXPRESSION;
	}

	public String getReferenceToId() {
		return null;
	}

	public IXMLReference getOwnerReference() {
		return this;
	}

	public String getQuerySpecificationId() {
		return null;
	}

	public IXMLExpressionParser getParser() {
		return parser;
	}

	@Override
	public IXMLReference addTo(IXMLReferenceTo to) {
		String tokenId = to.getTokenId();
		if (!StringUtils.isEmpty(tokenId)) {
			List<IXMLReferenceTo> tos = tokens.get(tokenId);
			if (tos == null) {
				tos = new ArrayList<IXMLReferenceTo>();
				tokens.put(tokenId, tos);
			}
			tos.add(to);
		}
		return super.addTo(to);
	}

	public List<IXMLReferenceTo> getTo(String tokenId) {
		List<IXMLReferenceTo> tos = tokens.get(tokenId);
		if (tos != null) {
			return tos;
		}
		return Collections.emptyList();
	}

	public String getTokenId() {
		return null;
	}

	@Override
	public boolean isExpression() {
		return true;
	}
}
