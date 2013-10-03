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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.ui.DecoratingXMLSearchLabelProvider;
import org.eclipse.wst.xml.search.ui.XMLLabelProvider;
import org.eclipse.wst.xml.search.ui.participant.IMatchPresentation;
import org.eclipse.wst.xml.search.ui.util.EditorOpener;

/**
 * {@link AbstractTextSearchViewPage} implementation for DOM-SSE.
 */
public class XMLSearchResultPage extends AbstractTextSearchViewPage {

	private static final int DEFAULT_ELEMENT_LIMIT = 1000;

	private IXMLSearchContentProvider fContentProvider;

	private EditorOpener fEditorOpener = new EditorOpener();

	public XMLSearchResultPage() {
		super.setElementLimit(new Integer(DEFAULT_ELEMENT_LIMIT));
	}

	public static class DecoratorIgnoringViewerSorter extends ViewerComparator {
		private final ILabelProvider fLabelProvider;

		public DecoratorIgnoringViewerSorter(ILabelProvider labelProvider) {
			fLabelProvider = labelProvider;
		}

		public int category(Object element) {
			if (element instanceof IDOMNode || element instanceof IResource)
				return 1;
			return 2;
		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			int cat1 = category(e1);
			int cat2 = category(e2);

			if (cat1 != cat2) {
				return cat1 - cat2;
			}
			String name1 = fLabelProvider.getText(e1);
			String name2 = fLabelProvider.getText(e2);
			if (name1 == null)
				name1 = "";//$NON-NLS-1$
			if (name2 == null)
				name2 = "";//$NON-NLS-1$
			return getComparator().compare(name1, name2);
		}
	}

	@Override
	protected void configureTableViewer(TableViewer viewer) {
		viewer.setUseHashlookup(true);
		XMLLabelProvider innerLabelProvider = new XMLLabelProvider(this);
		viewer.setLabelProvider(new DecoratingXMLSearchLabelProvider(
				innerLabelProvider));
		viewer.setContentProvider(new XMLSearchTableContentProvider(this));
		viewer.setComparator(new DecoratorIgnoringViewerSorter(
				innerLabelProvider));
		fContentProvider = (XMLSearchTableContentProvider) viewer
				.getContentProvider();
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		viewer.setUseHashlookup(true);
		XMLLabelProvider innerLabelProvider = new XMLLabelProvider(this);
		viewer.setLabelProvider(new DecoratingXMLSearchLabelProvider(
				innerLabelProvider));
		viewer.setContentProvider(new XMLSearchTreeContentProvider(this, viewer));
		viewer.setComparator(new DecoratorIgnoringViewerSorter(
				innerLabelProvider));
		fContentProvider = (IXMLSearchContentProvider) viewer
				.getContentProvider();
		addDragAdapters(viewer);
	}

	@Override
	protected void elementsChanged(Object[] objects) {
		if (fContentProvider != null)
			fContentProvider.elementsChanged(objects);
	}

	@Override
	protected void clear() {
		if (fContentProvider != null)
			fContentProvider.clear();
	}

	private void addDragAdapters(StructuredViewer viewer) {
		// Transfer[] transfers = new Transfer[] {
		// ResourceTransfer.getInstance() };
		// int ops = DND.DROP_COPY | DND.DROP_LINK;
		// viewer.addDragSupport(ops, transfers, new
		// NavigatorDragAdapter(viewer));
	}

	public StructuredViewer getViewer() {
		return super.getViewer();
	}


	@Override
	public void showMatch(Match match, int offset, int length, boolean activate) throws PartInitException {

		Object element= match.getElement();
		if (element instanceof IDOMNode) {
			IDOMNode node = (IDOMNode) element;
			EditorOpener.openDOMNode(getSite().getPage(), node,
					fEditorOpener, getSite().getShell());
		} else 	if (getInput() instanceof XMLSearchResult) {
			XMLSearchResult result= (XMLSearchResult) getInput();
			IMatchPresentation participant= result.getSearchParticpant(element);
			if (participant != null)
				participant.showMatch(match, offset, length, activate);
		}
	}

	@Override
	protected void handleOpen(OpenEvent event) {
		Object firstElement = ((IStructuredSelection) event.getSelection())
				.getFirstElement();
		if (firstElement instanceof IDOMNode) {
			if (getDisplayedMatchCount(firstElement) == 0) {
				// Search result item clicked is DOM node, open the XML editor
				// and select the node as soon as the XML editor is opened.
				IDOMNode node = (IDOMNode) firstElement;
				EditorOpener.openDOMNode(getSite().getPage(), node,
						fEditorOpener, getSite().getShell());
				return;
			}
		}
		super.handleOpen(event);
	}

}
