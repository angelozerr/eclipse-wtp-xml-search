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
package org.eclipse.wst.xml.search.editor.java.hover;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper.StringArgument;
import org.eclipse.wst.xml.search.editor.java.IJavaReference;
import org.eclipse.wst.xml.search.editor.java.JavaReferencesManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.searchers.IXMLSearcher;

public class Java2XHover implements IJavaEditorTextHover {

	private IEditorPart editor;
	
	public String getHoverInfo(final ITextViewer textViewer, final IRegion region) {
		Assert.isNotNull(textViewer);
		Assert.isNotNull(region);

		final int offset = region.getOffset();

		final IDocument document = textViewer.getDocument();
		if (document == null) {
			return null;
		}

		if (editor == null) {
			return null;
		}

		ITypeRoot typeRoot = EditorUtility.getEditorInputJavaElement(editor, false);
		ICompilationUnit compilationUnit =
			typeRoot instanceof ICompilationUnit ? (ICompilationUnit)typeRoot : null;

		if (compilationUnit == null) {
			return null;
		}

		IFile file = null;
		IJavaElement selectedNode = null;
		try {
			selectedNode = compilationUnit
					.getElementAt(offset);
			file = (IFile) compilationUnit.getCorrespondingResource();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		if (selectedNode == null) {
			return null;
		}

		
		IJavaReference reference = JavaReferencesManager.getInstance()
				.getXMLReference(selectedNode, null);
		if (reference == null) {
			return null;
		} 
		StringArgument stringArgument = DocumentHelper.findStringArgument(
				document, offset, true);
		if (stringArgument == null) {
			return null;
		}
		
		StringBuilder infos = null;
		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLSearcher searcher = expression.getSearcher();
			if (searcher != null) {
				String textInfo = searcher.searchForTextHover(selectedNode,
						-1, stringArgument.getMatchingString(), -1, -1, file, expression);
				infos = getTextHover(infos, textInfo);
			}
		} else {
			Collection<IXMLReferenceTo> to = reference.getTo();
			for (IXMLReferenceTo referenceTo : to) {
				IXMLSearcher searcher = referenceTo.getSearcher();
				if (searcher != null) {
					String textInfo = searcher.searchForTextHover(
							selectedNode, -1, stringArgument.getMatchingString(), -1, -1, file,
							referenceTo);
					infos = getTextHover(infos, textInfo);
				}
			}
		}

		if (infos != null && infos.length() > 0) {
			return infos.toString();
		}
		return null;
	}

	public IRegion getHoverRegion(final ITextViewer textViewer, final int offset) {
		return null;
	}

	public void setEditor(IEditorPart paramIEditorPart) {
		editor = paramIEditorPart;		
	}

	private StringBuilder getTextHover(StringBuilder infos, String textInfo) {
		if (!StringUtils.isEmpty(textInfo)) {
			if (infos == null) {
				infos = new StringBuilder();
			} else {
				infos.append("<br /><br />");
			}
			infos.append(textInfo);
		}
		return infos;
	}
}
