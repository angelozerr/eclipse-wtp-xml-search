package org.eclipse.wst.xml.search.editor.searchers.resource;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;
import org.eclipse.wst.xml.search.core.resource.IResourceQuerySpecification;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.ResourceSearchEngine;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistAdditionalProposalInfoProvider;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.core.util.ResourceQuerySpecificationUtil;
import org.eclipse.wst.xml.search.editor.internal.contentassist.ContentAssistBindingsManager;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;

public class XMLAssistSearcherForResource implements IXMLAssistSearcher {

	public static final IXMLAssistSearcher INSTANCE = new XMLAssistSearcherForResource(); 
	
	// ------------------- Completions

	/**
	 * 
	 */
	public void searchForCompletion(Object selectedNode, String mathingString,
			String forceBeforeText, String forceEndText, IFile file,
			IXMLReferenceTo referenceTo, IContentAssistProposalRecorder recorder) {
		IXMLReferenceToResource referencetoResource = (IXMLReferenceToResource) referenceTo;
		ContentAssisitCollectorForResource collector = new ContentAssisitCollectorForResource(
				selectedNode, recorder, ContentAssistBindingsManager.getDefault().getProvider(referencetoResource));
		internalSearch(selectedNode, file, referencetoResource, collector,
				mathingString, false);
	}

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IXMLReferenceToResource referencetoResource = (IXMLReferenceToResource) referenceTo;
		HyperlinkCollectorForResource collector = new HyperlinkCollectorForResource(
				hyperlinkRegion, hyperLinks);
		internalSearch(selectedNode, file, referencetoResource, collector,
				mathingString, true);
	}

	protected void internalSearch(Object selectedNode, IFile file,
			IXMLReferenceToResource referencetoResource,
			IResourceCollector collector, String mathingString,
			boolean fullMatch) {
		IResourceQuerySpecification[] querySpecifications = ResourceQuerySpecificationUtil
				.getQuerySpecifications(referencetoResource);
		if (querySpecifications == null || querySpecifications.length < 1)
			return;
		IResourceQuerySpecification querySpecification = null;
		for (int i = 0; i < querySpecifications.length; i++) {
			querySpecification = querySpecifications[i];
			IResourceRequestor requestor = querySpecification.getRequestor();
			if (querySpecification.isMultiResource()) {
				IResource[] containers = querySpecification.getResources(
						selectedNode, file);
				ResourceSearchEngine.getDefault().search(selectedNode,
						containers, requestor, collector,
						querySpecification.getURIResolver(file, selectedNode),
						mathingString, fullMatch, null);
			} else {
				IResource container = querySpecification.getResource(
						selectedNode, file);
				ResourceSearchEngine.getDefault().search(selectedNode,
						container, requestor, collector,
						querySpecification.getURIResolver(file, selectedNode),
						mathingString, fullMatch, null);
			}
		}
	}

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToResource referencetoResource = (IXMLReferenceToResource) referenceTo;
		IContentAssistAdditionalProposalInfoProvider<IResource> provider =
            (IContentAssistAdditionalProposalInfoProvider<IResource>) ContentAssistBindingsManager.getDefault().
            getProvider(referencetoResource);
		if (provider == null) {
			return null;
		}
		TextHoverForResource collector = new TextHoverForResource(provider);
		internalSearch(selectedNode, file, referencetoResource, collector,
				mathingString, true);
		return collector.getTextInfo();
	}
}
