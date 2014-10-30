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
package org.eclipse.wst.xml.search.editor.internal.hyperlink;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;

/**
 * JFace {@link IHyperlink} implementation for JDT Java element
 * {@link IJavaElement} .
 * 
 */
public class JavaElementHyperlink implements IHyperlink {

	private final IRegion region;
	private final IJavaElement element;

	public JavaElementHyperlink(IRegion region, IJavaElement element) {
		this.region = region;
		this.element = element;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public void open() {
		if (element != null)
			try {
				JavaUI.revealInEditor(JavaUI.openInEditor(element), element);
			} catch (PartInitException _ex) {
			} catch (JavaModelException _ex) {
			}
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return (new StringBuilder("Open '"))
				.append(JavaElementLabels.getElementLabel(element,
						JavaElementLabels.ALL_POST_QUALIFIED)).append("'")
				.toString();
	}

}
