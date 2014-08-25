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
package org.eclipse.wst.xml.search.editor.core.searchers.xml;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.util.XMLSearcherForXMLUtils;

public class XMLValidationSearcherForXML implements IXMLValidationSearcher {

	public static final IXMLValidationSearcher INSTANCE = new XMLValidationSearcherForXML();

	// ------------------- Validation

	/**
	 * 
	 */
	public IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		ValidationResultForXML collector = new ValidationResultForXML(
				mathingString, startIndex, endIndex);
		XMLSearcherForXMLUtils.search(selectedNode, mathingString, file,
				referenceTo, collector, false);
		return collector;
	}
}
