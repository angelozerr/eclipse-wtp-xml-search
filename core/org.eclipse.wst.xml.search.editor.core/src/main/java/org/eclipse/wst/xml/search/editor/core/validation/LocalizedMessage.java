/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * 		Code comes from org.eclipse.wst.validation.internal.operations.LocalizedMessage
 *******************************************************************************/
package org.eclipse.wst.xml.search.editor.core.validation;

import java.util.Locale;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.validation.internal.core.Message;

/**
 * This class is provided for validators which run only in UI and whose messages, because they
 * come from another tool, are already localized. LocalizedMessage cannot be used by any validator
 * which needs to run in both WebSphere and WSAD.
 */
public class LocalizedMessage extends Message {
	private String _message;

	public LocalizedMessage(int severity, String messageText) {
		this(severity, messageText, null);
	}

	public LocalizedMessage(int severity, String messageText, IResource targetObject) {
		this(severity, messageText, (Object) targetObject);
	}

	public LocalizedMessage(int severity, String messageText, Object targetObject) {
		super(null, severity, null);
		setLocalizedMessage(messageText);
		setTargetObject(targetObject);
	}

	public void setLocalizedMessage(String message) {
		_message = message;
	}

	public String getLocalizedMessage() {
		return _message;
	}

	public String getText() {
		return getLocalizedMessage();
	}

	public String getText(ClassLoader cl) {
		return getLocalizedMessage();
	}

	public String getText(Locale l) {
		return getLocalizedMessage();
	}

	public String getText(Locale l, ClassLoader cl) {
		return getLocalizedMessage();
	}
}
