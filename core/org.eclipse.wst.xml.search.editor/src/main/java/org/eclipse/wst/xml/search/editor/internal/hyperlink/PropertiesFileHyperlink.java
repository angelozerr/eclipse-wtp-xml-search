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

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.propertiesfileeditor.IPropertiesFilePartitions;
import org.eclipse.jdt.internal.ui.propertiesfileeditor.PropertyKeyHyperlinkDetector;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * JFace {@link IHyperlink} implementation for properties file {@link IStorage}.
 * 
 */
public class PropertiesFileHyperlink implements IHyperlink {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final IRegion region;
	private final IStorage propertiesFile;
	private final String keyName;
	private final IEditorPart editor;

	public PropertiesFileHyperlink(IRegion region, IStorage propertiesFile,
			String keyName, IEditorPart editor) {
		this.region = region;
		this.propertiesFile = propertiesFile;
		this.keyName = keyName;
		this.editor = editor;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return (new StringBuilder("Open '").toString());
	}

	public void open() {
		openKeyInPropertiesFile(keyName, propertiesFile, editor);

	}

	/**
	 * Calculates the region of the NLS key in the properties file and reveals
	 * it in editor.
	 * 
	 * @param keyName
	 *            the NLS key
	 * @param propertiesFile
	 *            the properties file, or <code>null</code>
	 * @param activeEditor
	 *            the active editor part
	 */
	public static void openKeyInPropertiesFile(String keyName,
			IStorage propertiesFile, IEditorPart activeEditor) {
		if (propertiesFile == null) {
			// showErrorInStatusLine(activeEditor,
			// JavaEditorMessages.Editor_OpenPropertiesFile_error_fileNotFound_dialogMessage);
			return;
		}

		IEditorPart editor;
		try {
			editor = EditorUtility.openInEditor(propertiesFile, true);
		} catch (PartInitException e) {
			handleOpenPropertiesFileFailed(propertiesFile, activeEditor);
			return;
		}

		// Reveal the key in the editor
		IEditorInput editorInput = editor.getEditorInput();
		IDocument document = null;
		if (editor instanceof ITextEditor)
			document = ((ITextEditor) editor).getDocumentProvider()
					.getDocument(editorInput);
		else {
			IFile file = (IFile) editorInput.getAdapter(IFile.class);
			if (file != null) {
				IPath path = file.getFullPath();
				ITextFileBufferManager manager = FileBuffers
						.getTextFileBufferManager();
				try {
					manager.connect(path, LocationKind.IFILE, null);
					try {
						ITextFileBuffer buffer = manager.getTextFileBuffer(
								path, LocationKind.IFILE);
						if (buffer != null)
							document = buffer.getDocument();
					} finally {
						manager.disconnect(path, LocationKind.IFILE, null);
					}
				} catch (CoreException ex) {
					JavaPlugin.log(ex);
				}
			}
		}

		// Find key in document
		boolean found = false;
		IRegion region = null;
		if (document != null) {
			FindReplaceDocumentAdapter finder = new FindReplaceDocumentAdapter(
					document);
			PropertyKeyHyperlinkDetector detector = new PropertyKeyHyperlinkDetector();
			detector.setContext(editor);
			String key = unwindEscapeChars(keyName);
			int offset = document.getLength() - 1;
			try {
				while (!found && offset >= 0) {
					region = finder
							.find(offset, key, false, true, false, false);
					if (region == null)
						offset = -1;
					else {
						// test whether it's the key
						IHyperlink[] hyperlinks = detector.detectHyperlinks(
								null, region, false);
						if (hyperlinks != null) {
							for (int i = 0; i < hyperlinks.length; i++) {
								IRegion hyperlinkRegion = hyperlinks[i]
										.getHyperlinkRegion();
								found = key.equals(document.get(
										hyperlinkRegion.getOffset(),
										hyperlinkRegion.getLength()));
							}
						} else if (document instanceof IDocumentExtension3) {
							// Fall back: test using properties file
							// partitioning
							ITypedRegion partition = null;
							partition = ((IDocumentExtension3) document)
									.getPartition(
											IPropertiesFilePartitions.PROPERTIES_FILE_PARTITIONING,
											region.getOffset(), false);
							found = IDocument.DEFAULT_CONTENT_TYPE
									.equals(partition.getType())
									&& key.equals(document.get(
											partition.getOffset(),
											partition.getLength()).trim());
						}
						// Prevent endless loop (panic code, shouldn't be
						// needed)
						if (offset == region.getOffset())
							offset = -1;
						else
							offset = region.getOffset();
					}
				}
			} catch (BadLocationException ex) {
				found = false;
			} catch (BadPartitioningException e1) {
				found = false;
			}
		}
		if (found)
			EditorUtility.revealInEditor(editor, region);
		else {
			EditorUtility.revealInEditor(editor, 0, 0);
			// showErrorInStatusLine(editor,
			// Messages.format(JavaEditorMessages.Editor_OpenPropertiesFile_error_keyNotFound,
			// keyName));
		}
	}

	/**
	 * Shows error message in status line if opening the properties file in
	 * editor fails.
	 * 
	 * @param propertiesFile
	 *            the propertiesFile
	 * @param editor
	 *            the editor part
	 */
	private static void handleOpenPropertiesFileFailed(IStorage propertiesFile,
			IEditorPart editor) {
		// showErrorInStatusLine(editor,
		// Messages.format(JavaEditorMessages.Editor_OpenPropertiesFile_error_openEditor_dialogMessage,
		// BasicElementLabels.getPathLabel(propertiesFile.getFullPath(),
		// true)));
	}

	public static String unwindEscapeChars(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			sb.append(getUnwoundString(c));
		}
		return sb.toString();
	}

	private static String getUnwoundString(char c) {
		switch (c) {
		case '\b':
			return "\\b";//$NON-NLS-1$
		case '\t':
			return "\\t";//$NON-NLS-1$
		case '\n':
			return "\\n";//$NON-NLS-1$
		case '\f':
			return "\\f";//$NON-NLS-1$
		case '\r':
			return "\\r";//$NON-NLS-1$

			// These can be used unescaped in properties file:
			// case '\"' :
			// return "\\\"";//$NON-NLS-1$
			// case '\'' :
			// return "\\\'";//$NON-NLS-1$

		case '\\':
			return "\\\\";//$NON-NLS-1$

			// This is only done when writing to the .properties file in
			// #unwindValue(String)
			// case '!':
			// return "\\!";//$NON-NLS-1$
			// case '#':
			// return "\\#";//$NON-NLS-1$

		default:
			if (((c < 0x0020) || (c > 0x007e))) {
				return new StringBuilder().append('\\').append('u')
						.append(toHex((c >> 12) & 0xF))
						.append(toHex((c >> 8) & 0xF))
						.append(toHex((c >> 4) & 0xF)).append(toHex(c & 0xF))
						.toString();

			} else
				return String.valueOf(c);
		}
	}

	private static char toHex(int halfByte) {
		return HEX_DIGITS[(halfByte & 0xF)];
	}

}
