package org.eclipse.wst.xml.search.editor.internal.jdt.search2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.java.JavaNoTypeCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.xml.search.editor.core.java.IJavaReference;
import org.eclipse.wst.xml.search.editor.core.java.JavaReferencesManager;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper;
import org.eclipse.wst.xml.search.editor.internal.util.DocumentHelper.StringArgument;
import org.eclipse.wst.xml.search.editor.searchers.IXMLAssistSearcher;
import org.eclipse.wst.xml.search.editor.searchers.XMLAssistSearcherBindingsManager;
;

public class SearchJavaNoTypeCompletionProposalComputer extends
		JavaNoTypeCompletionProposalComputer {

	@Override
	public List<ICompletionProposal> computeCompletionProposals(
			final ContentAssistInvocationContext context,
			final IProgressMonitor monitor) {
		List<ICompletionProposal> proposals = null;
		if (context instanceof JavaContentAssistInvocationContext) {
			final JavaContentAssistInvocationContext jcontext = (JavaContentAssistInvocationContext) context;
			try {
				int offset = jcontext.getInvocationOffset();
				IJavaElement element = jcontext.getCompilationUnit()
						.getElementAt(offset);
				if (element != null) {

					IJavaReference javaReference = JavaReferencesManager
							.getInstance().getXMLReference(element, null);
					if (javaReference != null) {

						IDocument document = jcontext.getDocument();
						StringArgument stringArgument = DocumentHelper
								.findStringArgument(document, offset, false);

						if (stringArgument != null) {

							IRegion region = stringArgument.getRegion();
							String matchingString = stringArgument
									.getMatchingString();
							if (proposals == null) {
								proposals = new ArrayList<ICompletionProposal>();
							}

							IResource resource = jcontext.getCompilationUnit()
									.getCorrespondingResource();
							IFile file = (resource != null && resource
									.getType() == IResource.FILE) ? (IFile) resource
									: null;
							List<IXMLReferenceTo> tos = javaReference.getTo();
							for (IXMLReferenceTo to : tos) {
								IXMLAssistSearcher searcher = XMLAssistSearcherBindingsManager.
								                getDefault().getXMLAssistSearcher(to);
								if (searcher != null) {
									searcher.searchForCompletion(
											element,
											matchingString.toString(),
											null,
											null,
											file,
											to,
											new JavaContentAssistProposalRecorder(
													region, proposals));
								}
							}
						}
					}
				}

			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		if (proposals != null) {
			return proposals;
		}
		return Collections.emptyList();
	}

	private String getMatchingString(final IDocument document, final int offset) {
		StringBuilder s = new StringBuilder("");
		try {
			final IRegion li = document.getLineInformationOfOffset(offset);
			for (int i = offset - 1; i >= li.getOffset(); i--) {
				if (document.get(i, 1).equals("\"")) {
					break;
				}
				s.insert(0, document.get(i, 1));
			}
		} catch (final BadLocationException e) {
		}
		return s.toString();
	}

}
