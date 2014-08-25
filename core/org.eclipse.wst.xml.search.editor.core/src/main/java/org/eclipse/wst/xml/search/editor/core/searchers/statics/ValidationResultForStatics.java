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
package org.eclipse.wst.xml.search.editor.core.searchers.statics;

import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.editor.core.validation.AbstractValidationResult;

public class ValidationResultForStatics extends AbstractValidationResult
		implements IStaticValueCollector {

	public ValidationResultForStatics(String value, int startIndex, int endIndex) {
		super(value, startIndex, endIndex);
	}

	public boolean add(IStaticValue value) {
		nbElements++;
		return true;
	}

}
