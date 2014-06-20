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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.namespaces.NamespacesManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.contentassist.DefaultDOMContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.PropertyContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.ResourceContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.internal.contentassist.ContentAssistsManager;
import org.eclipse.wst.xml.search.editor.internal.references.filters.XMLReferenceFiltersManager;
import org.eclipse.wst.xml.search.editor.internal.searchers.java.JavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.internal.searchers.java.JavaQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.internal.searchers.javamethod.JavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.internal.searchers.javamethod.JavaMethodQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.references.IReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.XMLSearcherManager;
import org.eclipse.wst.xml.search.editor.searchers.expressions.IXMLExpressionParser;
import org.eclipse.wst.xml.search.editor.searchers.java.DefaultExtendedClassProvider;
import org.eclipse.wst.xml.search.editor.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.IJavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.searchers.properties.PropertyInfo;
import org.w3c.dom.Node;

public class XMLReferencePathFactory {

	private static final String ADDITIONAL_PROPOSAL_INFO_PROVIDER_ID_ATTR = "additionalProposalInfoProviderId";
	// Default searcher ID
	private static final String DEFAULT_SEARCHER_FOR_STATICS = "org.eclipse.wst.xml.search.editor.searcher.statics";
	private static final String DEFAULT_SEARCHER_FOR_JAVAMETHOD = "org.eclipse.wst.xml.search.editor.searcher.javamethod";
	private static final String DEFAULT_SEARCHER_FOR_JAVA = "org.eclipse.wst.xml.search.editor.searcher.java";
	private static final String DEFAULT_SEARCHER_FOR_FILE = "org.eclipse.wst.xml.search.editor.searcher.resource";
	private static final String DEFAULT_SEARCHER_FOR_PROPERTIES = "org.eclipse.wst.xml.search.editor.searcher.properties";;
	private static final String DEFAULT_SEARCHER_FOR_XML = "org.eclipse.wst.xml.search.editor.searcher.xml";

	public static XMLReferencePathFactory INSTANCE = new XMLReferencePathFactory();
	public static final String CONTENT_TYPE_IDS_ATTR = "contentTypeIds";

	// Global attributes for to*
	private static final String ID_ATTR = "id";
	private static final String QUERY_SPECIFICATION_ID_ATTR = "querySpecificationId";
	private static final String TOKEN_ID_ATTR = "tokenId";
	private static final String SEARCHER_ID_ATTR = "searcherId";
	private static final String FILTER_ID_ATTR = "filterId";

	// toResource
	private static final String TO_RESOURCE_ELT = "toResource";

	// toJava
	private static final String TO_JAVA_ELT = "toJava";

	// toJavaMethod
	private static final String TO_JAVA_METHOD_ELT = "toJavaMethod";
	private static final String PATH_FOR_CLASS_ATTR = "pathForClass";

	// toProperty
	private static final String TO_PROPERTY_ELT = "toProperty";

	// toStatic
	private static final String TO_STATIC_ELT = "toStatic";

	// to
	private static final String TO_ELT = "to";
	private static final String PATH_ATTR = "path";
	private static final String TARGET_NODES_ATTR = "targetNodes";
	private static final String NAMESPACE_ID_ATTR = "namespacesId";

	public IXMLReference createReference(IConfigurationElement element,
			String[] defaultContentTypeIds, IXMLReferenceValidator validator,
			IXMLExpressionParser parser, IXMLSearcher expressionSearcher) {
		String[] contentTypeIds = defaultContentTypeIds;
		String ids = element.getAttribute(CONTENT_TYPE_IDS_ATTR);
		if (!StringUtils.isEmpty(ids)) {
			contentTypeIds = ids.split(",");
		}
		String fromPath = element.getAttribute(PATH_ATTR);
		String fromTargetNodes = element.getAttribute(TARGET_NODES_ATTR);
		String fromQuerySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		IXMLReferenceFilter filter = getFilter(element);
		Namespaces namespaces = getNamespaces(element);
		if (parser != null) {
			return XMLReferencesUtil.createXMLReferenceToExpression(fromPath,
					fromTargetNodes, namespaces, fromQuerySpecificationId,
					contentTypeIds, filter, validator, parser,
					expressionSearcher);
		}
		return XMLReferencesUtil.createXMLReference(fromPath, fromTargetNodes,
				namespaces, fromQuerySpecificationId, contentTypeIds, filter,
				validator);
	}

