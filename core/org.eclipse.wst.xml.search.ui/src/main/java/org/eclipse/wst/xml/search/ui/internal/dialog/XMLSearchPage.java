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
package org.eclipse.wst.xml.search.ui.internal.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.XMLSearchEngine2;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AbstractXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.AllFilesXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.IXMLSearchDOMDocumentVisitor;
import org.eclipse.wst.xml.search.core.queryspecifications.visitor.XPathNodeSetSearchVisitor;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.core.xpath.DefaultXPathProcessor;
import org.eclipse.wst.xml.search.core.xpath.IXPathProcessorType;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.core.xpath.XPathProcessorManager;
import org.eclipse.wst.xml.search.ui.internal.Messages;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchQuery;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchUIPlugin;
import org.eclipse.wst.xml.search.ui.internal.util.FileTypeEditor;
import org.eclipse.wst.xml.search.ui.internal.util.SWTUtil;
import org.eclipse.wst.xml.search.ui.util.DOMUtils;
import org.eclipse.wst.xml.search.ui.util.SearchUtil;

/**
 * {@link ISearchPage} implementation for DOM-SSE.
 * 
 */
public class XMLSearchPage extends DialogPage implements ISearchPage {

	private static final String DEFAULT_XML_EXT = "*.xml";
	private static final int HISTORY_SIZE = 12;

	private ISearchPageContainer container;
	private boolean fFirstTime = true;
	private Combo xpathCombo;
	private CLabel fStatusLabel;
	private Combo fExtensions;
	private ComboViewer xpathProcessorViewer;
	private FileTypeEditor fFileTypeEditor;

	private NamespaceInfos namespaceInfos = null;

	// Dialog store id constants
	private static final String PAGE_NAME = "XMLSearchPage"; //$NON-NLS-1$
	private static final String STORE_HISTORY = "HISTORY"; //$NON-NLS-1$
	private static final String STORE_HISTORY_SIZE = "HISTORY_SIZE"; //$NON-NLS-1$

	private List<SearchPatternData> fPreviousSearchPatterns = new ArrayList<SearchPatternData>(
			20);

	private static class SearchPatternData {

		private static final String XPATH_SETTINGS_ID = "xpath";
		private static final String XPATH_EVALUATOR_SETTINGS_ID = "xpathProcessor";
		private static final String FILE_NAME_PATTERNS_SETTINGS_ID = "fileNamePatterns";
		private static final String SCOPE_SETTINGS_ID = "scope";
		private static final String WORKING_SETS_SETTINGS_ID = "workingSets";

		public final String xpath;
		public final String xpathProcessorId;
		public final String[] fileNamePatterns;
		public final int scope;
		public final IWorkingSet[] workingSets;

		public SearchPatternData(String xpath, String xpathProcessorId,
				String[] fileNamePatterns, int scope, IWorkingSet[] workingSets) {
			Assert.isNotNull(fileNamePatterns);
			this.xpath = xpath;
			this.xpathProcessorId = xpathProcessorId;
			this.fileNamePatterns = fileNamePatterns;
			this.scope = scope;
			this.workingSets = workingSets; // can be null
		}

		public void store(IDialogSettings settings) {
			settings.put(XPATH_SETTINGS_ID, xpath); //$NON-NLS-1$
			settings.put(XPATH_EVALUATOR_SETTINGS_ID, xpathProcessorId); //$NON-NLS-1$
			settings.put(FILE_NAME_PATTERNS_SETTINGS_ID, fileNamePatterns); //$NON-NLS-1$
			settings.put(SCOPE_SETTINGS_ID, scope); //$NON-NLS-1$
			if (workingSets != null) {
				String[] wsIds = new String[workingSets.length];
				for (int i = 0; i < workingSets.length; i++) {
					wsIds[i] = workingSets[i].getLabel();
				}
				settings.put(WORKING_SETS_SETTINGS_ID, wsIds); //$NON-NLS-1$
			} else {
				settings.put(WORKING_SETS_SETTINGS_ID, new String[0]); //$NON-NLS-1$
			}

		}

