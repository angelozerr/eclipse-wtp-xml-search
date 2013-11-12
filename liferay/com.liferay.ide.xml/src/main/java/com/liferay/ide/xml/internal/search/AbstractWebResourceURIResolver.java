package com.liferay.ide.xml.internal.search;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.resource.ResourceBaseURIResolver;

/**
 * Extension of WTP/XML Search resources uri resolver for Liferay to manage css,
 * js, icons used in the descriptor of lifreay which starts with "/". Ex :
 * 
 * <pre>
 * <header-portlet-css>/html/portlet/directory/css/main.css</header-portlet-css>
 * </pre>
 * 
 */
public abstract class AbstractWebResourceURIResolver extends
		ResourceBaseURIResolver {

	@Override
	public String resolve(Object selectedNode, IResource rootContainer,
			IResource file) {
		return "/" + super.resolve(selectedNode, rootContainer, file);
	}

	@Override
	public boolean accept(Object selectedNode, IResource rootContainer,
			IResource file, String matching, boolean fullMatch) {
		String extension = file.getFileExtension();
		if (!getExtensions().contains(extension)) {
			return false;
		}
		return super.accept(selectedNode, rootContainer, file, matching,
				fullMatch);
	}

	protected abstract Set<String> getExtensions();
}
