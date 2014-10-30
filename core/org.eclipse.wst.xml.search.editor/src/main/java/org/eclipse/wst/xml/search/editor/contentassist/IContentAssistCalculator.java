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
package org.eclipse.wst.xml.search.editor.contentassist;

public interface IContentAssistCalculator {

	/**
	 * 
	 * Calculate content assist proposals under the given context.
	 * 
	 * @param context
	 *            the current context of the content assist proposal request
	 * 
	 * @param recorder
	 *            the recorder to record calculated proposals
	 */

	void computeProposals(IContentAssistContext context,
			IContentAssistProposalRecorder recorder);

}
