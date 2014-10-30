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
package org.eclipse.wst.xml.search.core;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * A collector to add DOM node {@link IDOMNode} retrieved by the XML search
 * engine.
 * 
 */
public interface IXMLSearchDOMNodeCollector {

	/**
	 * Appends the specified node.
	 * 
	 * @param node the node to add.
	 * @return
	 */
	boolean add(IDOMNode node);
}
