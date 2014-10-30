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
package org.eclipse.wst.xml.search.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.wst.xml.search.core.internal.XMLSearchCorePlugin;
import org.eclipse.wst.xml.search.core.xpath.DefaultXPathProcessor;

/**
 * XML Search Core preferences initializer used to initialize the preference of XPath
 * Preprocessor.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public static final String DEFAULT_XPATH_PROCESSOR = "defaultXPathProcessor";

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope()
				.getNode(XMLSearchCorePlugin.getDefault().getBundle()
						.getSymbolicName());
		node.put(DEFAULT_XPATH_PROCESSOR, DefaultXPathProcessor.ID);
	}

}
