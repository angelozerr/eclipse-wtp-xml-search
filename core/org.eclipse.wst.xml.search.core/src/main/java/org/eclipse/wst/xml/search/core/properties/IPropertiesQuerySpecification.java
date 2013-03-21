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
package org.eclipse.wst.xml.search.core.properties;

import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;

/**
 * Interface of Properties Query specification used to search property from
 * properties files coming from workspace or JAR.
 * 
 */
public interface IPropertiesQuerySpecification extends IResourceProvider,
		IPropertiesRequestorProvider, IMultiResourceProvider {

	public static final IPropertiesQuerySpecification[] EMPTY = new IPropertiesQuerySpecification[0];

	/**
	 * Returns true if multi-resources is supported and false otherwise.
	 * 
	 * @return
	 */
	boolean isMultiResource();

}
