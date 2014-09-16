package org.eclipse.wst.xml.search.editor.core.references;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.editor.core.references.filters.IXMLReferenceFilter;
import org.eclipse.wst.xml.search.editor.core.searchers.java.IJavaQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.IJavaMethodQuerySpecification;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferencePath;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToProperty;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToResource;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferenceToStatic;

public abstract class AbstractReference extends ArrayList<IXMLReferenceTo> implements IReference {

	public String getReferenceId() {
		return null;
	}
	
	public List<IXMLReferenceTo> getTo() {
		return this;
	}

	public IReference addTo(IXMLReferenceTo to) {
		super.add(to);
		return this;
	}

	public IXMLReferenceToXML createToXML(
			String id,
			String path,
			String targetNodes,
			Namespaces namespaces,
			String querySpecificationId,
			String tokenId,
			IXMLReferenceFilter filter) {
		return new XMLReferencePath(this, id, path, targetNodes
				.split(","), namespaces, querySpecificationId, tokenId, filter);
	}

	public IXMLReferenceToResource createToResource(
			String id,
			String querySpecificationId,
			String tokenId) {
		return new XMLReferenceToResource(this, id, querySpecificationId, tokenId);
	}

	public IXMLReferenceToJava createToJava(String id, String querySpecificationId, String tokenId,
			IJavaQuerySpecification querySpecification) {
		return new XMLReferenceToJava(this, id, querySpecificationId,
				querySpecification, tokenId);
	}

	public IXMLReferenceToJavaMethod createToJavaMethod(String id,
			String querySpecificationId, String tokenId,
			String pathForClass,
			IJavaMethodQuerySpecification querySpecification) {
		return new XMLReferenceToJavaMethod(this, id,
				querySpecificationId, pathForClass, querySpecification, tokenId);
	}

	public IXMLReferenceToStatic createToStatic(
			String id,
			String querySpecificationId,
			String tokenId) {
		return new XMLReferenceToStatic(this, id, querySpecificationId, tokenId);
	}

	public IXMLReferenceToProperty createToProperty(
			String id,
			String querySpecificationId,
			String tokenId) {
		return new XMLReferenceToProperty(this, id,querySpecificationId, tokenId);
	}

	public boolean isExpression() {
		return false;
	}

}
