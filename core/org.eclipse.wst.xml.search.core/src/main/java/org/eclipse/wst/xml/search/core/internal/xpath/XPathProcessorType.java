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
package org.eclipse.wst.xml.search.core.internal.xpath;

import org.eclipse.wst.xml.search.core.xpath.IXPathProcessor;
import org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType;

/**
 * 
 * Implementation of {@link IXPathProcessorType}.
 * 
 */
public class XPathProcessorType implements IXPathProcessorType {

	private final String id;
	private final String name;
	private final boolean contributed;
	private final String source;
	private final IXPathProcessor evaluator;

	public XPathProcessorType(String id, String name, String source,
			boolean contributed, IXPathProcessor evaluator) {
		this.id = id;
		this.name = name;
		this.source = source;
		this.contributed = contributed;
		this.evaluator = evaluator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType#getProcessor()
	 */
	public IXPathProcessor getProcessor() {
		return evaluator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType#isContributed()
	 */
	public boolean isContributed() {
		return contributed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType#getSource()
	 */
	public String getSource() {
		return source;
	}
}
