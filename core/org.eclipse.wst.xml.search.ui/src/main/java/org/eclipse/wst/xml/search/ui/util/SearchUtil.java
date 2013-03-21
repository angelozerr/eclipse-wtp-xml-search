/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.util;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.core.XMLSearchEngine2;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;
import org.eclipse.wst.xml.search.ui.internal.Messages;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchQuery;

/**
 * Utility class for Search UI.
 * 
 */
public class SearchUtil {

	/**
	 * This helper method with Object as parameter is needed to prevent the
	 * loading of the Search plug-in: the VM verifies the method call and hence
	 * loads the types used in the method signature, eventually triggering the
	 * loading of a plug-in (in this case ISearchQuery results in Search plug-in
	 * being loaded).
	 * 
	 * @param query
	 *            the search query
	 */
	public static void runQueryInBackground(Object query) {
		NewSearchUI.runQueryInBackground((ISearchQuery) query);
	}

	/**
	 * This helper method with Object as parameter is needed to prevent the
	 * loading of the Search plug-in: the VM verifies the method call and hence
	 * loads the types used in the method signature, eventually triggering the
	 * loading of a plug-in (in this case ISearchQuery results in Search plug-in
	 * being loaded).
	 * 
	 * @param context
	 *            the runnable context
	 * @param query
	 *            the search query
	 * @return status
	 */
	public static IStatus runQueryInForeground(IRunnableContext context,
			Object query) {
		return NewSearchUI.runQueryInForeground(context, (ISearchQuery) query);
	}

	/**
	 * Returns the {@link ITextEditor} from the given event.
	 * 
	 * @param event
	 * @return
	 */
	public static ITextEditor getTextEditor(ExecutionEvent event) {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		return getTextEditor(editor);
	}

	/**
	 * Returns the {@link ITextEditor} from the given editor part.
	 * 
	 * @param editor
	 * @return
	 */
	public static ITextEditor getTextEditor(IEditorPart editor) {
		if (editor instanceof ITextEditor)
			return (ITextEditor) editor;
		else {
			Object o = editor.getAdapter(ITextEditor.class);
			if (o != null)
				return (ITextEditor) o;
		}
		return null;
	}

	/**
	 * Returns the {@link IFile} from the given editor part.
	 * 
	 * @param editor
	 * @return
	 */
	public static IFile getFile(IEditorPart editor) {
		IEditorInput editorInput = editor.getEditorInput();
		if (!(editorInput instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput) editorInput).getFile();
	}

	/**
	 * Perform the XMl Search.
	 * 
	 * @param shell
	 *            the parent shell.
	 * @param querySpecificationRegistry
	 *            registry of the XML query specification to execute.
	 * @param reporter
	 *            XML search reporter.
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public static void performNewSearch(Shell shell,
			IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchReporter reporter) throws InterruptedException,
			CoreException {

		XMLSearchQuery query = new XMLSearchQuery(querySpecificationRegistry,
				XMLSearchEngine2.getDefault(), reporter);
		if (query.canRunInBackground()) {
			/*
			 * This indirection with Object as parameter is needed to prevent
			 * the loading of the Search plug-in: the VM verifies the method
			 * call and hence loads the types used in the method signature,
			 * eventually triggering the loading of a plug-in (in this case
			 * ISearchQuery results in Search plug-in being loaded).
			 */
			SearchUtil.runQueryInBackground(query);
		} else {
			IProgressService progressService = PlatformUI.getWorkbench()
					.getProgressService();
			/*
			 * This indirection with Object as parameter is needed to prevent
			 * the loading of the Search plug-in: the VM verifies the method
			 * call and hence loads the types used in the method signature,
			 * eventually triggering the loading of a plug-in (in this case it
			 * would be ISearchQuery).
			 */
			IStatus status = SearchUtil.runQueryInForeground(progressService,
					query);
			if (status.matches(IStatus.ERROR | IStatus.INFO | IStatus.WARNING)) {
				ErrorDialog.openError(shell,
						Messages.Search_Error_search_title,
						Messages.Search_Error_search_message, status);
			}
		}
	}

}
