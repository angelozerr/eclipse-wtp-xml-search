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
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.compiler.IProblem;

public class CompilationProblemRequestor implements IProblemRequestor {

	private boolean fIsActive;
	private boolean fIsRunning;
	private List<IProblem> fCollectedProblems;

	CompilationProblemRequestor() {
		fIsActive = false;
		fIsRunning = false;
	}

	public void beginReporting() {
		fIsRunning = true;
		fCollectedProblems = new ArrayList<IProblem>();
	}

	public void acceptProblem(IProblem problem) {
		if (isActive())
			fCollectedProblems.add(problem);
	}

	public void endReporting() {
		fIsRunning = false;
	}

	public boolean isActive() {
		return fIsActive && fCollectedProblems != null;
	}

	public void setIsActive(boolean isActive) {
		if (fIsActive != isActive) {
			fIsActive = isActive;
			if (fIsActive)
				startCollectingProblems();
			else
				stopCollectingProblems();
		}
	}

	private void startCollectingProblems() {
		fCollectedProblems = new ArrayList();
	}

	private void stopCollectingProblems() {
	}

	public List getCollectedProblems() {
		return fCollectedProblems;
	}

	public boolean isRunning() {
		return fIsRunning;
	}

}
