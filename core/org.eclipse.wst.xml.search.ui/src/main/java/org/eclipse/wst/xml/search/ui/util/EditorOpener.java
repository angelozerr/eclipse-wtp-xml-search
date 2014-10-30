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
package org.eclipse.wst.xml.search.ui.util;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.ui.internal.Messages;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchUIPlugin;

public class EditorOpener {

	private IEditorReference fReusedEditor;

	public static void openDOMNode(IWorkbenchPage wbPage, IDOMNode node,
			EditorOpener editorOpener, Shell shell) {
		try {
			// Search result item clicked is DOM node, open the XML editor
			// and select the node as soon as the XML editor is opened.
			IFile file = DOMUtils.getFile(node);
			editorOpener.openAndSelect(wbPage, file, node.getStartOffset(),
					node.getLength(), false);
		} catch (PartInitException e) {
			ErrorDialog.openError(shell,
					Messages.XMLSearchPage_open_file_dialog_title,
					Messages.XMLSearchPage_open_file_failed, e.getStatus());
		}
	}

	public IEditorPart open(IWorkbenchPage wbPage, IFile file, boolean activate)
			throws PartInitException {
		if (NewSearchUI.reuseEditor())
			return showWithReuse(file, wbPage, getEditorID(file), activate);
		return showWithoutReuse(file, wbPage, getEditorID(file), activate);
	}

	public IEditorPart openAndSelect(IWorkbenchPage wbPage, IFile file,
			int offset, int length, boolean activate) throws PartInitException {
		String editorId = null;
		IEditorDescriptor desc = IDE.getEditorDescriptor(file);
		if (desc == null || !desc.isInternal()) {
			editorId = "org.eclipse.ui.DefaultTextEditor"; //$NON-NLS-1$
		} else {
			editorId = desc.getId();
		}

		IEditorPart editor;
		if (NewSearchUI.reuseEditor()) {
			editor = showWithReuse(file, wbPage, editorId, activate);
		} else {
			editor = showWithoutReuse(file, wbPage, editorId, activate);
		}

		if (editor instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) editor;
			textEditor.selectAndReveal(offset, length);
		} else if (editor != null) {
			showWithMarker(editor, file, offset, length);
		}
		return editor;
	}

	private IEditorPart showWithoutReuse(IFile file, IWorkbenchPage wbPage,
			String editorID, boolean activate) throws PartInitException {
		return IDE.openEditor(wbPage, file, editorID, activate);
	}

	private String getEditorID(IFile file) throws PartInitException {
		IEditorDescriptor desc = IDE.getEditorDescriptor(file);
		if (desc == null)
			return XMLSearchUIPlugin.getDefault().getWorkbench()
					.getEditorRegistry()
					.findEditor(IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID)
					.getId();
		return desc.getId();
	}

	private IEditorPart showWithReuse(IFile file, IWorkbenchPage page,
			String editorId, boolean activate) throws PartInitException {
		IEditorInput input = new FileEditorInput(file);
		IEditorPart editor = page.findEditor(input);
		if (editor != null) {
			page.bringToTop(editor);
			if (activate) {
				page.activate(editor);
			}
			return editor;
		}
		IEditorReference reusedEditorRef = fReusedEditor;
		if (reusedEditorRef != null) {
			boolean isOpen = reusedEditorRef.getEditor(false) != null;
			boolean canBeReused = isOpen && !reusedEditorRef.isDirty()
					&& !reusedEditorRef.isPinned();
			if (canBeReused) {
				boolean showsSameInputType = reusedEditorRef.getId().equals(
						editorId);
				if (!showsSameInputType) {
					page.closeEditors(
							new IEditorReference[] { reusedEditorRef }, false);
					fReusedEditor = null;
				} else {
					editor = reusedEditorRef.getEditor(true);
					if (editor instanceof IReusableEditor) {
						((IReusableEditor) editor).setInput(input);
						page.bringToTop(editor);
						if (activate) {
							page.activate(editor);
						}
						return editor;
					}
				}
			}
		}
		editor = page.openEditor(input, editorId, activate);
		if (editor instanceof IReusableEditor) {
			IEditorReference reference = (IEditorReference) page
					.getReference(editor);
			fReusedEditor = reference;
		} else {
			fReusedEditor = null;
		}
		return editor;
	}

	private void showWithMarker(IEditorPart editor, IFile file, int offset,
			int length) throws PartInitException {
		IMarker marker = null;
		try {
			marker = file.createMarker(NewSearchUI.SEARCH_MARKER);
			HashMap attributes = new HashMap(4);
			attributes.put(IMarker.CHAR_START, new Integer(offset));
			attributes.put(IMarker.CHAR_END, new Integer(offset + length));
			marker.setAttributes(attributes);
			IDE.gotoMarker(editor, marker);
		} catch (CoreException e) {
			throw new PartInitException(Messages.XMLSearchPage_error_marker, e);
		} finally {
			if (marker != null)
				try {
					marker.delete();
				} catch (CoreException e) {
					// ignore
				}
		}
	}

}