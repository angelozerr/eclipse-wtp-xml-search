/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.editor.core.references;


public interface IXMLReferenceTo {

	public enum ToType {
		XML, RESOURCE, JAVA, JAVA_METHOD, STATIC, PROPERTY, EXPRESSION
	}

	String getReferenceToId();

	ToType getType();

	IReference getOwnerReference();

	String getQuerySpecificationId();

	String getTokenId();
	

}
