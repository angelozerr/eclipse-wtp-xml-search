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
package org.eclipse.wst.xml.search.editor.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XMLReferencesValidator extends AbstractTagValidator {

	@Override
	protected void doValidateStartTag(
			IStructuredDocumentRegion structuredDocumentRegion,
			IReporter reporter, IFile file, IStructuredModel model) {
		IDOMNode node = DOMUtils.getNodeByOffset(model,
				structuredDocumentRegion.getStartOffset());
		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}

		IDOMElement element = (IDOMElement) node;
		NamedNodeMap map = element.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			IDOMAttr attr = (IDOMAttr) map.item(i);
			IXMLReference reference = XMLReferencesUtil.getXMLReference(attr,
					file);
			if (reference != null) {
				reference.getValidator().validate(reference, attr, file, this,
						reporter, false);
			}
		}
	}

	@Override
	protected void doValidateXMLContent(
			IStructuredDocumentRegion structuredDocumentRegion,
			IReporter reporter, IFile file, IStructuredModel model) {

		IDOMNode node = DOMUtils.getNodeByOffset(model,
				structuredDocumentRegion.getStartOffset());
		if (node == null || node.getNodeType() != Node.TEXT_NODE) {
			return;
		}
		IDOMText text = (IDOMText) node;
		IXMLReference reference = XMLReferencesUtil.getXMLReference(node, file);
		if (reference != null) {
			reference.getValidator().validate(reference, node, file, this,
					reporter, false);
		}
	}

}
