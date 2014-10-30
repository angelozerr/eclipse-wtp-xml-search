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
package org.apache.struts2.ide.validators.core;

public interface XWorkValidatorConstants {

	String VALIDATOR_CONTENT_TYPE = "org.apache.struts2.ide.contenttype.xworkValidatorConfig";
	String VALIDATOR_CONFIG_CONTENT_TYPE = "org.apache.struts2.ide.contenttype.xworkValidator";
		
	// XML Attributes
	String NAME_ATTR = "name";
	String TYPE_ATTR = "type";
	String CLASS_ATTR = "class";
	String ABSTRACT_ATTR = "abstract";
	String DEFAULT_ATTR = "default";
	String FILE_ATTR = "file";
	
	// XML element
	String INCLUDE_ELT = "include";
	
}