		public static SearchPatternData create(IDialogSettings settings) {
			String xpath = settings.get(XPATH_SETTINGS_ID); //$NON-NLS-1$
			String xpathProcessorId = settings.get(XPATH_EVALUATOR_SETTINGS_ID); //$NON-NLS-1$
			String[] wsIds = settings.getArray(WORKING_SETS_SETTINGS_ID); //$NON-NLS-1$
			IWorkingSet[] workingSets = null;
			if (wsIds != null && wsIds.length > 0) {
				IWorkingSetManager workingSetManager = PlatformUI
						.getWorkbench().getWorkingSetManager();
				workingSets = new IWorkingSet[wsIds.length];
				for (int i = 0; workingSets != null && i < wsIds.length; i++) {
					workingSets[i] = workingSetManager.getWorkingSet(wsIds[i]);
					if (workingSets[i] == null) {
						workingSets = null;
					}
				}
			}
			String[] fileNamePatterns = settings
					.getArray(FILE_NAME_PATTERNS_SETTINGS_ID); //$NON-NLS-1$
			if (fileNamePatterns == null) {
				fileNamePatterns = StringUtils.EMPTY_ARRAY;
			}
			try {
				int scope = settings.getInt(SCOPE_SETTINGS_ID); //$NON-NLS-1$

				return new SearchPatternData(xpath, xpathProcessorId,
						fileNamePatterns, scope, workingSets);
			} catch (NumberFormatException e) {
				return null;
			}
		}

	}

	private static class XPathProcessorLabelProvider extends LabelProvider {
		/*
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return null;
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			return ((IXPathProcessorType) element).getName();
		}

	}

	private static class XPathProcessorContentProvider implements
			IStructuredContentProvider {

		/*
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return XPathProcessorManager.getDefault().getProcessors();
		}
	}

	public XMLSearchPage() {

	}

	// ---- Action Handling ------------------------------------------------

	public boolean performAction() {
		try {
			NewSearchUI.runQueryInBackground(newQuery());
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(),
					Messages.XMLSearchPage_replace_searchproblems_title,
					Messages.XMLSearchPage_replace_searchproblems_message,
					e.getStatus());
			return false;
		}
		return true;
	}

	private ISearchQuery newQuery() throws CoreException {
		SearchPatternData data = getPatternData();
		String xpath = data.xpath;
		IXMLSearchDOMDocumentVisitor visitor = XPathNodeSetSearchVisitor.INSTANCE;

		final Collection<String> extensions = new ArrayList<String>();
		final boolean acceptAllFiles = getExtensions(extensions);
		IXMLSearchRequestor requestor = null;
		if (acceptAllFiles) {
			requestor = AllFilesXMLSearchRequestor.INSTANCE;
		} else {
			requestor = new AbstractXMLSearchRequestor() {
				protected boolean accept(IFile file, IResource rootResource) {
					boolean accept = false;
					for (String extension : extensions) {
						accept = extension.equals(file.getFileExtension());
						if (accept)
							return true;
					}
					return false;
				};
			};
		}

		return new XMLSearchQuery(ResourcesPlugin.getWorkspace().getRoot(),
				requestor, visitor, xpath, getXPathProcessorId(),
				null, null, new XMLSearchEngine2(), null);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		readConfiguration();

		Composite result = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		result.setLayout(layout);

		addXPathControls(result);
		addXPathProcessorControls(result);

		Label separator = new Label(result, SWT.NONE);
		separator.setVisible(false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, false,
				false, 2, 1);
		data.heightHint = convertHeightInCharsToPixels(1) / 3;
		separator.setLayoutData(data);

		addFileNameControls(result);

		setControl(result);

		Dialog.applyDialogFont(result);
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(result, IXMLHelpContextIds.XML_SEARCH_PAGE);

	}

	/*
	 * Implements method from IDialogPage
	 */
	public void setVisible(boolean visible) {
		if (visible && xpathCombo != null) {
			if (fFirstTime) {
				fFirstTime = false;
				// Set item and text here to prevent page from resizing
				xpathCombo.setItems(getPreviousSearchPatterns());
				xpathProcessorViewer.setInput(XPathProcessorManager
						.getDefault().getProcessors());
				fExtensions.setItems(getPreviousExtensions());
				if (!initializeXPathControl()) {
					xpathCombo.select(0);
					fExtensions.setText(DEFAULT_XML_EXT); //$NON-NLS-1$
					handleWidgetSelected();
				}
			}
			xpathCombo.setFocus();
			if (xpathProcessorViewer.getSelection().isEmpty()) {
				selectXPathProcessor(DefaultXPathProcessor.ID);
			}
		}
		updateOKStatus();
		super.setVisible(visible);
	}

