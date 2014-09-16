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
package org.eclipse.wst.xml.search.editor.core.references;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.util.FileUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo.ToType;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.IXMLExpressionParser;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReference;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToExpression;
import org.w3c.dom.Node;

public class XMLReferencesUtil {

	public static IXMLReference getXMLReference(Node node, IFile file) {
		return getXMLReference(node, FileUtils.getContentTypeId(file));
	}

	public static IXMLReference getXMLReference(Node node, String contentTypeId) {
		return XMLReferencesManager.getInstance().getXMLReference(node,
				contentTypeId);
	}

	public static List<IXMLReference> getXMLReferenceInversed(Node node,
			String contentTypeId) {
		return XMLReferencesManager.getInstance().getXMLReferenceInversed(node,
				contentTypeId);
	}

	public static boolean match(Node node, IXMLReferencePath referencePath) {
		return XMLReferencesManager.getInstance().match(node, referencePath);
	}

	public static IXMLReference createXMLReference(String referenceId,String fromPath,
			String fromTargetNodes, Namespaces namespaces,
			String querySpecificationId, String[] contentTypeIds,
			IXMLReferenceFilter filter, IXMLReferenceValidator validator) {
		return new XMLReference(referenceId, fromPath, fromTargetNodes, namespaces,
				querySpecificationId, contentTypeIds, filter, validator);
	}

	public static IXMLReferenceToExpression createXMLReferenceToExpression(
			String referenceId, String fromPath, String fromTargetNodes, Namespaces namespaces,
			String querySpecificationId, String[] contentTypeIds,
			IXMLReferenceFilter filter, IXMLReferenceValidator validator,
			IXMLExpressionParser parser) {
		return new XMLReferenceToExpression(referenceId, fromPath, fromTargetNodes,
				namespaces, querySpecificationId, contentTypeIds, filter,
				validator, parser);
	}

	public static void registerXMLReference(IXMLReference reference) {
		XMLReferencesManager.getInstance().registerXMReference(reference);
	}

	public static XMLReferencePathResult getXMLReferencesByPathTo(String path,
			String contentTypeId) {
		return new XMLReferencePathResult(XMLReferencesManager.getInstance()
				.getXMLReferencesByPathTo(path, contentTypeId), true);
	}

	public static XMLReferencePathResult getReferencePath(Node selectedNode,
			IFile file) {
		return getReferencePath(selectedNode, FileUtils.getContentTypeId(file));
	}

	public static XMLReferencePathResult getReferencePath(Node selectedNode,
			String contentTypeId) {
		List<IXMLReference> references = XMLReferencesUtil
				.getXMLReferenceInversed(selectedNode, contentTypeId);
		if (references != null && references.size() > 0) {
			XMLReferencePathResult referencePaths = new XMLReferencePathResult(
					references.size(), false);
			for (IXMLReference reference : references) {
				referencePaths.add(reference.getFrom());
			}
			return referencePaths;
		} else {
			IXMLReference reference = XMLReferencesUtil.getXMLReference(
					selectedNode, contentTypeId);
			if (reference != null) {
				IXMLReferencePath referencePath = null;
				for (IXMLReferenceTo referenceTo : reference.getTo()) {
					if (referenceTo.getType() == ToType.XML) {
						referencePath = (IXMLReferencePath) referenceTo;
						break;
					}
				}

				if (referencePath != null) {
					return XMLReferencesUtil.getXMLReferencesByPathTo(
							referencePath.getKeyPath(), contentTypeId);
				}
			}
		}
		return null;
	}

	public static Collection<IXMLReference> getAllReferencesWithOneReferenceTo(
			ToType toType) {
		return XMLReferencesManager.getInstance()
				.getAllReferencesWithOneReferenceTo(toType);
	}

}
