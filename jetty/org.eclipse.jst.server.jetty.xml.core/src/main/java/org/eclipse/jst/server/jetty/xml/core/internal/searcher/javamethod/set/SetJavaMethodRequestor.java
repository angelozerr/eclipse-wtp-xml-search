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
package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod.set;

import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.BeansJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.IJavaMethodRequestor;

public class SetJavaMethodRequestor extends BeansJavaMethodRequestor {

	public static final IJavaMethodRequestor INSTANCE = new SetJavaMethodRequestor();

	public SetJavaMethodRequestor() {
		super(SET_PREFIX, false);
	}

}
