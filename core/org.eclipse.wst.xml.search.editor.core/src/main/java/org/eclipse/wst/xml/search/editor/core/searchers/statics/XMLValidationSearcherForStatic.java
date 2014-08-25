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
package org.eclipse.wst.xml.search.editor.core.searchers.statics;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.core.statics.IStaticValueQuerySpecification;
import org.eclipse.wst.xml.search.core.statics.IStaticValueVisitor;
import org.eclipse.wst.xml.search.core.statics.StaticValueSearchEngine;
import org.eclipse.wst.xml.search.editor.core.util.StaticQuerySpecificationUtil;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;

public class XMLValidationSearcherForStatic implements IXMLValidationSearcher {

	public static final IXMLValidationSearcher INSTANCE = new XMLValidationSearcherForStatic();

	// ------------------- Validation

	/**
	 * 
	 */
	public IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToStatic referenceToStatic = (IXMLReferenceToStatic) referenceTo;
		ValidationResultForStatics collector = new ValidationResultForStatics(
				mathingString, startIndex, endIndex);
		internalSearch(selectedNode, file, collector, mathingString, false,
				referenceToStatic);
		return collector;
	}
	
	private void internalSearch(Object selectedNode, IFile file,
			IStaticValueCollector collector, String matchingString,
			boolean startsWith, IXMLReferenceToStatic referenceToStatic) {
		IStaticValueQuerySpecification querySpecification = StaticQuerySpecificationUtil
				.getStaticQuerySpecification(referenceToStatic);
		if (querySpecification != null) {
			IStaticValueVisitor visitor = querySpecification.getVisitor(
					selectedNode, file);
			if (visitor == null) {
				return;
			}
			StaticValueSearchEngine.getDefault().search(selectedNode, file,
					visitor, collector, matchingString, startsWith, null);
		}
	}
}
