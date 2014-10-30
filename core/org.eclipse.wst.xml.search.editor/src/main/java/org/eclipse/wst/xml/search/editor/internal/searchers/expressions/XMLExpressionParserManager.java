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
package org.eclipse.wst.xml.search.editor.internal.searchers.expressions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.AbstractRegistryManager;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.internal.XMLSearchEditorPlugin;
import org.eclipse.wst.xml.search.editor.searchers.expressions.IXMLExpressionParser;

public class XMLExpressionParserManager extends AbstractRegistryManager {

	private static XMLExpressionParserManager INSTANCE = new XMLExpressionParserManager();

	private static final String CLASS_ATTR = "class";
	private static final String ID_ATTR = "id";
	private static final String EXPRESSION_PARSER_ELT = "parser";
	private static final String EXPRESSION_PARSERS_EXTENSION_POINT = "expressionParsers";

	private Map<String, IXMLExpressionParser> expressionParserById = null;

	public static XMLExpressionParserManager getDefault() {
		return INSTANCE;
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (expressionParserById == null) {// not loaded yet
			return;
		}
		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addParser(expressionParserById, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addParser(
			Map<String, IXMLExpressionParser> expressionParserById,
			IConfigurationElement[] cf) {
		String id = null;
		for (IConfigurationElement ce : cf) {
			// loop for to get provider declaration
			if (EXPRESSION_PARSER_ELT.equals(ce.getName())) {
				id = ce.getAttribute(ID_ATTR);
				try {
					Object o = ce.createExecutableExtension(CLASS_ATTR);
					if (o instanceof IXMLExpressionParser) {
						expressionParserById.put(id, (IXMLExpressionParser) o);
					}
				} catch (Throwable t) {
					Trace.trace(Trace.SEVERE,
							"  Could not load expressionParser for id: " + id,
							t);
				}
			}
		}
	}

	public IXMLExpressionParser getParser(String parserId) {
		if (StringUtils.isEmpty(parserId)) {
			return null;
		}
		if (expressionParserById == null) {
			loadParsers();
		}

		return expressionParserById.get(parserId);
	}

	private synchronized void loadParsers() {
		if (expressionParserById != null) {
			return;
		}
		Map<String, IXMLExpressionParser> expressionParserById = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					XMLSearchEditorPlugin.PLUGIN_ID,
					EXPRESSION_PARSERS_EXTENSION_POINT);
			expressionParserById = new HashMap<String, IXMLExpressionParser>(
					cf.length);
			addParser(expressionParserById, cf);
		} else {
			expressionParserById = new HashMap<String, IXMLExpressionParser>();
		}
		this.expressionParserById = expressionParserById;
		super.addRegistryListenerIfNeeded();

	}

	@Override
	protected String getExtensionPoint() {
		return EXPRESSION_PARSERS_EXTENSION_POINT;
	}

	@Override
	protected String getPluginId() {
		return XMLSearchEditorPlugin.PLUGIN_ID;
	}

}
