package org.apache.struts2.ide.core;

import org.apache.struts2.ide.core.internal.Struts2Model;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Struts2CorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.apache.struts2.ide.core";

	// The shared instance
	private static Struts2CorePlugin plugin;

	/**
	 * The constructor
	 */
	public Struts2CorePlugin() {
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
	public static Struts2CorePlugin getDefault() {
		return plugin;
	}

	public static IStruts2Model getStruts2Model() {
		return Struts2Model.getInstance();
	}

}
