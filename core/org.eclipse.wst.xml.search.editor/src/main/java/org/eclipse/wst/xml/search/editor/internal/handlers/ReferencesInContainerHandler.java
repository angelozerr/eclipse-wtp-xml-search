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
package org.eclipse.wst.xml.search.editor.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.queryspecifications.XMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.editor.internal.Messages;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.editor.internal.reporter.XMLSearchReporterManager;
import org.eclipse.wst.xml.search.editor.internal.util.ExceptionHandler;
import org.eclipse.wst.xml.search.editor.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.references.XMLReferencePathResult;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.util.XMLQuerySpecificationUtil;
import org.eclipse.wst.xml.search.ui.util.DOMUtils;
import org.eclipse.wst.xml.search.ui.util.SearchUtil;

/**
 * 
 * Handler launched with Ctrl+Shift+G to start search which retrieve referenced
 * nodes of the selected node.
 * 
 */
public class ReferencesInContainerHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

		// 1) Get text editor which has started the search.
		ITextEditor textEditor = SearchUtil.getTextEditor(event);
		if (textEditor == null) {
			showOperationUnavailableDialog(
					XMLSearchEditorPlugin.getActiveWorkbenchShell(),
					Messages.ReferencesInContainerHandler_operationUnavailable_textEditorUnavailable);
			return null;
		}

		// 2) Get file which has started the search.
		Shell shell = textEditor.getSite().getShell();
		IFile file = SearchUtil.getFile(textEditor);
		if (file == null) {
			showOperationUnavailableDialog(
					XMLSearchEditorPlugin.getActiveWorkbenchShell(),
					Messages.ReferencesInContainerHandler_operationUnavailable_fileUnavailable);
			return null;
		}

		// 3) Get selected node which has started the search.
		IDOMNode selectedNode = DOMUtils.getSelectedNode(textEditor);
		if (selectedNode == null) {
			showOperationUnavailableDialog(
					XMLSearchEditorPlugin.getActiveWorkbenchShell(),
					Messages.ReferencesInContainerHandler_operationUnavailable_domNodeUnavailable);
			return null;
		}

		// 3) Get list of XML reference path to execute to retrieve referenced
		// nodes of the selected node.
		XMLReferencePathResult result = XMLReferencesUtil.getReferencePath(
				selectedNode, file);
		if (result == null) {
			showOperationUnavailableDialog(
					XMLSearchEditorPlugin.getActiveWorkbenchShell(),
					Messages.ReferencesInContainerHandler_operationUnavailable_noReferencesDefined);
			return null;
		}

		IXMLQuerySpecificationRegistry querySpecificationRegistry = new XMLQuerySpecificationRegistry(
				file, selectedNode);
		for (IXMLReferencePath referencePath : result) {
			IXMLQuerySpecification querySpecification = XMLQuerySpecificationUtil
					.getQuerySpecification(referencePath);
			if (querySpecification != null) {
				String query = referencePath.getQuery(selectedNode, null,
						querySpecification.getEqualsStringQueryBuilder(),
						result.isReversed());
				if (query != null) {
					querySpecificationRegistry.register(querySpecification,
							query, null);
				}
			}
		}

		// will return true except for debugging purposes.
		try {
			SearchUtil.performNewSearch(shell, querySpecificationRegistry,
					XMLSearchReporterManager.getDefault());
		} catch (InterruptedException e) {
			// cancelled

		} catch (CoreException ex) {
			ExceptionHandler.handle(ex, shell,
					Messages.Search_Error_search_notsuccessful_title,
					Messages.Search_Error_search_notsuccessful_message);
		}

		return null;
	}

	private void showOperationUnavailableDialog(Shell shell, String message) {
		MessageDialog
				.openInformation(
						shell,
						Messages.ReferencesInContainerHandler_operationUnavailable_title,
						message);
	}
}
