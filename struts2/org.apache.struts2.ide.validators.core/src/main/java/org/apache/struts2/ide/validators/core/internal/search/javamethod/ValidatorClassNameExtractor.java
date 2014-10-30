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
package org.apache.struts2.ide.validators.core.internal.search.javamethod;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.AbstractClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;
import org.w3c.dom.Node;

public class ValidatorClassNameExtractor extends AbstractClassNameExtractor {

	public static final IClassNameExtractor INSTANCE = new ValidatorClassNameExtractor();

	@Override
	public String[] doExtractClassNames(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		String fileName = file.getName();
		int index = fileName.indexOf('-');
		if (index == -1) {
			return null;
		}
		String className = fileName.substring(0, index);
		String packageName = JdtUtils.getPackageName(file.getParent());
		if (packageName != null) {
			return new String[] { packageName + "." + className };
		}
		return new String[] { className };
	}

}
