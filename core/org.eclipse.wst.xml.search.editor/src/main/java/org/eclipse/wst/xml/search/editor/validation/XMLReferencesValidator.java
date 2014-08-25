package org.eclipse.wst.xml.search.editor.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.ui.internal.reconcile.validator.AnnotationInfo;
import org.eclipse.wst.sse.ui.internal.reconcile.validator.IncrementalReporter;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.core.validation.LocalizedMessage;
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
				LocalizedMessage message = reference.getValidator().validate(reference, attr, file, this);
				addMessage(message, this, reporter);
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
			LocalizedMessage message = reference.getValidator().validate(reference, node, file, this);
			addMessage(message, this, reporter);
		}
	}

	private void addMessage(LocalizedMessage message, IValidator validator, IReporter reporter) {
		if (message != null) {
			AnnotationInfo info = new AnnotationInfo(message);
			((IncrementalReporter) reporter).addAnnotationInfo(validator,
					info);
		}
	}

}