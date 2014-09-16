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

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.editor.core.references.IReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.classnameprovider.XPathClassNameExtractor;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.requestor.IJavaMethodRequestor;
import org.w3c.dom.Node;

public class XMLReferenceToJavaMethod extends AbstractXMLReferenceTo implements
		IXMLReferenceToJavaMethod {

	private static final String PARENT_TOKEN = "../";
	private static final int PARENT_TOKEN_LENGTH = PARENT_TOKEN.length();

	private final String pathForClass;
	private final IJavaMethodQuerySpecification querySpecification;
	private boolean findByParentNode = false;
	private String findByAttrName = null;

	public XMLReferenceToJavaMethod(IReference ownerReference, String id,
			String querySpecificationId, String pathForClass,
			IJavaMethodQuerySpecification querySpecification, String tokenId) {
		super(ownerReference, id, querySpecificationId, tokenId);
		this.pathForClass = pathForClass;
		this.querySpecification = querySpecification;
		if (pathForClass != null) {
			findByAttrName = getAttrCondition(pathForClass);
			if (findByAttrName == null) {
				if (pathForClass.startsWith(PARENT_TOKEN)) {
					findByParentNode = true;
					findByAttrName = getAttrCondition(pathForClass.substring(
							PARENT_TOKEN_LENGTH, pathForClass.length()));
				}
			}
		}
	}

	private static String getAttrCondition(String s) {
		if (s.startsWith("@")) {
			return s.substring(1, s.length());
		}
		return null;
	}

	public ToType getType() {
		return ToType.JAVA_METHOD;
	}

	public String getPathForClass() {
		return pathForClass;
	}

	public IClassNameExtractor getClassNameExtractor(Object selectedNode,
			IFile file) {
		if (querySpecification != null) {
			return querySpecification.getClassNameExtractor(selectedNode, file);
		}
		return null;
	}

	public String extractClassName(Node selectedNode, IFile file,
			String xpathFactoryProviderId, NamespaceInfos namespaceInfo)
			throws XPathExpressionException {
		IClassNameExtractor extractor = getClassNameExtractor(selectedNode,
				file);
		if (extractor == null) {
			extractor = XPathClassNameExtractor.INSTANCE;
		}
		if (extractor != null) {
			return extractor.extractClassName(selectedNode, file, pathForClass,
					findByAttrName, findByParentNode, xpathFactoryProviderId,
					namespaceInfo);
		}
		return null;
	}

	public boolean isFindByParentNode() {
		return findByParentNode;
	}

	public String getFindByAttrName() {
		return findByAttrName;
	}

	public IJavaMethodRequestor getRequestor(Object selectedNode, IFile file) {
		if (querySpecification != null) {
			return querySpecification.getRequestor(selectedNode, file);
		}
		return null;
	}
}
