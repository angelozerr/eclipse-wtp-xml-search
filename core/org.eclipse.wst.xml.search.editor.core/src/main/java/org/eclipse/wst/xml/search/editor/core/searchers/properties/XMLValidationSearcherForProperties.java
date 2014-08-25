package org.eclipse.wst.xml.search.editor.core.searchers.properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.properties.IPropertiesCollector;
import org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.properties.IPropertiesRequestor;
import org.eclipse.wst.xml.search.core.properties.PropertiesSearchEngine;
import org.eclipse.wst.xml.search.editor.core.util.PropertiesQuerySpecificationUtil;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;


public class XMLValidationSearcherForProperties implements IXMLValidationSearcher {

	public static final IXMLValidationSearcher INSTANCE = new XMLValidationSearcherForProperties();

	public IValidationResult searchForValidation(Object selectedNode,
			String matchingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToProperty referencetoResource = (IXMLReferenceToProperty) referenceTo;
		ValidationResultForProperties collector = new ValidationResultForProperties();
		internalSearch(selectedNode, file, referencetoResource, collector,
				matchingString, true);
		return collector;
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