	public IXMLReferenceTo createTo(IConfigurationElement element,
			IReference reference) {
		if (reference == null) {
			return null;
		}
		String id = element.getAttribute(ID_ATTR);
		String toQuerySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		String tokenId = element.getAttribute(TOKEN_ID_ATTR);
		if (TO_ELT.equals(element.getName())) {
			// XML reference path
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_XML);
			if (searcher == null) {
				return null;
			}
			Namespaces namespaces = getNamespaces(element);

			IContentAssistAdditionalProposalInfoProvider<Node> additionalProposalInfoProvider = getAdditionalProposalInfoProvider(element);
			if (additionalProposalInfoProvider == null) {
				additionalProposalInfoProvider = DefaultDOMContentAssistAdditionalProposalInfoProvider.INSTANCE;
			}
			String toPath = element.getAttribute(PATH_ATTR);
			String toTargetNodes = element.getAttribute(TARGET_NODES_ATTR);
			return reference.createToXML(id, searcher, toPath, toTargetNodes,
					namespaces, toQuerySpecificationId, tokenId, null,
					additionalProposalInfoProvider);
		} else if (TO_PROPERTY_ELT.equals(element.getName())) {
			// Resource (file, folder...) reference
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_PROPERTIES);
			if (searcher == null) {
				return null;
			}
			IContentAssistAdditionalProposalInfoProvider<PropertyInfo> additionalProposalInfoProvider = getAdditionalProposalInfoProvider(element);
			if (additionalProposalInfoProvider == null) {
				additionalProposalInfoProvider = PropertyContentAssistAdditionalProposalInfoProvider.INSTANCE;
			}
			return reference.createToProperty(id, searcher,
					toQuerySpecificationId, tokenId,
					additionalProposalInfoProvider);
		} else if (TO_RESOURCE_ELT.equals(element.getName())) {
			// Resource (file, folder...) reference
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_FILE);
			if (searcher == null) {
				return null;
			}
			IContentAssistAdditionalProposalInfoProvider<IResource> additionalProposalInfoProvider = getAdditionalProposalInfoProvider(element);
			if (additionalProposalInfoProvider == null) {
				additionalProposalInfoProvider = ResourceContentAssistAdditionalProposalInfoProvider.INSTANCE;
			}
			return reference.createToResource(id, searcher,
					toQuerySpecificationId, tokenId,
					additionalProposalInfoProvider);
		} else if (TO_JAVA_ELT.equals(element.getName())) {
			// Java reference
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_JAVA);
			if (searcher == null) {
				return null;
			}
			IJavaQuerySpecification querySpecification = getJavaQuerySpecification(element);
			return reference.createToJava(id, searcher, toQuerySpecificationId,
					tokenId, querySpecification);
		} else if (TO_JAVA_METHOD_ELT.equals(element.getName())) {
			// Java method reference
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_JAVAMETHOD);
			if (searcher == null) {
				return null;
			}
			IJavaMethodQuerySpecification querySpecification = getJavaMethodQuerySpecification(element);
			String pathForClass = element.getAttribute(PATH_FOR_CLASS_ATTR);
			return reference.createToJavaMethod(id, searcher,
					toQuerySpecificationId, tokenId, pathForClass,
					querySpecification);
		} else if (TO_STATIC_ELT.equals(element.getName())) {
			// static reference
			IXMLSearcher searcher = getSearcher(element,
					DEFAULT_SEARCHER_FOR_STATICS);
			if (searcher == null) {
				return null;
			}
			IContentAssistAdditionalProposalInfoProvider<?> additionalProposalInfoProvider = getAdditionalProposalInfoProvider(element);
			return reference.createToStatic(id, searcher,
					toQuerySpecificationId, tokenId,
					additionalProposalInfoProvider);
		}
		return null;
	}

	private Namespaces getNamespaces(IConfigurationElement element) {
		String namespaceId = element.getAttribute(NAMESPACE_ID_ATTR);
		if (StringUtils.isEmpty(namespaceId)) {
			return null;
		}
		return NamespacesManager.getInstance().getNamespaces(namespaceId);
	}

	private IXMLSearcher getSearcher(IConfigurationElement element,
			String defaultSearcherId) {
		String searcherId = element.getAttribute(SEARCHER_ID_ATTR);
		if (searcherId == null) {
			searcherId = defaultSearcherId;
		}
		IXMLSearcher searcher = XMLSearcherManager.getDefault().getSearcher(
				searcherId);
		if (searcher == null) {
			return null;
		}
		return searcher;
	}

	private <T> IContentAssistAdditionalProposalInfoProvider<T> getAdditionalProposalInfoProvider(
			IConfigurationElement element) {
		String additionalProposalInfoProviderId = element
				.getAttribute(ADDITIONAL_PROPOSAL_INFO_PROVIDER_ID_ATTR);
		if (StringUtils.isEmpty(additionalProposalInfoProviderId)) {
			return null;
		}
		return ContentAssistsManager.getDefault().getProvider(
				additionalProposalInfoProviderId);
	}

	private IJavaMethodQuerySpecification getJavaMethodQuerySpecification(
			IConfigurationElement element) {
		String querySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		if (StringUtils.isEmpty(querySpecificationId)) {
			return JavaMethodQuerySpecification.DEFAULT;
		}
		return JavaMethodQuerySpecificationrManager.getDefault()
				.getQuerySpecification(querySpecificationId);
	}

	private IJavaQuerySpecification getJavaQuerySpecification(
			IConfigurationElement element) {
		String querySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		if (StringUtils.isEmpty(querySpecificationId)) {
			String extendsClass = element.getAttribute("extends");
			if (!StringUtils.isEmpty(extendsClass)) {
				DefaultExtendedClassProvider implementsClassProvider = new DefaultExtendedClassProvider(
						extendsClass.replaceAll("\\s+", "").split(","));
				return new JavaQuerySpecification(implementsClassProvider);
			}
			return null;
		}
		return JavaQuerySpecificationrManager.getDefault()
				.getQuerySpecification(querySpecificationId);
	}

	private IXMLReferenceFilter getFilter(IConfigurationElement element) {
		String filterId = element.getAttribute(FILTER_ID_ATTR);
		if (StringUtils.isEmpty(filterId)) {
			return null;
		}
		return XMLReferenceFiltersManager.getDefault().getProvider(filterId);
	}

}
