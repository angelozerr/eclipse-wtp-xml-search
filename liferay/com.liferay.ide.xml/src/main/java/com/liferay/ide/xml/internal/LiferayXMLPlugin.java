/*******************************************************************************
 * Copyright (c) 2013-2014 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package com.liferay.ide.xml.internal;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LiferayXMLPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.liferay.ide.xml";

	// The shared instance
	private static LiferayXMLPlugin plugin;

	// Images
	public static String PORTLET_IMG = "portlet";

	/**
	 * The constructor
	 */
	public LiferayXMLPlugin() {
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

		URL baseIconsURL = getBundle().getEntry("icons/");
		ImageDescriptor portletImage = ImageDescriptor.createFromURL(new URL(
				baseIconsURL, "portlet.png"));
		getImageRegistry().put(PORTLET_IMG, portletImage);
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
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LiferayXMLPlugin getDefault() {
		return plugin;
	}

}
