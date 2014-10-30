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
package org.apache.struts2.ide.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;

public interface IStruts2Model {

	/**
	 * Returns "struts-default.xml" {@link IStorage} from the JAR
	 * "struts2-core-*.jar".
	 * 
	 * @param project
	 * @return
	 */
	IStorage getStrutsDefaultFromStruts2CoreJAR(IProject project);
	
	/**
	 * Returns "default.xml" {@link IStorage} from the JAR
	 * "xwork-core-*.jar".
	 * 
	 * @param project
	 * @return
	 */
	IStorage getDefaultFromXWorkCoreJAR(IProject project);

	/**
	 * 
	 * @param project
	 * @return
	 */
	IStorage[] getStrutsXMLFromJAR(IProject project);

}
