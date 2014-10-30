/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.xml.search.editor.internal.util;

import java.io.StringWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.xml.search.editor.internal.Messages;
import org.eclipse.wst.xml.search.editor.internal.Trace;


public class ExceptionHandler {

	private static ExceptionHandler fgInstance = new ExceptionHandler();

	/**
	 * Handles the given <code>CoreException</code>.
	 * 
	 * @param e
	 *            the <code>CoreException</code> to be handled
	 * @param parent
	 *            the dialog window's parent shell
	 * @param title
	 *            the dialog window's window title
	 * @param message
	 *            message to be displayed by the dialog window
	 */
	public static void handle(CoreException e, Shell parent, String title,
			String message) {
		fgInstance.perform(e, parent, title, message);
	}

	protected void perform(CoreException e, Shell shell, String title,
			String message) {
		Trace.trace(Trace.SEVERE, message, e);
		IStatus status = e.getStatus();
		if (status != null) {
			ErrorDialog.openError(shell, title, message, status);
		} else {
			displayMessageDialog(e.getMessage(), shell, title, message);
		}
	}

	// ---- Helper methods
	// -----------------------------------------------------------------------

	private void displayMessageDialog(String exceptionMessage, Shell shell,
			String title, String message) {
		StringWriter msg = new StringWriter();
		if (message != null) {
			msg.write(message);
			msg.write("\n\n"); //$NON-NLS-1$
		}
		if (exceptionMessage == null || exceptionMessage.length() == 0)
			msg.write(Messages.ExceptionDialog_seeErrorLogMessage);
		else
			msg.write(exceptionMessage);
		MessageDialog.openError(shell, title, msg.toString());
	}

}
