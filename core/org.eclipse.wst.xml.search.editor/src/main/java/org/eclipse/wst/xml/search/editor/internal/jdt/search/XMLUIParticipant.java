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
package org.eclipse.wst.xml.search.editor.internal.jdt.search;

import org.eclipse.jdt.ui.search.IMatchPresentation;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.ui.DecoratingXMLSearchLabelProvider;
import org.eclipse.wst.xml.search.ui.XMLLabelProvider;
import org.eclipse.wst.xml.search.ui.util.EditorOpener;

public class XMLUIParticipant implements IMatchPresentation {

	private EditorOpener fEditorOpener = new EditorOpener();

	public ILabelProvider createLabelProvider() {
		return new DecoratingXMLSearchLabelProvider((new XMLLabelProvider()));
	}

	public void showMatch(Match match, int currentOffset, int currentLength,
			boolean activate) throws PartInitException {
		Object o = match.getElement();
		if (o instanceof IDOMNode) {
			if (activate) {
				IDOMNode node = (IDOMNode) o;
				EditorOpener.openDOMNode(XMLSearchEditorPlugin.getActivePage(),
						node, fEditorOpener,
						XMLSearchEditorPlugin.getActiveWorkbenchShell());
			}
		}

	}

}
