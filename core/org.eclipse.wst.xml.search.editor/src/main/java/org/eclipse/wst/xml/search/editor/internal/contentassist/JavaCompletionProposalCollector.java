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

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;

public class JavaCompletionProposalCollector extends
		CompletionProposalCollector {

	public JavaCompletionProposalCollector(ICompilationUnit cu) {
		super(cu);

	}

	public void accept(CompletionProposal proposal) {
		boolean accepted = false;

		int flags = JavaCompletionUtils.FLAG_PACKAGE | JavaCompletionUtils.FLAG_CLASS;
		if (CompletionProposal.TYPE_REF == proposal.getKind()) {
			if ((flags & JavaCompletionUtils.FLAG_CLASS) != 0
					&& !Flags.isInterface(proposal.getFlags())) {
				super.accept(proposal);
				accepted = true;
			}
			if (!accepted
					&& (flags & JavaCompletionUtils.FLAG_INTERFACE) != 0
					&& Flags.isInterface(proposal.getFlags())) {
				super.accept(proposal);
				accepted = true;
			}
		}

		if (CompletionProposal.PACKAGE_REF == proposal.getKind()) {
			if (!accepted
					&& (flags & JavaCompletionUtils.FLAG_PACKAGE) != 0) {
				super.accept(proposal);
			}
		}
	}

}
