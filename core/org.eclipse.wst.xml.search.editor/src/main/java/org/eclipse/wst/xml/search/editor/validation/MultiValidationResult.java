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
package org.eclipse.wst.xml.search.editor.validation;

import java.util.ArrayList;
import java.util.List;

public class MultiValidationResult extends ArrayList<IValidationResult>
		implements IMultiValidationResult {

	@Override
	public boolean add(IValidationResult result) {
		if (result != null) {
			super.add(result);
		}
		return false;
	}

	public int getNbElements() {
		return 0;
	}

	public void setNbElements(int nbElements) {

	}

	public String getValue() {
		return null;
	}

	public int getStartIndex() {
		return -1;
	}

	public int getEndIndex() {
		return -1;
	}

	public boolean isMulti() {
		return true;
	}

	public List<IValidationResult> getResults() {
		return this;
	}
}
