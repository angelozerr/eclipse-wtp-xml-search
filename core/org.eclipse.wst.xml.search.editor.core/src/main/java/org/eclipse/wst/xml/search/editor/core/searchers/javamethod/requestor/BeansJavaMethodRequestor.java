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
package org.eclipse.wst.xml.search.editor.core.searchers.javamethod.requestor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public class BeansJavaMethodRequestor extends AbstractJavaMethodRequestor {

	protected static final String SET_PREFIX = "set";
	protected static final String GET_PREFIX = "get";
	protected static final String IS_PREFIX = "is";

	private final String prefix;
	private final int prefixLength;
	private final boolean checkCaseForFirstChar;

	public BeansJavaMethodRequestor(String prefix, boolean checkCaseForFirstChar) {
		this.prefix = prefix;
		this.prefixLength = prefix.length();
		this.checkCaseForFirstChar = checkCaseForFirstChar;
	}

	@Override
	protected String formatMethodName(Object selectedNode, IMethod method) {
		if (!isStartPefix(method)) {
			return null;
		}
		if (method.getElementName().length() <= prefixLength) {
			return null;
		}
		String formattedMethodName = method.getElementName().substring(
				prefixLength, method.getElementName().length());
		Boolean b = isUpperCaseForFirstChar();
		if (b == null) {
			return formattedMethodName;
		}
		String fisrtChar = formattedMethodName.substring(0, 1);
		if (b.booleanValue()) {
			return fisrtChar.toUpperCase()
					+ formattedMethodName.substring(1, formattedMethodName
							.length());
		}
		return fisrtChar.toLowerCase()
				+ formattedMethodName
						.substring(1, formattedMethodName.length());
	}

	@Override
	protected IStatus validate(IMethod method) {
		IStatus status = super.validate(method);
		if (status == null || status.isOK()) {
			if (!isStartPefix(method)) {
				return Status.CANCEL_STATUS;
			}
			return Status.OK_STATUS;
		}
		return status;
	}

	protected boolean isStartPefix(IMethod method) {
		String methodName = method.getElementName();
		return methodName.startsWith(prefix);
	}
	
	@Override
	protected boolean isMatchPartial(String matching, String methodNameTest) {
		if (matching.length() == 0) {
			return true;
		}
		if (super.isMatchPartial(matching.substring(1,
				matching.length()), methodNameTest.substring(1,
				methodNameTest.length()))) {
			String s1 = matching.substring(0, 1);
			String s2 = methodNameTest.substring(0, 1);
			return s1.equalsIgnoreCase(s2);
		}
		return false;
	}

	@Override
	protected boolean isMatchTotally(String methodNameFromXML,
			String methodNameTest) {
		if (methodNameFromXML.length() < prefixLength) {
			return false;
		}
		if (super.isMatchTotally(methodNameFromXML.substring(1,
				methodNameFromXML.length()), methodNameTest.substring(1,
				methodNameTest.length()))) {
			String s1 = methodNameFromXML.substring(0, 1);
			String s2 = methodNameTest.substring(0, 1);
			return s1.equalsIgnoreCase(s2);
		}
		return false;
	}

	protected Boolean isUpperCaseForFirstChar() {
		return false;
	}

}
