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
package org.eclipse.wst.xml.search.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.eclipse.wst.xml.search.ui.participant.IMatchPresentation;

/**
 * {@link AbstractTextSearchResult} implementation for DOM-SSE.
 * 
 */
public class XMLSearchResult extends AbstractTextSearchResult implements
		IEditorMatchAdapter, IFileMatchAdapter {

	protected static final Match[] NO_MATCHES = new Match[0];

	private XMLSearchQuery fQuery;
	private final Map<Object, IMatchPresentation> fElementsToParticipants;

	public XMLSearchResult(XMLSearchQuery query) {
		fQuery = query;
		fElementsToParticipants = new HashMap<Object, IMatchPresentation>();
	}

	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		return this;
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		return this;
	}

	public ImageDescriptor getImageDescriptor() {
		return fQuery.getImageDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.search.ui.ISearchResult#getLabel()
	 */
	public String getLabel() {
		return fQuery.getResultLabel(getMatchCount());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.search.ui.ISearchResult#getTooltip()
	 */
	public String getTooltip() {
		return getLabel();
	}

	public ISearchQuery getQuery() {
		return fQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.search.ui.text.IEditorMatchAdapter#computeContainedMatches
	 * (org.eclipse.search.ui.text.AbstractTextSearchResult,
	 * org.eclipse.ui.IEditorPart)
	 */
	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IEditorPart editor) {
		// IStructuredModel editor.getAdapter(IStructuredModel.class);
		return computeContainedMatches(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.search.ui.text.IEditorMatchAdapter#computeContainedMatches
	 * (org.eclipse.search.ui.text.AbstractTextSearchResult,
	 * org.eclipse.ui.IEditorPart)
	 */
	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IFile file) {
		return computeContainedMatches(file);
	}

	private Match[] computeContainedMatches(IAdaptable adaptable) {
		// IJavaElement javaElement= (IJavaElement)
		// adaptable.getAdapter(IJavaElement.class);
		// Set matches= new HashSet();
		// if (javaElement != null) {
		// collectMatches(matches, javaElement);
		// }
		// IFile file= (IFile) adaptable.getAdapter(IFile.class);
		// if (file != null) {
		// collectMatches(matches, file);
		// }
		// if (!matches.isEmpty()) {
		// return (Match[]) matches.toArray(new Match[matches.size()]);
		// }
		// adaptable.getAdapter(IStructuredDocument.class)
		return NO_MATCHES;
	}

	public boolean isShownInEditor(Match match, IEditorPart editor) {
		Object element = match.getElement();
		if (element instanceof IDOMNode) {
			// DOMNode matched
			IDOMNode node = (IDOMNode) element;
			IStructuredModel editorModel = (IStructuredModel) editor
					.getAdapter(IStructuredModel.class);
			if (editorModel != null) {
				// Returns true if found node belong to the current XML editor
				// which
				// has launched the search and false otherwise.
				return editorModel.equals(node.getModel());
			}
		}
		return false;
	}

	public IFile getFile(Object element) {
		if (element instanceof IFile)
			return (IFile) element;
		if (element instanceof IDOMNode) {
			// Returns owner file of the node
			IDOMNode node = (IDOMNode) element;
			return DOMUtils.getFile(node);
		}
		return null;
	}

	public synchronized IMatchPresentation getSearchParticpant(Object element) {
		return fElementsToParticipants.get(element);
	}

	boolean addMatch(Match match, IMatchPresentation participant) {
		Object element = match.getElement();
		if (fElementsToParticipants.get(element) != null) {
			// TODO must access the participant id / label to properly report
			// the error.
			XMLSearchUIPlugin
					.log(new Status(
							IStatus.WARNING,
							XMLSearchUIPlugin.PLUGIN_ID,
							0,
							"A second search participant was found for an element", null)); //$NON-NLS-1$
			return false;
		}
		fElementsToParticipants.put(element, participant);
		addMatch(match);
		return true;
	}
}
