package org.apache.struts2.ide.ui.validators.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	public static String Validation_ActionNotFound;
	public static String Validation_ActionNonUnique;
	public static String Validation_MessageKeyNotFound;
	public static String Validation_MessageKeyNonUnique;

	private static final String BUNDLE_NAME = Messages.class.getName();

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
