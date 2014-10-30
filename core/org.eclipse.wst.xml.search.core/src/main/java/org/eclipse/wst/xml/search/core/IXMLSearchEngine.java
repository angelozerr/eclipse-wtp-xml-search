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
package org.eclipse.wst.xml.search.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecificationRegistry;
import org.eclipse.wst.xml.search.core.reporter.IXMLSearchReporter;

/**
 * XML search engine interface.
 * 
 */
public interface IXMLSearchEngine extends ISimpleXMLSearchEngine {

	IStatus search(IXMLQuerySpecificationRegistry querySpecificationRegistry,
			IXMLSearchDOMNodeCollector collector, IXMLSearchReporter reporter,
			IProgressMonitor monitor);
}
