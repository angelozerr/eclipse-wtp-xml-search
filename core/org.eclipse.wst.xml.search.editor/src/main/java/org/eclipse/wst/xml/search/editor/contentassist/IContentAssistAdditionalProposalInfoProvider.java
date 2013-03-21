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
package org.eclipse.wst.xml.search.editor.contentassist;

import org.eclipse.swt.graphics.Image;

/**
 * Content assist additional proposal info provider.
 * 
 * @param <T>
 */
public interface IContentAssistAdditionalProposalInfoProvider<T> {

	String getDisplayText(String displayText, T node);

	Image getImage(T node);

	String getTextInfo(T node);
}
