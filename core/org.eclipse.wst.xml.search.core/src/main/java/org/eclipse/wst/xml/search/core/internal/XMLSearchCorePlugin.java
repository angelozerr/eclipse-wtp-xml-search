/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.core.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.core.properties.PropertiesQuerySpecificationManager;
import org.eclipse.wst.xml.search.core.queryspecifications.XMLQuerySpecificationManager;
import org.eclipse.wst.xml.search.core.resource.ResourceQuerySpecificationManager;
import org.eclipse.wst.xml.search.core.statics.StaticValueQuerySpecificationManager;
import org.eclipse.wst.xml.search.core.xpath.XPathProcessorManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XMLSearchCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.xml.search.core";

	// The shared instance
	private static XMLSearchCorePlugin plugin;

	/**
	 * The constructor
	 */
	public XMLSearchCorePlugin() {
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
		XMLQuerySpecificationManager.getDefault().initialize();
		StaticValueQuerySpecificationManager.getDefault().initialize();
		ResourceQuerySpecificationManager.getDefault().initialize();
		PropertiesQuerySpecificationManager.getDefault().initialize();
		XPathProcessorManager.getDefault().initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		XMLQuerySpecificationManager.getDefault().destroy();
		StaticValueQuerySpecificationManager.getDefault().destroy();
		ResourceQuerySpecificationManager.getDefault().destroy();
		PropertiesQuerySpecificationManager.getDefault().destroy();
		XPathProcessorManager.getDefault().destroy();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XMLSearchCorePlugin getDefault() {
		return plugin;
	}

	public static IStatus createStatus(int severity, String message) {
		return new Status(severity, XMLSearchCorePlugin.PLUGIN_ID, message);
	}

	public static IStatus createStatus(int severity, String message, Throwable e) {
		return new Status(severity, XMLSearchCorePlugin.PLUGIN_ID, message, e);
	}

}
