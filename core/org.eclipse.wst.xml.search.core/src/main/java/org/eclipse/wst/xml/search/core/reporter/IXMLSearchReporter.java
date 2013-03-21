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
package org.eclipse.wst.xml.search.core.reporter;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResource;

/**
 * Search reporter to trace start/end search.
 *
 */
public interface IXMLSearchReporter {

	void beginSearch(int searchId, Map<IResource, Collection<String>> query);

	void endSearch(int searchId, long elapsedTime);

	boolean isEnabled();

}
