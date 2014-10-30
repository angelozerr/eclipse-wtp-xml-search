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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.storage.StructuredStorageModelManager;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;

public class EditorUtils {

	public static final IProgressMonitor getProgressMonitor() {
		IWorkbenchWindow activeWorkbenchWindow = XMLSearchEditorPlugin
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return new NullProgressMonitor();
		IEditorPart editor = activeWorkbenchWindow.getActivePage()
				.getActiveEditor();
		if (editor != null
				&& editor.getEditorSite() != null
				&& editor.getEditorSite().getActionBars() != null
				&& editor.getEditorSite().getActionBars()
						.getStatusLineManager() != null
				&& editor.getEditorSite().getActionBars()
						.getStatusLineManager().getProgressMonitor() != null) {
			IStatusLineManager manager = editor.getEditorSite().getActionBars()
					.getStatusLineManager();
			IProgressMonitor monitor = manager.getProgressMonitor();
			manager.setMessage("Processing completion proposals");
			manager.setCancelEnabled(true);
			return monitor;
		} else {
			return new NullProgressMonitor();
		}
	}

	public static String prepareMatchString(ContentAssistRequest request) {
		String matchString = request.getMatchString();
		return prepareMatchString(matchString);
	}

	public static String prepareMatchString(String matchString) {
		if (matchString == null)
			matchString = "";
		if (matchString.length() > 0
				&& (matchString.startsWith("\"") || matchString.startsWith("'")))
			matchString = matchString.substring(1);
		return matchString;
	}

	public static IFile getFile(ContentAssistRequest request) {
		if (request == null) {
			return null;
		}

		IStructuredDocumentRegion region = request.getDocumentRegion();
		if (region == null) {
			return null;
		}

		IDocument document = region.getParentDocument();
		return DOMUtils.getFile(document);
	}

	public static IEditorPart openInEditor(IDOMNode node) {
		IStorage storage = StructuredStorageModelManager.getModelManager()
				.getStorage(node.getModel());
		if (storage != null) {
			return openStorageInEditor(node, storage);
		}
		return openFileInEditor(node, node.getModel().getBaseLocation());
	}

	private static IEditorPart openFileInEditor(IDOMNode node,
			String baseLocation) {
		IFile xmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path(baseLocation));
		if (xmlFile.exists()) {
			return openInEditor(xmlFile, node.getStartOffset(), node
					.getLength(), true);
		}
		return null;
	}

	public static IEditorPart openInEditor(IFile file, int start, int length,
			boolean activate) {
		IEditorPart editor = null;
		IWorkbenchPage page = XMLSearchEditorPlugin.getActivePage();
		try {
			if (start > 0) {
				editor = IDE.openEditor(page, file, activate);
				ITextEditor textEditor = null;
				if (editor instanceof ITextEditor)
					textEditor = (ITextEditor) editor;
				else if (editor instanceof IAdaptable)
					textEditor = (ITextEditor) editor
							.getAdapter(ITextEditor.class);
				if (textEditor != null) {
					IDocument document = textEditor.getDocumentProvider()
							.getDocument(editor.getEditorInput());
					// int start = document.getLineOffset(line - 1);
					textEditor.selectAndReveal(start, length);
					page.activate(editor);
				} else {
					IMarker marker = file
							.createMarker("org.eclipse.core.resources.textmarker");
					marker.setAttribute("lineNumber", start);
					editor = IDE.openEditor(page, marker, activate);
					marker.delete();
				}
			} else {
				editor = IDE.openEditor(page, file, activate);
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, e.getMessage(), e);
		}
		return editor;
	}

	private static IEditorPart openStorageInEditor(IDOMNode node,
			IStorage storage) {
		return openInEditor(storage, node.getStartOffset(), node.getLength(),
				true);
	}

	public static IEditorPart openInEditor(IStorage storage, int start,
			int length, boolean activate) {
		IEditorPart editor = null;
		try {
			editor = EditorUtility.openInEditor(storage, activate);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (editor == null) {
			return null;
		}
		if (start > 0) {
			ITextEditor textEditor = null;
			if (editor instanceof ITextEditor)
				textEditor = (ITextEditor) editor;
			else if (editor instanceof IAdaptable)
				textEditor = (ITextEditor) editor.getAdapter(ITextEditor.class);
			if (textEditor != null) {
				IDocument document = textEditor.getDocumentProvider()
						.getDocument(editor.getEditorInput());
				// int start = document.getLineOffset(line - 1);
				textEditor.selectAndReveal(start, length);
				// page.activate(editor);
			} else {
				// IMarker marker = file
				// .createMarker("org.eclipse.core.resources.textmarker");
				// marker.setAttribute("lineNumber", start);
				// editor = IDE.openEditor(page, marker, activate);
				// marker.delete();
			}
		} else {
			// editor = IDE.openEditor(page, file, activate);
		}

		return editor;
	}

}
