package org.eclipse.wst.xml.search.editor.java.hyperlink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.editor.core.java.IJavaReference;
import org.eclipse.wst.xml.search.editor.core.java.JavaReferencesManager;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToExpression;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper.StringArgument;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.searchers.XMLAssistSearcherBindingsManager;

public class Java2XHyperLinkDetectetor extends AbstractHyperlinkDetector {

	public IHyperlink[] detectHyperlinks(final ITextViewer textViewer,
			final IRegion region, final boolean canShowMultipleHyperlinks) {
		if ((region == null) || (textViewer == null)) {
			return null;
		}
		final int offset = region.getOffset();

		final IDocument document = textViewer.getDocument();
		if (document == null) {
			return null;
		}
		final JavaEditor editor = (JavaEditor) getAdapter(JavaEditor.class);
		Assert.isNotNull(editor);

		ICompilationUnit compilationUnit = (ICompilationUnit)EditorUtility.getEditorInputJavaElement(
				editor, false);
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
		List<IHyperlink> hyperLinks = new ArrayList<IHyperlink>();
		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);

		StringArgument stringArgument = DocumentHelper.findStringArgument(
				document, offset, true);
		if (stringArgument == null) {
			return null;
		}
		IRegion hyperlinkRegion = stringArgument.getRegion();
		String matchingString = stringArgument.getMatchingString();
		if (reference.isExpression()) {
			IXMLReferenceToExpression expression = (IXMLReferenceToExpression) reference;
			IXMLAssistSearcher searcher = XMLAssistSearcherBindingsManager
			                .getDefault().getXMLAssistSearcher((IXMLReference)expression);
			if (searcher != null) {
				searcher.searchForHyperlink(selectedNode, offset,
						matchingString, -1, -1, file, expression,
						hyperlinkRegion, hyperLinks, textEditor);
			}
		} else {

			Collection<IXMLReferenceTo> toPath = reference.getTo();
			for (IXMLReferenceTo referenceTo : toPath) {
				IXMLAssistSearcher searcher = XMLAssistSearcherBindingsManager
				                .getDefault().getXMLAssistSearcher(referenceTo);
				if (searcher != null) {
					searcher.searchForHyperlink(selectedNode, offset,
							matchingString, -1, -1, file, referenceTo,
							hyperlinkRegion, hyperLinks, textEditor);
				}
			}
		}
		if (hyperLinks.size() == 0) {
			return null;
		}
		return hyperLinks.toArray(new IHyperlink[hyperLinks.size()]);
	}

	public static IRegion findStringArgumentInJava(final IDocument document,
			final int offset) {
		try {
			final IRegion li = document.getLineInformationOfOffset(offset);
			for (int i = offset - 1; i >= li.getOffset(); i--) {
				if (document.get(i, 1).equals("\"")) {
					for (int j = offset; j <= li.getOffset() + li.getLength(); j++) {
						if (document.get(j, 1).equals("\"")) {
							return new Region(i + 1, j - i - 1);
						}
					}
				}
			}
		} catch (final BadLocationException e) {
		}
		return null;
	}
}
