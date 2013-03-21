/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui;

import org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Extension of {@link DecoratingStyledCellLabelProvider} for DOM-SSE.
 * 
 */
public class DecoratingXMLSearchLabelProvider extends
		DecoratingStyledCellLabelProvider implements ILabelProvider {

	public DecoratingXMLSearchLabelProvider(XMLLabelProvider provider) {
		super(provider, PlatformUI.getWorkbench().getDecoratorManager()
				.getLabelDecorator(), null);
	}

	public String getText(Object element) {
		return getStyledText(element).getString();
	}

}
