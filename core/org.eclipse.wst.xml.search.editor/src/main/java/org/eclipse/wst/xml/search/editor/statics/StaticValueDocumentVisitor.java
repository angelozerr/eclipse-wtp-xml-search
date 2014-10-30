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
package org.eclipse.wst.xml.search.editor.statics;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.core.statics.IStaticValueVisitor;

public class StaticValueDocumentVisitor implements IStaticValueVisitor {

	public void visit(Object selectedNode, IFile file, String matching,
			boolean startsWith, IStaticValueCollector collector) {
		final IFile documentFile = getFile(selectedNode, file);

		final IDocumentProvider provider = new TextFileDocumentProvider();
		try {
			provider.connect(documentFile);

			int startOffset = 0;
			boolean match = false;
			final IDocument jdoc = provider.getDocument(documentFile);
			int nol = jdoc.getNumberOfLines();
			for (int lidx = 0; lidx < nol; lidx++) {
				IRegion li = jdoc.getLineInformation(lidx);
				String line = jdoc.get(li.getOffset(), li.getLength());
				startOffset = li.getOffset();
				String[] ss = line.split("\"");
				if (ss.length > 1) {
					for (int i = 1; i < ss.length; i += 2) {
						if (i == 1) {
							startOffset += ss[0].length();
						}
						final String proposedWid = ss[i];
						if (proposedWid.length() > 0
								&& !proposedWid.contains(" ")) {
							match = (startsWith ? proposedWid
									.startsWith(matching) : proposedWid
									.equals(matching));
							if (match) {
								collector.add(new StaticValueDocument(
										proposedWid, line, startOffset + 1,
										proposedWid.length(), documentFile));
							} else {
								startOffset += proposedWid.length();
							}

							// proposals.add(new
							// CustomCompletionProposal(proposedWid,
							// rtr.getOffset(), existingWid.length(),
							// rtr.getOffset(), img, proposedWid, null,
							// line.replaceAll("\"" + proposedWid + "\"",
							// "\"<b>" + proposedWid + "</b>\""), 0, true));

						} else {
							startOffset += proposedWid.length();
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			provider.disconnect(documentFile);
		}
	}

	protected IFile getFile(Object selectedNode, IFile file) {
		return file;
	}

}
