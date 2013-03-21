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
package org.eclipse.wst.xml.search.editor.internal.references;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.core.xpath.matcher.XPathMatcher;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.references.IReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToXML;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.w3c.dom.Node;

public class XMLReferencePath extends AbstractXMLReferenceTo implements
		IXMLReferenceToXML {

	public static final int FROM_PATH_INDEX = -9999999;

	private final String path;
	private final String[] targetNodes;
	private final XPathMatcher matcher;
	private final String keyPath;
	private final IXMLReferenceFilter filter;
	private final IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider;
	private final Namespaces namespaces;

	private Map<String, String> builtPaths = new HashMap<String, String>();
	private int nbWildCard;

	public XMLReferencePath(
			IReference ownerReference,
			String id,
			IXMLSearcher searcher,
			String path,
			String[] targetNodes,
			Namespaces namespaces,
			String querySpecificationId,
			String tokenId,
			IXMLReferenceFilter filter,
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider) {
		super(ownerReference, id, searcher, querySpecificationId, tokenId);
		this.targetNodes = targetNodes;
		this.namespaces = namespaces;
		this.matcher = new XPathMatcher(path);
		this.nbWildCard = matcher.getNbWildCard();
		this.keyPath = computeKeyPath(path, targetNodes);
		this.path = computeXPathWithWildcard(path);
		this.filter = filter;
		this.additionalProposalInfoProvider = additionalProposalInfoProvider;
	}

	private String computeXPathWithWildcard(String path) {
		if (nbWildCard == -1)
			return path;
		for (int i = 0; i < nbWildCard + 1; i++) {
			path = path.replaceAll("\\$" + i + "", "{" + i + "}");
		}
		return path;
	}

	public String getPath() {
		return path;
	}

	public String[] getTargetNodes() {
		return targetNodes;
	}

	public boolean match(final Node node) {
		return matcher.match(node);
	}

	private String getXPathFromCache(IStringQueryBuilder builder,
			Object selectedNode) {
		if (builder == null) {
			builder = EqualsStringQueryBuilder.INSTANCE;
		}
		if (builder.getId() == null) {
			return createXPath(builder, selectedNode);
		}
		// boolean withWildcard = nbWildCard > 0;
		String id = builder.getId() + "_" + nbWildCard;
		String xpath = builtPaths.get(id);
		if (xpath == null) {
			xpath = createXPath(builder, selectedNode);
			builtPaths.put(id, xpath);
		}
		return xpath;
	}

	private String createXPath(IStringQueryBuilder builder, Object selectedNode) {
		String xpath;
		String p = path;// (withWildcard ? path : pathWithoutWildcard);
		int index = nbWildCard + 1;
		xpath = builder.build(p, targetNodes, index, selectedNode);
		return xpath;
	}

	public ToType getType() {
		return ToType.XML;
	}

	public String getKeyPath() {
		return keyPath;
	}

	private String computeKeyPath(String path, String[] targetNodes) {
		StringBuilder result = new StringBuilder(path);
		for (String targetNode : targetNodes) {
			result.append("_");
			result.append(targetNode);
		}
		return result.toString();
	}

	public String getQuery(Object selectedNode, String mathingString,
			IStringQueryBuilder builder) {
		return getQuery(selectedNode, mathingString, builder, true);
	}

	public String getQuery(Object selectedNode, String mathingString,
			IStringQueryBuilder builder, boolean reversed) {
		// Get Xpath with wildcard
		String xpath = getXPathFromCache(builder, selectedNode);

		// get values
		Collection<String> values = null;
		if (hasWildCard()) {
			// Get values of XPath
			IXMLReferencePath from = ((IXMLReference) getOwnerReference())
					.getFrom();
			if (!reversed) {
				List<IXMLReferenceTo> to = getOwnerReference().getTo();
				for (IXMLReferenceTo referenceTo : to) {
					if (referenceTo.getType() == ToType.XML) {
						values = ((IXMLReferenceToXML) referenceTo)
								.getWildcardValues(selectedNode);
					}
				}
			} else {
				values = from.getWildcardValues(selectedNode);
			}
		} else {
			values = new ArrayList<String>();
		}
		if (mathingString != null) {
			values.add(mathingString);
		} else {
			if (selectedNode instanceof Node) {
				values.add(DOMUtils.getNodeValue((Node) selectedNode));
			}
		}
		return XPathManager.getManager().getXPath(xpath,
				values.toArray(StringUtils.EMPTY_ARRAY));
	}

	public List<String> getWildcardValues(Object selectedNode) {
		if (selectedNode instanceof Node) {
			return matcher.getWildcardValues((Node) selectedNode);
		}
		return null;
	}

	public IContentAssistAdditionalProposalInfoProvider<?> getAdditionalProposalInfoProvider() {
		return additionalProposalInfoProvider;
	}

	public IXMLReferenceFilter getFilter() {
		return filter;
	}

	public boolean hasWildCard() {
		return nbWildCard != -1;
	}

	public Namespaces getNamespaces() {
		return namespaces;
	}
}
