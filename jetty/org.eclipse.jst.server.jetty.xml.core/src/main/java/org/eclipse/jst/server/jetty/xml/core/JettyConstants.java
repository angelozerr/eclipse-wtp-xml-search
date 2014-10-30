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
package org.eclipse.jst.server.jetty.xml.core;

public interface JettyConstants {

	String JETTY_CONFIG_CONTENT_TYPE = "org.eclipse.jst.server.jetty.xml.contenttype.jettyConfigFile";
	
	// XML Elements 
	String NEW_ELEMENT = "New";
	String REF_ELT = "Ref";
	String GET_ELT = "Get";
	
	// XML Attributes
	String ID_ATTR = "id";
	String NAME_ATTR = "name";
	String CLASS_ATTR = "class";
}
