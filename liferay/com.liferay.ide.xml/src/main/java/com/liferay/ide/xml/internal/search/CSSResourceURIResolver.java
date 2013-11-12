package com.liferay.ide.xml.internal.search;

import java.util.HashSet;
import java.util.Set;

/**
 * Extension of WTP/XML Search resources uri resolver for Liferay to manage css,
 * js, icons used in the descriptor of lifreay which starts with "/". Ex :
 * 
 * <pre>
 * <header-portlet-css>/html/portlet/directory/css/main.css</header-portlet-css>
 * </pre>
 * 
 */
public class CSSResourceURIResolver extends AbstractWebResourceURIResolver {

	public static final CSSResourceURIResolver INSTANCE = new CSSResourceURIResolver();

	private static final Set<String> EXTENSIONS;

	static {
		EXTENSIONS = new HashSet<String>();
		EXTENSIONS.add("css");
	}

	@Override
	protected Set<String> getExtensions() {
		return EXTENSIONS;
	}
}
