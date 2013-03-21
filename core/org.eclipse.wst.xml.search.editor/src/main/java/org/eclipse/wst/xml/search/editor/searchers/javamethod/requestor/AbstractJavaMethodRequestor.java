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
package org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.internal.Trace;

public abstract class AbstractJavaMethodRequestor implements
		IJavaMethodRequestor {

	public String matchPartial(Object selectedNode, String matching,
			IMethod method) {
		IStatus status = validate(method);
		if (status == null || !status.isOK()) {
			return null;
		}
		String methodNameTest = formatMethodName(selectedNode, method);
		if (methodNameTest != null && isMatchPartial(matching, methodNameTest)) {
			return methodNameTest;
		}
		return null;
	}

	protected boolean isMatchPartial(String matching, String methodNameTest) {
		if (methodNameTest.startsWith(matching)) {
			return true;
		}
		return false;
	}

	public IStatus matchTotally(Object selectedNode, String matching,
			IMethod method) {
		String methodNameTest = formatMethodName(selectedNode, method);
		if (methodNameTest != null && isMatchTotally(matching, methodNameTest)) {
			return validate(method);
		}
		return Status.CANCEL_STATUS;
	}

	protected boolean isMatchTotally(String methodNameFromXML,
			String methodNameTest) {
		if (methodNameFromXML.equals(methodNameTest)) {
			return true;
		}
		return false;
	}

	protected abstract String formatMethodName(Object selectedNode,
			IMethod method);

	protected IStatus validate(IMethod method) {
		return doValidate(method);
	}

	protected IStatus doValidate(IMethod method) {
		try {
			if (method.isConstructor()) {
				return Status.CANCEL_STATUS;
			}
			int flags = method.getFlags();
			Boolean checkIsPublic = checkIsPublic();
			if (checkIsPublic != null) {
				if (!Flags.isPublic(flags) == checkIsPublic.booleanValue()) {
					return Status.CANCEL_STATUS;
				}
			}
			Boolean checkIsInterface = checkIsInterface();
			if (checkIsInterface != null) {
				if (!Flags.isInterface(flags) == checkIsInterface.booleanValue()) {
					return Status.CANCEL_STATUS;
				}
			}

		} catch (JavaModelException e) {
			Trace.trace(Trace.SEVERE, "Method constructor", e);
		}
		return Status.OK_STATUS;
	}

	protected Boolean checkIsPublic() {
		return true;
	}

	protected Boolean checkIsInterface() {
		return false;
	}
}
