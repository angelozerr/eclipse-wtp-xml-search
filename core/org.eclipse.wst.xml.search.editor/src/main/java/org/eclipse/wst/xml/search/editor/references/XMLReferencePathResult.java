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
package org.eclipse.wst.xml.search.editor.references;

import java.util.ArrayList;
import java.util.Collection;

public class XMLReferencePathResult extends ArrayList<IXMLReferencePath> {

	private final boolean reversed;

	public XMLReferencePathResult(int size, boolean reversed) {
		super(size);
		this.reversed = reversed;
	}

	public XMLReferencePathResult(
			Collection<IXMLReferencePath> referencesByPathTo, boolean reversed) {
		super.addAll(referencesByPathTo);
		this.reversed = reversed;
	}
	
	public boolean isReversed() {
		return reversed;
	}

}
