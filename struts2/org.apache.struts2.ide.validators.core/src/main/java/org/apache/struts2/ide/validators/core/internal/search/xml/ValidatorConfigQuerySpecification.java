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
package org.apache.struts2.ide.validators.core.internal.search.xml;

import org.apache.struts2.ide.core.IStruts2Model;
import org.apache.struts2.ide.core.Struts2CorePlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IStorageProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;

public class ValidatorConfigQuerySpecification implements IXMLSearchRequestorProvider,
		IMultiResourceProvider, IStorageProvider {

	public IXMLSearchRequestor getRequestor() {
		return ValidatorConfigSearchRequestor.INSTANCE;
	}
	
	public IResource[] getResources(Object selectedNode, IResource resource) {	
		return null;
	}
	
	public IStorage getStorage(Object selectedNode, IResource resource) {
		IStruts2Model model = Struts2CorePlugin.getStruts2Model();
		return model.getDefaultFromXWorkCoreJAR(resource.getProject());
	}

}
