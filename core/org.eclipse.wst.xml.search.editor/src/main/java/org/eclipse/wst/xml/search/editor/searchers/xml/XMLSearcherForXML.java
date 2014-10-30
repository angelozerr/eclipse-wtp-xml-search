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
package org.eclipse.wst.xml.search.editor.searchers.xml;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToXML;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.util.XMLSearcherForXMLUtils;
import org.eclipse.wst.xml.search.editor.validation.IValidationResult;

public class XMLSearcherForXML implements IXMLSearcher {

	public static final IXMLSearcher INSTANCE = new XMLSearcherForXML();

	// ------------------- Completions

	/**
	 * 
	 */
	public void searchForCompletion(Object selectedNode, String mathingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		ContentAssisitCollectorForXML collector = new ContentAssisitCollectorForXML(
				forceBeforeText, forceEndText,
				(IXMLReferenceToXML) referenceTo, recorder);
		XMLSearcherForXMLUtils.search(selectedNode, mathingString, file,
				referenceTo, collector, true);
	}

	// ------------------- Hyperlinks

	/**
	 * 
	 */
	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		HyperlinkCollectorForXML collector = new HyperlinkCollectorForXML(
				hyperlinkRegion, hyperLinks, startOffset, endOffset);
		XMLSearcherForXMLUtils.search(selectedNode, mathingString, file,
				referenceTo, collector, false);
	}

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

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToXML referenceToXML = (IXMLReferenceToXML) referenceTo;
		IContentAssistAdditionalProposalInfoProvider<IDOMNode> provider = (IContentAssistAdditionalProposalInfoProvider<IDOMNode>) referenceToXML
				.getAdditionalProposalInfoProvider();
		if (provider == null) {
			return null;
		}
		TextInfoForXML collector = new TextInfoForXML(provider);
		XMLSearcherForXMLUtils.search(selectedNode, mathingString, file,
				referenceTo, collector, false);
		return collector.getTextInfo();
	}
}
