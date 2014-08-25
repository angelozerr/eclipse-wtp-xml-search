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
package org.eclipse.wst.xml.search.editor.searchers.statics;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.core.statics.IStaticValueQuerySpecification;
import org.eclipse.wst.xml.search.core.statics.IStaticValueVisitor;
import org.eclipse.wst.xml.search.core.statics.StaticValueSearchEngine;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToStatic;
import org.eclipse.wst.xml.search.editor.core.util.StaticQuerySpecificationUtil;
import org.eclipse.wst.xml.search.editor.internal.contentassist.ContentAssistBindingsManager;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;

public class XMLAssistSearcherForStatic implements IXMLAssistSearcher {

	public static final IXMLAssistSearcher INSTANCE = new XMLAssistSearcherForStatic();

	// ------------------- Completions

	/**
	 * 
	 */
	public void searchForCompletion(Object selectedNode, String matchingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		IXMLReferenceToStatic referenceToStatic = (IXMLReferenceToStatic) referenceTo;
		IStaticValueCollector collector = new ContentAssisitCollectorForStatics(
				forceBeforeText, forceEndText, referenceToStatic, recorder);
		internalSearch(selectedNode, file, collector, matchingString, true,
				referenceToStatic);
	}

	// ------------------- Hyperlinks

	/**
	 * 
	 */
	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IXMLReferenceToStatic referenceToStatic = (IXMLReferenceToStatic) referenceTo;
		IStaticValueQuerySpecification querySpecification = StaticQuerySpecificationUtil
				.getStaticQuerySpecification(referenceToStatic);
		if (querySpecification != null) {
		}

		HyperlinkCollectorForStatics collector = new HyperlinkCollectorForStatics(
				hyperlinkRegion, hyperLinks);
		internalSearch(selectedNode, file, collector, mathingString, false,
				referenceToStatic);
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

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToStatic referenceToStatic = (IXMLReferenceToStatic) referenceTo;
		IContentAssistAdditionalProposalInfoProvider<IStaticValue> provider = 
			(IContentAssistAdditionalProposalInfoProvider<IStaticValue>)
				ContentAssistBindingsManager.getDefault().getProvider(referenceToStatic);
		TextInfoForStatic collector = new TextInfoForStatic(provider);
		internalSearch(selectedNode, file, collector,
				mathingString != null ? mathingString : mathingString, false,
				referenceToStatic);
		return collector.getTextInfo();
	}
}
