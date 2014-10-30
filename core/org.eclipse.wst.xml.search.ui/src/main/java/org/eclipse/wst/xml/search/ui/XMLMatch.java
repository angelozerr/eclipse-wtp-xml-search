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
package org.eclipse.wst.xml.search.ui;

import org.eclipse.search.ui.text.Match;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * Search Match extension for DOM-SSE Node {@link IDOMNode}.
 * 
 */
public class XMLMatch extends Match {

	public XMLMatch(IDOMNode node) {
		super(node, node.getStartOffset(), node.getLength());
	}

}
