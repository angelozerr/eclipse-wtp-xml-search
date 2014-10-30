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
package org.eclipse.wst.xml.search.ui.participant;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IQueryParticipant {

	void search(ISearchRequestor requestor, Object selectedNode,
			IProgressMonitor monitor) throws CoreException;

	/**
	 * Returns true if this query participant is enable for the given selected
	 * node and false otherwise.
	 * 
	 * @param selectedNode
	 *            true if this query participant is enable for the given
	 *            selected node and false otherwise.
	 * @return
	 */
	boolean isEnabledFor(Object selectedNode);

	/**
	 * Gets the UI participant responsible for handling the display of elements
	 * not known to the XML search UI. The XML search UI knows elements are of
	 * type <code>IDOMNode</code> and <code>IResource</code>. A participant may
	 * return <code>null</code> if matches are only reported against elements of
	 * type <code>IResource</code> and <code>IDOMNode</code>.
	 * 
	 * @return The UI participant for this query participant or
	 *         <code>null</code>.
	 */
	IMatchPresentation getUIParticipant();

}
