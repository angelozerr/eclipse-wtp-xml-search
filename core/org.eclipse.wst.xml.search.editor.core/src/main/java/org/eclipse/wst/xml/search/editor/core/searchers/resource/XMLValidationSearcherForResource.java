package org.eclipse.wst.xml.search.editor.core.searchers.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.resource.IResourceCollector;
import org.eclipse.wst.xml.search.core.resource.IResourceQuerySpecification;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.ResourceSearchEngine;
import org.eclipse.wst.xml.search.editor.core.util.ResourceQuerySpecificationUtil;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.searchers.properties.XMLValidationSearcherForProperties;

public class XMLValidationSearcherForResource implements IXMLValidationSearcher {

	public static final IXMLValidationSearcher INSTANCE = new XMLValidationSearcherForProperties();

	public IValidationResult searchForValidation(Object selectedNode,
			String matchingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToResource referencetoResource = (IXMLReferenceToResource) referenceTo;
		ValidationResultForResource collector = new ValidationResultForResource();
		internalSearch(selectedNode, file, referencetoResource, collector,
				matchingString, true);
		return collector;
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
}
