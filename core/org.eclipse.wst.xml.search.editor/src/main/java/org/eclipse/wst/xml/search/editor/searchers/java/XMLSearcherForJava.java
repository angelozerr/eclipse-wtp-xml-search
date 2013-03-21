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
package org.eclipse.wst.xml.search.editor.searchers.java;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.internal.contentassist.JavaCompletionUtils;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.JavaElementHyperlink;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.validation.IValidationResult;

public class XMLSearcherForJava implements IXMLSearcher {

	public static final IXMLSearcher INSTANCE = new XMLSearcherForJava();

	// ------------------- Completions

	/**
	 * 
	 */
	public void searchForCompletion(Object selectedNode,
			String mathingString, String forceBeforeText, String forceEndText,
			IFile file, IXMLReferenceTo referenceTo,
			IContentAssistProposalRecorder recorder) {
		IXMLReferenceToJava referenceToJava = (IXMLReferenceToJava) referenceTo;
		// Find class from
		IType[] classes = referenceToJava.getExtends(selectedNode, file);
		if (classes != null) {
			for (int i = 0; i < classes.length; i++) {
				JavaCompletionUtils.addTypeHierachyAttributeValueProposals(
						mathingString, file, recorder, classes[i], 12);
			}
		} else {
			JavaCompletionUtils.addClassValueProposals(mathingString, file,
					recorder);
		}
	}

	// ------------------- Hyperlinks

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		if (type != null) {
			hyperLinks.add(new JavaElementHyperlink(hyperlinkRegion, type));
		}
	}

	// ------------------- Validation

	public IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		return (type != null ? IValidationResult.OK : IValidationResult.NOK);
	}

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		if (type != null && type instanceof IMember) {
			return new ProposalInfo((IMember) type).getInfo(EditorUtils
					.getProgressMonitor());
		}
		return null;
	}
}
