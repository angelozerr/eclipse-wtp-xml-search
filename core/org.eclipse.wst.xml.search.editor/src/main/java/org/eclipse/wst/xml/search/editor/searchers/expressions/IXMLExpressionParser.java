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
package org.eclipse.wst.xml.search.editor.searchers.expressions;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToExpression;
import org.w3c.dom.Node;

public interface IXMLExpressionParser {

	SearcherToken parse(String nodeValue, String matchingString,
			IXMLReferenceToExpression toExpression);
	
	SearcherToken[] parse(String nodeValue, IXMLReferenceToExpression toExpression);

	SearcherToken parse(String nodeValue, int offset,
			IXMLReferenceToExpression toExpression);

	
}
