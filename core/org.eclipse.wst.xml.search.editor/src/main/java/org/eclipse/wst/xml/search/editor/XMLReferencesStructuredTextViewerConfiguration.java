/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;
import org.eclipse.wst.sse.ui.internal.ExtendedConfigurationBuilder;
import org.eclipse.wst.sse.ui.internal.SSEUIPlugin;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.sse.ui.internal.taginfo.AnnotationHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.BestMatchHover;
import org.eclipse.wst.sse.ui.internal.taginfo.ProblemAnnotationHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.TextHoverManager;
import org.eclipse.wst.xml.core.text.IXMLPartitions;
import org.eclipse.wst.xml.search.editor.hover.XMLReferencesInfoHoverProcessor;
import org.eclipse.wst.xml.search.editor.hyperlink.XMLReferencesHyperlinkDetector;
import org.eclipse.wst.xml.search.editor.internal.contentassist.XMLReferencesContentAssistProcessor;
import org.eclipse.wst.xml.search.editor.internal.style.LineStyleProviderForXMLReferences;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.eclipse.wst.xml.ui.internal.style.LineStyleProviderForXML;

public class XMLReferencesStructuredTextViewerConfiguration extends
		StructuredTextViewerConfigurationXML {

	private static final IHyperlinkDetector[] IHYPERLINK_DETECTOR_EMPTY = new IHyperlinkDetector[0];

	private LineStyleProvider fLineStyleProviderForXML;

	@Override
	public IContentAssistProcessor[] getContentAssistProcessors(
			ISourceViewer sourceViewer, String partitionType) {
		IContentAssistProcessor processors[];
		if (partitionType == IStructuredPartitions.DEFAULT_PARTITION
				|| partitionType == IXMLPartitions.XML_DEFAULT)
			processors = (new IContentAssistProcessor[] { createContentAssistProcessor() });
		else
			processors = super.getContentAssistProcessors(sourceViewer,
					partitionType);
		return processors;
	}

	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		if (sourceViewer == null
				|| !fPreferenceStore.getBoolean("hyperlinksEnabled"))
			return null;
		List<IHyperlinkDetector> allDetectors = new ArrayList<IHyperlinkDetector>();
		allDetectors.add(createHyperlinkDetector());
		IHyperlinkDetector superDetectors[] = super
				.getHyperlinkDetectors(sourceViewer);
		IHyperlinkDetector aihyperlinkdetector[];
		int j = (aihyperlinkdetector = superDetectors).length;
		for (int i = 0; i < j; i++) {
			IHyperlinkDetector detector = aihyperlinkdetector[i];
			if (!allDetectors.contains(detector))
				allDetectors.add(detector);
		}
		return allDetectors.toArray(IHYPERLINK_DETECTOR_EMPTY);
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		ITextHover textHover = null;

		/*
		 * Returns a default problem, annotation, and best match hover depending
		 * on stateMask
		 */
		TextHoverManager.TextHoverDescriptor[] hoverDescs = SSEUIPlugin
				.getDefault().getTextHoverManager().getTextHovers();
		int i = 0;
		while (i < hoverDescs.length && textHover == null) {
			if (hoverDescs[i].isEnabled()
					&& computeStateMask(hoverDescs[i].getModifierString()) == stateMask) {
				String hoverType = hoverDescs[i].getId();
				if (TextHoverManager.PROBLEM_HOVER.equalsIgnoreCase(hoverType))
					textHover = new ProblemAnnotationHoverProcessor();
				else if (TextHoverManager.ANNOTATION_HOVER
						.equalsIgnoreCase(hoverType))
					textHover = new AnnotationHoverProcessor();
				else if (TextHoverManager.COMBINATION_HOVER
						.equalsIgnoreCase(hoverType))
					textHover = new BestMatchHover(
							createDocumentationHover(contentType));
				else if (TextHoverManager.DOCUMENTATION_HOVER
						.equalsIgnoreCase(hoverType)) {
					textHover = createDocumentationHover(contentType);
				}
			}
			i++;
		}
		return textHover;
	}

	protected ITextHover createDocumentationHover(String partitionType) {
		ITextHover textHover = null;
		if (partitionType == IStructuredPartitions.DEFAULT_PARTITION
				|| partitionType == IXMLPartitions.XML_DEFAULT) {
			return new XMLReferencesInfoHoverProcessor();
		}
		Object extendedTextHover = ExtendedConfigurationBuilder.getInstance()
				.getConfiguration(
						ExtendedConfigurationBuilder.DOCUMENTATIONTEXTHOVER,
						partitionType);
		if (extendedTextHover instanceof ITextHover) {
			textHover = (ITextHover) extendedTextHover;
		}
		return textHover;
	}

	protected IContentAssistProcessor createContentAssistProcessor() {
		return new XMLReferencesContentAssistProcessor();
	}

	protected IHyperlinkDetector createHyperlinkDetector() {
		return new XMLReferencesHyperlinkDetector();
	}

	@Override
	public LineStyleProvider[] getLineStyleProviders(
			ISourceViewer sourceViewer, String partitionType) {
		LineStyleProvider[] providers = null;
		if ((partitionType == IXMLPartitions.XML_DEFAULT)
				|| (partitionType == IXMLPartitions.XML_CDATA)
				|| (partitionType == IXMLPartitions.XML_COMMENT)
				|| (partitionType == IXMLPartitions.XML_DECLARATION)
				|| (partitionType == IXMLPartitions.XML_PI)) {
			providers = new LineStyleProvider[] { getLineStyleProviderForXML(sourceViewer) };
		}
		return providers;
	}

	private LineStyleProvider getLineStyleProviderForXML(
			ISourceViewer sourceViewer) {
		if (fLineStyleProviderForXML == null) {
			if (isColorReferencedNodes()) {
				fLineStyleProviderForXML = new LineStyleProviderForXMLReferences( sourceViewer );
			} else {
				fLineStyleProviderForXML = new LineStyleProviderForXML();
			}
		}
		return fLineStyleProviderForXML;
	}

	protected boolean isColorReferencedNodes() {
		return true;
	}

}
