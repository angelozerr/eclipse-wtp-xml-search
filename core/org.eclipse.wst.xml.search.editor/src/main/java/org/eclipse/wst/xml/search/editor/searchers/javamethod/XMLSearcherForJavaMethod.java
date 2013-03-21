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
package org.eclipse.wst.xml.search.editor.searchers.javamethod;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.JavaElementHyperlink;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToJavaMethod;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.validation.DefaultValidationResult;
import org.eclipse.wst.xml.search.editor.validation.IValidationResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLSearcherForJavaMethod implements IXMLSearcher {

	public static final IXMLSearcher INSTANCE = new XMLSearcherForJavaMethod();

	public void searchForCompletion(Object selectedNode,
			String mathingString, String forceBeforeText, String forceEndText,
			IFile file, IXMLReferenceTo referenceTo,
			IContentAssistProposalRecorder recorder) {
		IXMLReferenceToJavaMethod toJavaMethod = (IXMLReferenceToJavaMethod) referenceTo;
		try {
			String className = getClassName(selectedNode, file, referenceTo);
			if (className == null) {
				return;
			}
			IType type = JdtUtils.getJavaType(file.getProject(), className);
			if (type == null) {
				return;
			}
			IJavaMethodRequestor filter = toJavaMethod.getRequestor(
					selectedNode, file);
			if (filter == null) {
				return;
			}
			try {
				createCompletion(selectedNode, mathingString, recorder, type,
						filter);
			} catch (JavaModelException e) {
				Trace
						.trace(Trace.SEVERE,
								"Error while getting methods for class="
										+ className, e);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private void createCompletion(Object selectedNode, String mathingString,
			IContentAssistProposalRecorder recorder, IType type,
			IJavaMethodRequestor requestor) throws JavaModelException {
		if (type == null) {
			return;
		}
		IMethod method = null;
		IMethod[] methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			String methodName = requestor.matchPartial(selectedNode,
					mathingString, method);
			if (methodName != null) {
				createMethodProposal(recorder, method, methodName);
			}
		}
		createCompletion(selectedNode, mathingString, recorder, JdtUtils
				.getSuperType(type), requestor);
	}

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IXMLReferenceToJavaMethod toJavaMethod = (IXMLReferenceToJavaMethod) referenceTo;
		try {
			String className = getClassName(selectedNode, file, referenceTo);
			if (className == null) {
				return;
			}
			IType type = JdtUtils.getJavaType(file.getProject(), className);
			if (type == null) {
				return;
			}
			IJavaMethodRequestor filter = toJavaMethod.getRequestor(
					selectedNode, file);
			if (filter == null) {
				return;
			}
			try {
				String matching = mathingString;
				createHyperlink(selectedNode, matching, hyperlinkRegion,
						hyperLinks, type, filter);
			} catch (JavaModelException e) {
				Trace
						.trace(Trace.SEVERE,
								"Error while getting methods for class="
										+ className, e);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private void createHyperlink(Object selectedNode, String matching,
			IRegion hyperlinkRegion, List<IHyperlink> hyperLinks, IType type,
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
				hyperLinks
						.add(new JavaElementHyperlink(hyperlinkRegion, method));
			}
		}
		createHyperlink(selectedNode, matching, hyperlinkRegion, hyperLinks,
				JdtUtils.getSuperType(type), requestor);
	}

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

	protected void createMethodProposal(
			IContentAssistProposalRecorder recorder, IMethod method,
			String methodNameToUse) {
		try {
			String parameterNames[] = method.getParameterNames();
			String parameterTypes[] = JdtUtils.getParameterTypesString(method);
			String returnType = JdtUtils.getReturnTypeString(method, true);
			String methodName = method.getElementName();
			String replaceText = methodName;
			StringBuilder buf = new StringBuilder();
			buf.append(replaceText);
			if (parameterTypes.length > 0 && parameterNames.length > 0) {
				buf.append(" (");
				for (int i = 0; i < parameterTypes.length; i++) {
					buf.append(parameterTypes[i]);
					buf.append(' ');
					buf.append(parameterNames[i]);
					if (i < parameterTypes.length - 1)
						buf.append(", ");
				}

				buf.append(") ");
			} else {
				buf.append("() ");
			}
			if (returnType != null) {
				buf.append(Signature.getSimpleName(returnType));
				buf.append(" - ");
			} else {
				buf.append(" void - ");
			}
			buf.append(method.getParent().getElementName());
			String displayText = buf.toString();
			org.eclipse.swt.graphics.Image image = XMLSearchEditorPlugin
					.getDefault().getJavaElementLabelProvider().getImageLabel(
							method, method.getFlags() | 2);
			recorder.recordProposal(image, 10, displayText, methodNameToUse,
					null
			/*
			 * new ProposalInfo((IMember) method).getInfo(EditorUtils
			 * .getProgressMonitor())
			 */);
		} catch (JavaModelException _ex) {
		}
	}

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IXMLReferenceToJavaMethod toJavaMethod = (IXMLReferenceToJavaMethod) referenceTo;
		StringBuilder result = new StringBuilder();
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
				createTextInfo(selectedNode, mathingString, result, type, filter);
			} catch (JavaModelException e) {
				Trace
						.trace(Trace.SEVERE,
								"Error while getting methods for class="
										+ className, e);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private void createTextInfo(Object selectedNode, String matching,
			StringBuilder result, IType type, IJavaMethodRequestor requestor)
			throws JavaModelException {
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
				String info = new ProposalInfo(method).getInfo(EditorUtils
						.getProgressMonitor());
				if (info != null) {
					if (result.length() > 0) {
						result.append("<br/></br/>");
					}
					result.append(info);
				}
			}
		}
		createTextInfo(selectedNode, matching, result, JdtUtils
				.getSuperType(type), requestor);
	}
}
