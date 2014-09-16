package org.eclipse.wst.xml.search.editor.core.searchers.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.xml.search.editor.core.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;

public class XMLValidationSearcherForJava implements IXMLValidationSearcher{

	// ------------------- Validation

	public IValidationResult searchForValidation(Object selectedNode,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IValidationResult result = new ValidationResultForJava();
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		if (type != null ) {
			result.setNbElements(1);
			IType[] superTypes = ((IXMLReferenceToJava) referenceTo).getExtends(selectedNode, file);
			if (superTypes != null && superTypes.length > 0) {
				boolean isHierarchyCorrect = false;
				for (IType superType : superTypes) {
					try {
						if (JdtUtils.hierarchyContainsComponent(type,superType.getFullyQualifiedName())) {
							isHierarchyCorrect = true;
							break;
						}
					} catch(JavaModelException e){
						Trace.trace(Trace.SEVERE, e.getMessage(), e);
					}
				}
				if (!isHierarchyCorrect) {
					result.setNbElements(-1);
				}
			}
		}
		return result;
	}
}
