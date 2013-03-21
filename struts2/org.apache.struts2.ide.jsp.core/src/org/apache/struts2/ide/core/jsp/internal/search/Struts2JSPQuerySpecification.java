package org.apache.struts2.ide.core.jsp.internal.search;

import org.eclipse.jst.jsp.search.editor.queryspecifications.JSPQuerySpecification;

public class Struts2JSPQuerySpecification extends JSPQuerySpecification {

	public Struts2JSPQuerySpecification() {
		super(Struts2TaglibReferenceFilter.INSTANCE);
	}

}
