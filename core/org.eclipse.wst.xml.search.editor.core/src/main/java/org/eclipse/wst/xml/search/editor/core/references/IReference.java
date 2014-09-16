package org.eclipse.wst.xml.search.editor.core.references;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;

public interface IReference {
	
	String getReferenceId();
	
	List<IXMLReferenceTo> getTo();
	
	IReference addTo(IXMLReferenceTo to);
	
	IXMLReferenceToXML createToXML(
			String id,
			String path,
			String targetNodes,
			Namespaces namespaces,
			String querySpecificationId,
			String tokenId,
			IXMLReferenceFilter filter);

	IXMLReferenceToResource createToResource(
			String id,
			String querySpecificationId,
			String tokenId);

	IXMLReferenceToJava createToJava(String id,
			String querySpecificationId, String tokenId,
			IJavaQuerySpecification querySpecification);

	IXMLReferenceToJavaMethod createToJavaMethod(String id,
			String querySpecificationId, String tokenId,
			String pathForClass,
			IJavaMethodQuerySpecification querySpecification);

	IXMLReferenceToStatic createToStatic(
			String id,
			String querySpecificationId,
			String tokenId);

	IXMLReferenceToProperty createToProperty(
			String id,
			String querySpecificationId,
			String tokenId);

	boolean isExpression();
}
