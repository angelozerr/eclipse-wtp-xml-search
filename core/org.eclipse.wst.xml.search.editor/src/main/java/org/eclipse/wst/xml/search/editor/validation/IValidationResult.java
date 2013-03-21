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
package org.eclipse.wst.xml.search.editor.validation;

public interface IValidationResult {

	public static final IValidationResult OK = new AbstractValidationResult() {
		public int getNbElements() {
			return 1;
		}

		public void setNbElements(int nbElements) {

		}
	};

	public static final IValidationResult NOK = new AbstractValidationResult() {
		public int getNbElements() {
			return 0;
		}

		public void setNbElements(int nbElements) {

		}
	};

	String getValue();

	int getStartIndex();

	int getEndIndex();

	int getNbElements();

	void setNbElements(int nbElements);

	boolean isMulti();

}
