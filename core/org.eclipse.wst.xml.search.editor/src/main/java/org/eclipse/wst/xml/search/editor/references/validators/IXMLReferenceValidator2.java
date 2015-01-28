/**
 *  Copyright (c) 2000-present Liferay, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Gregory Amerson <gregory.amerson@liferay.com> - initial API and implementation
 */
package org.eclipse.wst.xml.search.editor.references.validators;

import org.eclipse.core.resources.IProject;

public interface IXMLReferenceValidator2 {

	public boolean isValidTarget(IProject project);

}
