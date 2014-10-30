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
package org.apache.struts2.ide.ui.internal;

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
