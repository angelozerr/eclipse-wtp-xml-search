/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - adapt for XML Search.
 * Code comes from org.eclipse.wst.xml.xpath.ui.internal.preferences.XPathPrefencePage but there is not Header license.
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.internal.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractPreferencePage;
import org.eclipse.wst.xml.search.ui.internal.Messages;

@SuppressWarnings("restriction")
public class XMLSearchPreferencePage extends AbstractPreferencePage {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 * @deprecated
	 */
	protected Control createContents(Composite parent) {
		Composite composite = createScrolledComposite(parent);

		String description = Messages.XMLSearchPreferencePage_0;
		Text text = new Text(composite, SWT.READ_ONLY);
		// some themes on GTK have different background colors for Text and
		// Labels
		text.setBackground(composite.getBackground());
		text.setText(description);

		setSize(composite);
		return composite;
	}

}