	private void addXPathControls(Composite group) {
		// grid layout with 2 columns

		// Info text
		Label label = new Label(group, SWT.LEAD);
		label.setText(Messages.SearchPage_xpath_text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		label.setFont(group.getFont());

		// XPath combo
		xpathCombo = new Combo(group, SWT.SINGLE | SWT.BORDER);
		// Not done here to prevent page from resizing
		xpathCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleWidgetSelected();
				updateOKStatus();
			}
		});
		// add some listeners for XPath syntax checking
		xpathCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateOKStatus();
			}
		});
		xpathCombo.setFont(group.getFont());
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false,
				1, 1);
		data.widthHint = convertWidthInCharsToPixels(50);
		xpathCombo.setLayoutData(data);

		// Text line which explains the special characters
		fStatusLabel = new CLabel(group, SWT.LEAD);
		fStatusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		fStatusLabel.setFont(group.getFont());
		fStatusLabel.setAlignment(SWT.LEFT);
		fStatusLabel.setText(Messages.SearchPage_xpath_hint);
	}

	private void addXPathProcessorControls(Composite group) {
		// Info text
		Label label = new Label(group, SWT.LEAD);
		label.setText(Messages.SearchPage_XPathProcessor_text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		label.setFont(group.getFont());

		// XPathProcessor combo
		xpathProcessorViewer = new ComboViewer(group, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY);
		xpathProcessorViewer
				.setLabelProvider(new XPathProcessorLabelProvider());
		xpathProcessorViewer
				.setContentProvider(new XPathProcessorContentProvider());
		// Not done here to prevent page from resizing
		xpathProcessorViewer.getCombo().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// handleWidgetSelected();
						updateOKStatus();
					}
				});

		xpathProcessorViewer.getCombo().setFont(group.getFont());
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false,
				1, 1);
		data.widthHint = convertWidthInCharsToPixels(50);
		xpathProcessorViewer.getCombo().setLayoutData(data);
	}

	private String getExtensionFromEditor() {
		IEditorPart ep = getEditorPart();
		if (ep != null) {
			Object elem = ep.getEditorInput();
			if (elem instanceof IFileEditorInput) {
				String extension = ((IFileEditorInput) elem).getFile()
						.getFileExtension();
				if (extension == null)
					return ((IFileEditorInput) elem).getFile().getName();
				return "*." + extension; //$NON-NLS-1$
			}
		}
		return null;
	}

	private IEditorPart getEditorPart() {
		return XMLSearchUIPlugin.getActivePage().getActiveEditor();
	}

	private void addFileNameControls(Composite group) {
		// grid layout with 2 columns

		// Line with label, combo and button
		Label label = new Label(group, SWT.LEAD);
		label.setText(Messages.SearchPage_fileNamePatterns_text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		label.setFont(group.getFont());

		fExtensions = new Combo(group, SWT.SINGLE | SWT.BORDER);
		fExtensions.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateOKStatus();
			}
		});
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false,
				1, 1);
		data.widthHint = convertWidthInCharsToPixels(50);
		fExtensions.setLayoutData(data);
		fExtensions.setFont(group.getFont());

		Button button = new Button(group, SWT.PUSH);
		button.setText(Messages.SearchPage_browse);
		GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false, 1, 1);
		gridData.widthHint = SWTUtil.getButtonWidthHint(button);
		button.setLayoutData(gridData);
		button.setFont(group.getFont());

		fFileTypeEditor = new FileTypeEditor(fExtensions, button);

	}

	protected void handleWidgetSelected() {
		int selectionIndex = xpathCombo.getSelectionIndex();
		if (selectionIndex < 0
				|| selectionIndex >= fPreviousSearchPatterns.size())
			return;

		SearchPatternData patternData = (SearchPatternData) fPreviousSearchPatterns
				.get(selectionIndex);
		if (!xpathCombo.getText().equals(patternData.xpath))
			return;
		xpathCombo.setText(patternData.xpath);
		fFileTypeEditor.setFileTypes(patternData.fileNamePatterns);
		selectXPathProcessor(patternData.xpathProcessorId);
		if (patternData.workingSets != null)
			getContainer().setSelectedWorkingSets(patternData.workingSets);
		else
			getContainer().setSelectedScope(patternData.scope);

	}

	private void selectXPathProcessor(String xpathProcessorId) {
		IXPathProcessorType provider = XPathProcessorManager.getDefault()
				.getProcessor(xpathProcessorId);
		if (provider == null) {
			provider = XPathProcessorManager.getDefault().getProcessor(
					DefaultXPathProcessor.ID);
		}
		if (provider != null) {
			xpathProcessorViewer
					.setSelection(new StructuredSelection(provider));
		}
	}

	protected void updateOKStatus() {
		boolean valid = validateXPath();
		if (valid) {
			valid = validateXPathProcessor();
		}
		// boolean hasFilePattern= fExtensions.getText().length() > 0;
		getContainer().setPerformActionEnabled(valid);
	}

	private boolean validateXPathProcessor() {
		if (xpathProcessorViewer.getSelection().isEmpty()) {
			statusMessage(true,
					Messages.SearchPage_xpathProcessor_errorRequired); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private boolean validateXPath() {
		String xpath = xpathCombo.getText();
		if (xpath == null || xpath.length() < 1) {
			statusMessage(true, Messages.SearchPage_xpath_errorRequired); //$NON-NLS-1$
			return false;
		}
		IStatus status = XPathManager.getManager().validateXPath(
				getXPathProcessorId(), xpath);
		if (!status.isOK() && status.getException() != null) {
			String error = getErrorMessage(status.getException());
			statusMessage(true, error != null ? error : ""); // only take first
			// // line
			return false;
		}
		statusMessage(false, Messages.SearchPage_xpath_hint);
		return true;
	}

	private String getErrorMessage(Throwable e) {
		if (e == null) {
			return null;
		}
		String locMessage = e.getLocalizedMessage();
		if (locMessage == null || locMessage.length() < 1) {
			locMessage = e.getMessage();
		}
		if (locMessage == null || locMessage.length() < 1) {
			return getErrorMessage(e.getCause());
		} else {
			int i = 0;
			while (i < locMessage.length()
					&& "\n\r".indexOf(locMessage.charAt(i)) == -1) { //$NON-NLS-1$
				i++;
			}
			return locMessage.substring(0, i);
		}
	}

	public void setContainer(ISearchPageContainer container) {
		this.container = container;
	}

	private ISearchPageContainer getContainer() {
		return container;
	}

	private boolean getExtensions(Collection<String> extensions) {
		String fileType = null;
		String[] fileTypes = fFileTypeEditor.getFileTypes();
		for (int i = 0; i < fileTypes.length; i++) {
			fileType = fileTypes[i];
			if (fileType.equals("*") || fileType.equals("*.*")) {
				return true;
			}
			if (fileType.startsWith("*.")) {
				extensions.add(fileType.substring(2, fileType.length()));
			} else {
				extensions.add(fileType);
			}
		}
		return false;
	}

	private boolean initializeXPathControl() {
		ISelection selection = getSelection();
		if (selection instanceof ITextSelection && !selection.isEmpty()) {
			ITextSelection textSelection = ((ITextSelection) selection);
			String text = textSelection.getText();
			if (text != null && !text.isEmpty()) {
				ITextEditor textEditor = SearchUtil
						.getTextEditor(getEditorPart());
				if (textEditor != null) {
					IDOMNode selectedNode = DOMUtils
							.getSelectedNode((ITextEditor) textEditor);
					if (selectedNode != null) {
						namespaceInfos = XPathManager.getManager()
								.getNamespaceInfo(selectedNode);
						// Compute XPath
						String xpath = XPathManager
								.getManager()
								.computeBasicXPath(selectedNode, namespaceInfos);
						if (xpath != null) {
							xpathCombo.setText(xpath);
						}
					}
				}
				//

				// if (fIsRegExSearch)
				// fPattern.setText(FindReplaceDocumentAdapter.escapeForRegExPattern(text));
				// else
				// fPattern.setText(insertEscapeChars(text));
				//
				if (getPreviousExtensions().length > 0) {
					fExtensions.setText(getPreviousExtensions()[0]);
				} else {
					String extension = getExtensionFromEditor();
					if (extension != null)
						fExtensions.setText(extension);
					else
						fExtensions.setText(DEFAULT_XML_EXT); //$NON-NLS-1$
				}
				return true;
			}
		}
		return false;
	}

	private ISelection getSelection() {
		return container.getSelection();
	}

	/**
	 * Return search pattern data and update previous searches. An existing
	 * entry will be updated.
	 * 
	 * @return the search pattern data
	 */
	private SearchPatternData getPatternData() {
		SearchPatternData match = findInPrevious(xpathCombo.getText());
		if (match != null) {
			fPreviousSearchPatterns.remove(match);
		}
		match = new SearchPatternData(getXPath(), getXPathProcessorId(),
				fFileTypeEditor.getFileTypes(), getContainer()
						.getSelectedScope(), getContainer()
						.getSelectedWorkingSets());
		fPreviousSearchPatterns.add(0, match);
		return match;
	}

	private String getXPath() {
		return xpathCombo.getText();
	}

	private String getXPathProcessorId() {
		ISelection selection = xpathProcessorViewer.getSelection();
		if (selection.isEmpty()) {
			return DefaultXPathProcessor.ID;
		}
		return ((IXPathProcessorType) ((IStructuredSelection) selection)
				.getFirstElement()).getId();
	}

	private SearchPatternData findInPrevious(String pattern) {
		for (SearchPatternData element : fPreviousSearchPatterns) {
			if (pattern.equals(element.xpath)) {
				return element;
			}
		}
		return null;
	}

	private String[] getPreviousExtensions() {
		List<String> extensions = new ArrayList<String>(
				fPreviousSearchPatterns.size());
		int size = fPreviousSearchPatterns.size();
		for (int i = 0; i < size; i++) {
			SearchPatternData data = (SearchPatternData) fPreviousSearchPatterns
					.get(i);
			String text = FileTypeEditor.typesToString(data.fileNamePatterns);
			if (!extensions.contains(text))
				extensions.add(text);
		}
		return (String[]) extensions.toArray(new String[extensions.size()]);
	}

	private String[] getPreviousSearchPatterns() {
		int size = fPreviousSearchPatterns.size();
		String[] patterns = new String[size];
		for (int i = 0; i < size; i++)
			patterns[i] = ((SearchPatternData) fPreviousSearchPatterns.get(i)).xpath;
		return patterns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	public void dispose() {
		writeConfiguration();
		super.dispose();
	}

	/**
	 * Returns the page settings for this Text search page.
	 * 
	 * @return the page settings to be used
	 */
	private IDialogSettings getDialogSettings() {
		return XMLSearchUIPlugin.getDefault().getDialogSettingsSection(
				PAGE_NAME);
	}

	/**
	 * Initializes itself from the stored page settings.
	 */
	private void readConfiguration() {
		IDialogSettings s = getDialogSettings();
		// fIsCaseSensitive= s.getBoolean(STORE_CASE_SENSITIVE);
		// fIsRegExSearch= s.getBoolean(STORE_IS_REG_EX_SEARCH);
		// fSearchDerived= s.getBoolean(STORE_SEARCH_DERIVED);

		try {
			int historySize = s.getInt(STORE_HISTORY_SIZE);
			for (int i = 0; i < historySize; i++) {
				IDialogSettings histSettings = s.getSection(STORE_HISTORY + i);
				if (histSettings != null) {
					SearchPatternData data = SearchPatternData
							.create(histSettings);
					if (data != null) {
						fPreviousSearchPatterns.add(data);
					}
				}
			}
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	/**
	 * Stores it current configuration in the dialog store.
	 */
	private void writeConfiguration() {
		IDialogSettings s = getDialogSettings();
		// s.put(STORE_CASE_SENSITIVE, fIsCaseSensitive);
		// s.put(STORE_IS_REG_EX_SEARCH, fIsRegExSearch);
		// s.put(STORE_SEARCH_DERIVED, fSearchDerived);

		int historySize = Math
				.min(fPreviousSearchPatterns.size(), HISTORY_SIZE);
		s.put(STORE_HISTORY_SIZE, historySize);
		for (int i = 0; i < historySize; i++) {
			IDialogSettings histSettings = s.addNewSection(STORE_HISTORY + i);
			SearchPatternData data = ((SearchPatternData) fPreviousSearchPatterns
					.get(i));
			data.store(histSettings);
		}
	}

	private void statusMessage(boolean error, String message) {
		fStatusLabel.setText(message);
		if (error)
			fStatusLabel.setForeground(JFaceColors.getErrorText(fStatusLabel
					.getDisplay()));
		else
			fStatusLabel.setForeground(null);
	}
}
