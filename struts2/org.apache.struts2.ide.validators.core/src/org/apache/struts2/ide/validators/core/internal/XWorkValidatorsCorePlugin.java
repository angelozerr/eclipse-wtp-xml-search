package org.apache.struts2.ide.validators.core.internal;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XWorkValidatorsCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.apache.struts2.ide.validators.core";

	// The shared instance
	private static XWorkValidatorsCorePlugin plugin;
	
	/**
	 * The constructor
	 */
	public XWorkValidatorsCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static XWorkValidatorsCorePlugin getDefault() {
		return plugin;
	}

}
