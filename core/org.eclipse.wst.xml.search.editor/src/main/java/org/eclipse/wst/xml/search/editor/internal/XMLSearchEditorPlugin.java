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
package org.eclipse.wst.xml.search.editor.internal;

import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.xml.search.core.storage.StructuredStorageModelManager;
import org.eclipse.wst.xml.search.editor.internal.contentassist.ContentAssistsManager;
import org.eclipse.wst.xml.search.editor.internal.indexing.XMLReferencesIndexManager;
import org.eclipse.wst.xml.search.editor.internal.jdt.JDTStorageLocationProvider;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferencesManager;
import org.eclipse.wst.xml.search.editor.internal.references.filters.XMLReferenceFiltersManager;
import org.eclipse.wst.xml.search.editor.internal.references.validators.XMLReferenceValidatorsManager;
import org.eclipse.wst.xml.search.editor.internal.reporter.XMLSearchReporterManager;
import org.eclipse.wst.xml.search.editor.internal.searchers.expressions.XMLExpressionParserManager;
import org.eclipse.wst.xml.search.editor.internal.searchers.java.JavaQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.internal.searchers.javamethod.JavaMethodQuerySpecificationrManager;
import org.eclipse.wst.xml.search.editor.java.JavaReferencesManager;
import org.eclipse.wst.xml.search.editor.java.JavaReferencesMatchersManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XMLSearchEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.xml.search.editor";

	// The shared instance
	private static XMLSearchEditorPlugin plugin;

	private JavaElementImageProvider javaElementLabelProvider;

	/**
	 * The constructor
	 */
	public XMLSearchEditorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		XMLSearchReporterManager.getDefault().initialize();
		XMLReferenceFiltersManager.getDefault().initialize();
		XMLReferenceValidatorsManager.getDefault().initialize();
		XMLReferencesManager.getInstance().initialize();
		JavaReferencesMatchersManager.getInstance().initialize();
		JavaReferencesManager.getInstance().initialize();
		ContentAssistsManager.getDefault().initialize();
		JavaQuerySpecificationrManager.getDefault().initialize();
		JavaMethodQuerySpecificationrManager.getDefault().initialize();
		XMLReferencesIndexManager.getDefault().initialize();
		javaElementLabelProvider = new JavaElementImageProvider();
		StructuredStorageModelManager.getModelManager()
				.setStorageLocationProvider(new JDTStorageLocationProvider());
		XMLExpressionParserManager.getDefault().initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		XMLSearchReporterManager.getDefault().destroy();
		XMLReferenceFiltersManager.getDefault().destroy();
		XMLReferenceValidatorsManager.getDefault().destroy();
		XMLReferencesManager.getInstance().destroy();
		JavaReferencesMatchersManager.getInstance().destroy();
		JavaReferencesManager.getInstance().destroy();
		ContentAssistsManager.getDefault().destroy();
		JavaQuerySpecificationrManager.getDefault().destroy();
		JavaMethodQuerySpecificationrManager.getDefault().destroy();
		XMLReferencesIndexManager.getDefault().destroy();
		XMLExpressionParserManager.getDefault().destroy();
		javaElementLabelProvider = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XMLSearchEditorPlugin getDefault() {
		return plugin;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	/**
	 * @return Returns the active workbench window's currrent page.
	 */
	public static IWorkbenchPage getActivePage() {
		return getActiveWorkbenchWindow().getActivePage();
	}

	public JavaElementImageProvider getJavaElementLabelProvider() {
		return javaElementLabelProvider;
	}
}
