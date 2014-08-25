package org.eclipse.wst.xml.search.editor.searchers.java;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceToJava;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.core.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.internal.contentassist.JavaCompletionUtils;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.JavaElementHyperlink;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;

@SuppressWarnings("restriction")
public class XMLAssistSearcherForJava implements IXMLAssistSearcher{
	public static final IXMLAssistSearcher INSTANCE = new XMLAssistSearcherForJava();

	// ------------------- Completions

	/**
	 * 
	 */
	public void searchForCompletion(Object selectedNode,
			String mathingString, String forceBeforeText, String forceEndText,
			IFile file, IXMLReferenceTo referenceTo,
			IContentAssistProposalRecorder recorder) {
		IXMLReferenceToJava referenceToJava = (IXMLReferenceToJava) referenceTo;
		// Find class from
		IType[] classes = referenceToJava.getExtends(selectedNode, file);
		if (classes != null) {
			for (int i = 0; i < classes.length; i++) {
				JavaCompletionUtils.addTypeHierachyAttributeValueProposals(
						mathingString, file, recorder, classes[i], 12);
			}
		} else {
			JavaCompletionUtils.addClassValueProposals(mathingString, file,
					recorder);
		}
	}

	// ------------------- Hyperlinks

	public void searchForHyperlink(Object selectedNode, int offset,
			String mathingString, int startOffset, int endOffset, IFile file,
			IXMLReferenceTo referenceTo, IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, ITextEditor textEditor) {
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		if (type != null) {
			hyperLinks.add(new JavaElementHyperlink(hyperlinkRegion, type));
		}
	}

	// ----------------- Text info

	public String searchForTextHover(Object selectedNode, int offset,
			String mathingString, int startIndex, int endIndex, IFile file,
			IXMLReferenceTo referenceTo) {
		IType type = JdtUtils.getJavaType(file.getProject(), mathingString);
		if (type != null && type instanceof IMember) {
			return new ProposalInfo((IMember) type).getInfo(EditorUtils
					.getProgressMonitor());
		}
		return null;
	}
}
