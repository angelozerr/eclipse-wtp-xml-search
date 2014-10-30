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
package org.apache.struts2.ide.ui.validators.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Utility class to handle image resources.
 */
public class ImageResource {
	// the image registry
	private static ImageRegistry imageRegistry;

	// map of image descriptors since these
	// will be lost by the image registry
	private static Map<String, ImageDescriptor> imageDescriptors;

	// base urls for images
	private static URL ICON_BASE_URL;

	private static final String URL_OBJ = "obj16/";

	// General Object Images
	public static final String IMG_STRUTS2_OBJ = "struts2";
	public static final String IMG_INTERCEPTOR_OBJ = "interceptor_obj";
	public static final String IMG_INTERCEPTOR_STACK_OBJ = "interceptor_stack_obj";
	public static final String IMG_PACKAGE_OBJ = "package_obj";
	public static final String IMG_PACKAGE_ABSTRACT_OBJ = "package_abstract_obj";
	public static final String IMG_RESULT_TYPE_OBJ = "result_type_obj";
	public static final String IMG_RESULT_TYPE_DEFAULT_OBJ = "result_type_default_obj";
	public static final String IMG_RESULT_OBJ = "result_obj";
	
	static {
		try {
			String pathSuffix = "icons/";
			ICON_BASE_URL = XWorkValidatorUIPlugin.getDefault().getBundle().getEntry(
					pathSuffix);
		} catch (Exception e) {
			Trace.trace(Trace.SEVERE, "Error while getting /icons URL.", e);
		}
	}

	/**
	 * Cannot construct an ImageResource. Use static methods only.
	 */
	private ImageResource() {
		// do nothing
	}

	/**
	 * Dispose of element images that were created.
	 */
	protected static void dispose() {
		// do nothing
	}

	/**
	 * Return the image with the given key.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.swt.graphics.Image
	 */
	public static Image getImage(String key) {
		return getImage(key, null);
	}

	/**
	 * Return the image with the given key.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.swt.graphics.Image
	 */
	public static Image getImage(String key, String keyIfImageNull) {
		if (imageRegistry == null)
			initializeImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null) {
			if (keyIfImageNull != null) {
				return getImage(keyIfImageNull, null);
			}
			imageRegistry.put(key, ImageDescriptor.getMissingImageDescriptor());
			image = imageRegistry.get(key);
		}
		return image;
	}

	/**
	 * Return the image descriptor with the given key.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.jface.resource.ImageDescriptor
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		if (imageRegistry == null)
			initializeImageRegistry();
		ImageDescriptor id = imageDescriptors.get(key);
		if (id != null)
			return id;

		return ImageDescriptor.getMissingImageDescriptor();
	}

	/**
	 * Initialize the image resources.
	 */
	protected static void initializeImageRegistry() {
		imageRegistry = new ImageRegistry();
		imageDescriptors = new HashMap<String, ImageDescriptor>();

		// load general object images
		registerImage(IMG_STRUTS2_OBJ, URL_OBJ + IMG_STRUTS2_OBJ
				+ ".png");
		registerImage(IMG_INTERCEPTOR_OBJ, URL_OBJ + IMG_INTERCEPTOR_OBJ
				+ ".gif");
		registerImage(IMG_INTERCEPTOR_STACK_OBJ, URL_OBJ
				+ IMG_INTERCEPTOR_STACK_OBJ + ".gif");
		registerImage(IMG_PACKAGE_OBJ, URL_OBJ + IMG_PACKAGE_OBJ + ".gif");
		registerImage(IMG_PACKAGE_ABSTRACT_OBJ, URL_OBJ
				+ IMG_PACKAGE_ABSTRACT_OBJ + ".gif");
		registerImage(IMG_RESULT_TYPE_OBJ, URL_OBJ + IMG_RESULT_TYPE_OBJ
				+ ".gif");
		registerImage(IMG_RESULT_TYPE_DEFAULT_OBJ, URL_OBJ
				+ IMG_RESULT_TYPE_DEFAULT_OBJ + ".gif");
		registerImage(IMG_RESULT_OBJ, URL_OBJ + IMG_RESULT_OBJ
				+ ".gif");

		// PlatformUI
		// .getWorkbench()
		// .getProgressService()
		// .registerIconForFamily(getImageDescriptor(IMG_SERVER),
		// GeneratorUtil.SERVER_JOB_FAMILY);
	}

	/**
	 * Register an image with the registry.
	 * 
	 * @param key
	 *            java.lang.String
	 * @param partialURL
	 *            java.lang.String
	 */
	private static void registerImage(String key, String partialURL) {
		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(
					ICON_BASE_URL, partialURL));
			imageRegistry.put(key, id);
			imageDescriptors.put(key, id);
		} catch (Exception e) {
			Trace.trace(Trace.SEVERE, "Error registering image " + key
					+ " from " + partialURL, e);
		}
	}

}