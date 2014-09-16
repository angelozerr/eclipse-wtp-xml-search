package org.eclipse.wst.xml.search.editor.core.searchers.javamethod;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.xml.search.editor.core.searchers.IXMLValidationSearcher;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.requestor.IJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.core.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.core.validation.DefaultValidationResult;
import org.eclipse.wst.xml.search.editor.core.validation.IValidationResult;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLValidationSearcherForJavaMethod implements IXMLValidationSearcher {

	public static final IXMLValidationSearcher INSTANCE = new XMLValidationSearcherForJavaMethod();

	public IValidationResult searchForValidation(Object selectedNode,
			String matchingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToJavaMethod toJavaMethod = (IXMLReferenceToJavaMethod) referenceTo;
		DefaultValidationResult result = new DefaultValidationResult();
		try {
			String className = getClassName(selectedNode, file, referenceTo);
			if (className == null) {
				return null;
			}
			IType type = JdtUtils.getJavaType(file.getProject(), className);
			if (type == null) {
				return null;
			}
			IJavaMethodRequestor filter = toJavaMethod.getRequestor(
					selectedNode, file);
			if (filter == null) {
				return null;
			}
			try {
				createValidation(selectedNode, matchingString, result, type, filter);
			} catch (JavaModelException e) {
				Trace
						.trace(Trace.SEVERE,
								"Error while getting methods for class="
										+ className, e);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void createValidation(Object selectedNode, String matching,
			DefaultValidationResult result, IType type,
			IJavaMethodRequestor requestor) throws JavaModelException {
		if (type == null) {
			return;
		}
		IMethod method = null;
		IMethod[] methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			IStatus status = requestor.matchTotally(selectedNode, matching,
					method);
			if (status != null && status.isOK()) {
				result.addElement();
			}
		}
		createValidation(selectedNode, matching, result, JdtUtils
				.getSuperType(type), requestor);
	}

	private String getClassName(Object selectedNode, IFile file,
			IXMLReferenceTo referenceTo) throws XPathExpressionException {
		IXMLReferenceToJavaMethod toJavaMethod = (IXMLReferenceToJavaMethod) referenceTo;
		return toJavaMethod.extractClassName(getOwnerNode((Node)selectedNode), file,
				null, null);
	}

	private Node getOwnerNode(Node node) {
		short nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ((Attr) node).getOwnerElement();
		case Node.TEXT_NODE:
			return ((Text) node).getParentNode();
		}
		return node;
	}

}
