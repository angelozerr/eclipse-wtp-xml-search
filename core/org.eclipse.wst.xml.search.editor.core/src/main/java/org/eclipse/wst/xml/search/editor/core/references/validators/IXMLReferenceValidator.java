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
package org.eclipse.wst.xml.search.editor.core.references.validators;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.validation.LocalizedMessage;


public interface IXMLReferenceValidator {

	public LocalizedMessage validate(IXMLReference reference, IDOMNode node, IFile file, IValidator validator);
}
