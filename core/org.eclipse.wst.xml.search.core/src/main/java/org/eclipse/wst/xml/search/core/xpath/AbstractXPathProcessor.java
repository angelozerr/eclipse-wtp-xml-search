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
package org.eclipse.wst.xml.search.core.xpath;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public abstract class AbstractXPathProcessor implements IXPathProcessor {

	public String computeXPath(String xpath, String... args) {
		if (args == null || args.length < 1) {
			return xpath;
		}
		return MessageFormat.format(xpath, (Object[]) args);
	}
	
	protected IStatus createStatusForXPathNotValid(String xpath, String pluginId, Throwable e) {
		return new Status(IStatus.ERROR, pluginId, "Error XPath parsing",
				e);
	}

}
