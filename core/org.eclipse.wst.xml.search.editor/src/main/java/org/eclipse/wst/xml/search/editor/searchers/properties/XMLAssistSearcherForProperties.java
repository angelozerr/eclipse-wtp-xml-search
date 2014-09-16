package org.eclipse.wst.xml.search.editor.searchers.properties;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.core.properties.IPropertiesCollector;
import org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.properties.IPropertiesRequestor;
import org.eclipse.wst.xml.search.core.properties.PropertiesSearchEngine;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.core.util.PropertiesQuerySpecificationUtil;


public class XMLAssistSearcherForProperties implements IXMLAssistSearcher {

	public static final IXMLAssistSearcher iNSTANCE = new XMLAssistSearcherForProperties();

	public void searchForCompletion(Object selectedNode, String mathingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		IXMLReferenceToProperty referenceToProperty = (IXMLReferenceToProperty) referenceTo;
		// 1) Create a collector for content assist
		ContentAssisitCollectorForProperties collector = new ContentAssisitCollectorForProperties(
				selectedNode, referenceToProperty, recorder);
		// 2) call properties search engine.
		internalSearch(selectedNode, file, referenceToProperty, collector,
				mathingString, false);
	}

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IXMLReferenceToProperty referenceToProperty = (IXMLReferenceToProperty) referenceTo;
		HyperlinkCollectorForProperties collector = new HyperlinkCollectorForProperties(
				hyperlinkRegion, hyperLinks, textEditor);
		internalSearch(selectedNode, file, referenceToProperty, collector,
				mathingString, true);

	}

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		return null;
	}

	protected void internalSearch(Object selectedNode, IFile file,
			IXMLReferenceToProperty referenceToProperty,
			IPropertiesCollector collector, String mathingString,
			boolean fullMatch) {
		IPropertiesQuerySpecification[] querySpecifications = PropertiesQuerySpecificationUtil
				.getQuerySpecifications(referenceToProperty);
		if (querySpecifications == null || querySpecifications.length < 1)
			return;
		IPropertiesQuerySpecification querySpecification = null;
		for (int i = 0; i < querySpecifications.length; i++) {
			querySpecification = querySpecifications[i];
			IPropertiesRequestor requestor = querySpecification.getRequestor();
			if (querySpecification.isMultiResource()) {
				IResource[] containers = querySpecification.getResources(
						selectedNode, file);
				PropertiesSearchEngine.getDefault().search(selectedNode,
						containers, requestor, collector, mathingString,
						fullMatch, null);
			} else {
				IResource container = querySpecification.getResource(
						selectedNode, file);
				PropertiesSearchEngine.getDefault().search(selectedNode,
						container, requestor, collector, mathingString,
						fullMatch, null);
			}
		}
	}

}
