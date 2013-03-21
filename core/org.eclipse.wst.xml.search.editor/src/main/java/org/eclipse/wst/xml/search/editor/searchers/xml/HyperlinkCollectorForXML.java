package org.eclipse.wst.xml.search.editor.searchers.xml;

import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.editor.internal.hyperlink.DOMNodeHyperlink;
import org.eclipse.wst.xml.search.editor.searchers.AbstractHyperlinkCollector;

public class HyperlinkCollectorForXML extends AbstractHyperlinkCollector
		implements IXMLSearchDOMNodeCollector {

	private final int startOffset;
	private final int endOffset;

	public HyperlinkCollectorForXML(IRegion hyperlinkRegion,
			List<IHyperlink> hyperLinks, int startOffset, int endOffset) {
		super(hyperlinkRegion, hyperLinks);
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public boolean add(IDOMNode node) {
		hyperLinks.add(new DOMNodeHyperlink(hyperlinkRegion, node, startOffset,
				endOffset));
		return true;
	}
}