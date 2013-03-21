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
package org.eclipse.wst.xml.search.core.statics;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class StaticValueSearchEngine implements IStaticValueSearchEngine {

	private static IStaticValueSearchEngine INSTANCE = new StaticValueSearchEngine();

	public IStatus search(Object selectedNode, IFile file, IStaticValueVisitor visitor, IStaticValueCollector collector,
			String matching, boolean startsWith, IProgressMonitor monitor) {
		visitor.visit(selectedNode, file, matching, startsWith, collector);
		return null;
	}

	public static IStaticValueSearchEngine getDefault() {
		return INSTANCE;
	}

}
