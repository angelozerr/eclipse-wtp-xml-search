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
package org.eclipse.wst.xml.search.editor.core.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.xml.search.core.storage.StructuredStorageModelManager;
import org.eclipse.wst.xml.search.editor.core.internal.references.filters.XMLReferenceFiltersManager;
import org.eclipse.wst.xml.search.editor.core.internal.references.validators.XMLReferenceValidatorsManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.expressions.XMLExpressionParserManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.java.JavaQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.javamethod.JavaMethodQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.core.java.JavaReferencesManager;
import org.eclipse.wst.xml.search.editor.core.java.JavaReferencesMatchersManager;
import org.eclipse.wst.xml.search.editor.core.jdt.JDTStorageLocationProvider;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesManager;
import org.eclipse.wst.xml.search.editor.core.reporter.XMLSearchReporterManager;
import org.eclipse.wst.xml.search.editor.core.searchers.XMLValidationSearcherBindingsManager;
import org.eclipse.wst.xml.search.editor.core.searchers.XMLValidationSearcherManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XMLSearchEditorCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.xml.search.editor.core";

	// The shared instance
	private static XMLSearchEditorCorePlugin plugin;

	/**
	 * The constructor
	 */
	public XMLSearchEditorCorePlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		XMLSearchReporterManager.getDefault().initialize();
		XMLReferenceFiltersManager.getDefault().initialize();
		XMLReferenceValidatorsManager.getDefault().initialize();
		XMLReferencesManager.getInstance().initialize();
		JavaReferencesMatchersManager.getInstance().initialize();
		JavaReferencesManager.getInstance().initialize();
		JavaQuerySpecificationrManager.getDefault().initialize();
		JavaMethodQuerySpecificationrManager.getDefault().initialize();
		XMLExpressionParserManager.getDefault().initialize();
		XMLValidationSearcherManager.getDefault().initialize();
		XMLValidationSearcherBindingsManager.getDefault().initialize();
		StructuredStorageModelManager.getModelManager()
			.setStorageLocationProvider(new JDTStorageLocationProvider());
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		XMLSearchReporterManager.getDefault().destroy();
		XMLReferenceFiltersManager.getDefault().destroy();
		XMLReferenceValidatorsManager.getDefault().destroy();
		XMLReferencesManager.getInstance().destroy();
		JavaReferencesMatchersManager.getInstance().destroy();
		JavaReferencesManager.getInstance().destroy();
		JavaQuerySpecificationrManager.getDefault().destroy();
		JavaMethodQuerySpecificationrManager.getDefault().destroy();
		XMLExpressionParserManager.getDefault().destroy();
		XMLValidationSearcherBindingsManager.getDefault().destroy();
		XMLValidationSearcherManager.getDefault().destroy();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XMLSearchEditorCorePlugin getDefault() {
		return plugin;
	}
}
