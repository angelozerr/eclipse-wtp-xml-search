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
package org.eclipse.wst.xml.search.editor.searchers.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;

public class DefaultExtendedClassProvider implements IExtendedClassProvider {

	private String[] extendedClasses;
	
	
	public DefaultExtendedClassProvider(String[] implementsClass) {
		this.extendedClasses = implementsClass;
	}
	
	public IType[] getExtends(Object selectedNode, IFile file) {
		Collection<IType> types = null;
		for (int i = 0; i < extendedClasses.length; i++) {
			String className = extendedClasses[i];
			IType type = JdtUtils.getJavaType(file.getProject(), className);
			if (type != null) {
				if (types == null) {
					types = new ArrayList<IType>();
				}
				types.add(type);
			}
		}
		if (types != null) {
			return types.toArray(JdtUtils.EMPTY_TYPE);
		}
		return null;
	}

}
