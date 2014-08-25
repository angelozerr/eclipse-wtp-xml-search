package org.eclipse.wst.xml.search.editor.core.searchers;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;

//Searcher of core plugin, only contains searcher for validation
public interface IXMLValidationSearcher {
	IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo);
}
