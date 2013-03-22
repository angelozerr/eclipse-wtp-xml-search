package org.apache.struts2.ide.ui.internal.validation;

import org.apache.struts2.ide.ui.internal.Messages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.validators.DefaultDOMNodeValidator;
import org.w3c.dom.Node;

public class Struts2ActionValidator extends DefaultDOMNodeValidator {

	@Override
	protected String getMessageText(IXMLReference reference, int nbElements,
			Node node, String textContent) {
		if (nbElements < 1) {
			return NLS.bind(Messages.Validation_ActionNotFound, textContent,
					nbElements);
		} else {
			return NLS.bind(Messages.Validation_ActionNonUnique, textContent,
					nbElements);
		}
	}
}
