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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.namespaces.NamespacesManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.internal.references.filters.XMLReferenceFiltersManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.java.JavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.java.JavaQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.javamethod.JavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.javamethod.JavaMethodQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.core.references.IReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.IXMLExpressionParser;
import org.eclipse.wst.xml.search.editor.core.searchers.java.DefaultExtendedClassProvider;
import org.eclipse.wst.xml.search.editor.core.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;

public class XMLReferencePathFactory {

	private static final String ADDITIONAL_PROPOSAL_INFO_PROVIDER_ID_ATTR = "additionalProposalInfoProviderId";

	public static XMLReferencePathFactory INSTANCE = new XMLReferencePathFactory();
	public static final String CONTENT_TYPE_IDS_ATTR = "contentTypeIds";

	// Global attributes for to*
	private static final String REFERENCE_ID_ATTR = "id";
	private static final String REFERENCE_TO_ID_ATTR = "id";
	private static final String QUERY_SPECIFICATION_ID_ATTR = "querySpecificationId";
	private static final String TOKEN_ID_ATTR = "tokenId";
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
			IXMLExpressionParser parser) {
		String[] contentTypeIds = defaultContentTypeIds;
		String ids = element.getAttribute(CONTENT_TYPE_IDS_ATTR);
		if (!StringUtils.isEmpty(ids)) {
			contentTypeIds = ids.split(",");
		}
		String referenceId = element.getAttribute(REFERENCE_ID_ATTR);
		String fromPath = element.getAttribute(PATH_ATTR);
		String fromTargetNodes = element.getAttribute(TARGET_NODES_ATTR);
		String fromQuerySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		IXMLReferenceFilter filter = getFilter(element);
		Namespaces namespaces = getNamespaces(element);
		if (parser != null) {
			return XMLReferencesUtil.createXMLReferenceToExpression(referenceId, fromPath,
					fromTargetNodes, namespaces, fromQuerySpecificationId,
					contentTypeIds, filter, validator, parser);
		}
		return XMLReferencesUtil.createXMLReference(referenceId, fromPath, fromTargetNodes,
				namespaces, fromQuerySpecificationId, contentTypeIds, filter,
				validator);
	}

	public IXMLReferenceTo createTo(IConfigurationElement element,
			IReference reference) {
		if (reference == null) {
			return null;
		}
		String referenceToId = element.getAttribute(REFERENCE_TO_ID_ATTR);
		String toQuerySpecificationId = element
				.getAttribute(QUERY_SPECIFICATION_ID_ATTR);
		String tokenId = element.getAttribute(TOKEN_ID_ATTR);
		if (TO_ELT.equals(element.getName())) {
			Namespaces namespaces = getNamespaces(element);

			String toPath = element.getAttribute(PATH_ATTR);
			String toTargetNodes = element.getAttribute(TARGET_NODES_ATTR);
			return reference.createToXML(referenceToId, toPath, toTargetNodes,
					namespaces, toQuerySpecificationId, tokenId, null);
		} else if (TO_PROPERTY_ELT.equals(element.getName())) {
			// Resource (file, folder...) reference
			return reference.createToProperty(referenceToId,toQuerySpecificationId, tokenId);
		} else if (TO_RESOURCE_ELT.equals(element.getName())) {
			// Resource (file, folder...) reference
			return reference.createToResource(referenceToId, toQuerySpecificationId, tokenId);
		} else if (TO_JAVA_ELT.equals(element.getName())) {
			// Java reference
			IJavaQuerySpecification querySpecification = getJavaQuerySpecification(element);
			return reference.createToJava(referenceToId, toQuerySpecificationId, tokenId, querySpecification);
		} else if (TO_JAVA_METHOD_ELT.equals(element.getName())) {
			// Java method reference
			IJavaMethodQuerySpecification querySpecification = getJavaMethodQuerySpecification(element);
			String pathForClass = element.getAttribute(PATH_FOR_CLASS_ATTR);
			return reference.createToJavaMethod(referenceToId, toQuerySpecificationId, tokenId, pathForClass, querySpecification);
		} else if (TO_STATIC_ELT.equals(element.getName())) {
			// static reference
			return reference.createToStatic(referenceToId, toQuerySpecificationId, tokenId);
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
