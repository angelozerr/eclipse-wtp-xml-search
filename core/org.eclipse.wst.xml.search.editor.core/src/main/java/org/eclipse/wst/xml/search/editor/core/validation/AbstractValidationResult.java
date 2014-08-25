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
package org.eclipse.wst.xml.search.editor.core.validation;

public abstract class AbstractValidationResult implements IValidationResult {

	private final String value;
	private final int startIndex;
	private final int endIndex;
	protected int nbElements = 0;

	public AbstractValidationResult(String value, int startIndex, int endIndex) {
		this.value = value;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public AbstractValidationResult() {
		this(null, -1, -1);
	}

	public String getValue() {
		return value;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public boolean isMulti() {
		return false;
	}
	
	public int getNbElements() {
		return nbElements;
	}
	
	public void setNbElements(int nbElements) {
		this.nbElements = nbElements;
	}
}
