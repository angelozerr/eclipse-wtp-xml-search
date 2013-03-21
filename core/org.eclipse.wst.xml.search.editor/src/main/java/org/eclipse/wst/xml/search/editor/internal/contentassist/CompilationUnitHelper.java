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
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import org.eclipse.jdt.core.WorkingCopyOwner;

class CompilationUnitHelper {

	private CompilationProblemRequestor fProblemRequestor;
	private WorkingCopyOwner fWorkingCopyOwner;
	private static CompilationUnitHelper instance;

	private CompilationUnitHelper() {
		fProblemRequestor = null;
		fWorkingCopyOwner = null;
	}

	public static final synchronized CompilationUnitHelper getInstance() {
		if (instance == null)
			instance = new CompilationUnitHelper();
		return instance;
	}

	public CompilationProblemRequestor getProblemRequestor() {
		if (fProblemRequestor == null)
			fProblemRequestor = new CompilationProblemRequestor();
		return fProblemRequestor;
	}

	public WorkingCopyOwner getWorkingCopyOwner() {
		if (fWorkingCopyOwner == null)
			fWorkingCopyOwner = new WorkingCopyOwner() {

				public String toString() {
					return "WTP/XML Search Working copy owner";
				}

				final CompilationUnitHelper this$0;
				{
					this$0 = CompilationUnitHelper.this;
				}
			};
		return fWorkingCopyOwner;
	}

}
