/*******************************************************************************
 * Copyright (c) 2010 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.ui.internal.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType;
import org.eclipse.wst.xml.search.core.xpath.XPathProcessorManager;
import org.eclipse.wst.xml.search.ui.internal.Messages;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchUIPlugin;

public class XPathProcessorPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private InstalledProcessorsBlock processorsBlock;

	public XPathProcessorPreferencePage() {
		super();
		// only used when page is shown programatically
		setTitle(Messages.XPathProcessorPreferencePage_0);
		setDescription(Messages.XPathProcessorPreferencePage_1);
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(XMLSearchUIPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite ancestor) {
		initializeDialogUnits(ancestor);

		noDefaultAndApplyButton();

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		ancestor.setLayout(layout);

		processorsBlock = new InstalledProcessorsBlock();
		processorsBlock.createControl(ancestor);
		Control control = processorsBlock.getControl();
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		control.setLayoutData(data);

		// TODO PlatformUI.getWorkbench().getHelpSystem().setHelp...

		initDefaultInstall();
		processorsBlock
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IXPathProcessorType processor = getCurrentDefaultProcessor();
						if (processor == null) {
							setValid(false);
							setErrorMessage(Messages.XPathProcessorPreferencePage_2);
						} else {
							setValid(true);
							setErrorMessage(null);
						}
					}
				});
		applyDialogFont(ancestor);
		return ancestor;
	}

	@Override
	public boolean performOk() {
		IXPathProcessorType processorType = getCurrentDefaultProcessor();
		if (processorType == null) {
			setErrorMessage("Please select a XPath processor");
			return false;
		}
		processorsBlock.saveColumnSettings();
		XPathProcessorManager.getDefault().setDefaultProcessor(processorType);
		return true;
	}

	private void initDefaultInstall() {
		IXPathProcessorType processorType = XPathProcessorManager.getDefault()
				.getDefaultProcessor();
		verifyDefaultVM(processorType);

		// IXPathEvaluatorType realDefault = JAXPRuntime.getDefaultProcessor();
		// if (realDefault != null) {
		// IXPathEvaluatorType[] installs = processorsBlock.getProcessors();
		// for (IXPathEvaluatorType fakeInstall : installs) {
		// if (fakeInstall.getId().equals(realDefault.getId())) {
		// verifyDefaultVM(fakeInstall);
		// break;
		// }
		// }
		// }
	}

	private void verifyDefaultVM(IXPathProcessorType install) {
		if (install != null) {
			processorsBlock.setCheckedInstall(install);
		} else {
			processorsBlock.setCheckedInstall(null);
		}
	}

	private IXPathProcessorType getCurrentDefaultProcessor() {
		return processorsBlock.getCheckedInstall();
	}

}
