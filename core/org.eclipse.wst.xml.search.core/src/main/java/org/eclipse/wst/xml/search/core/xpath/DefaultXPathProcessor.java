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
package org.eclipse.wst.xml.search.core.xpath;

import javax.xml.xpath.XPathFactory;


public class DefaultXPathProcessor extends
		AbstractXPathProcessorForXPathFactory {

	public static final IXPathProcessor INSTANCE = new DefaultXPathProcessor();
	
	public static final String ID = "org.eclipse.wst.xml.search.core.xpath.DefaultXPathProcessor";

	@Override
	protected XPathFactory createFactory() {
		return XPathFactory.newInstance();
	}
}
