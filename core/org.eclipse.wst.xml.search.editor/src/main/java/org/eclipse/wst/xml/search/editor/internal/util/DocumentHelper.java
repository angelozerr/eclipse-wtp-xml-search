package org.eclipse.wst.xml.search.editor.internal.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public class DocumentHelper {

	public static class StringArgument {

		private final IRegion region;
		private final String matchingString;

		public StringArgument(IRegion region, String matchingString) {
			this.region = region;
			this.matchingString = matchingString;
		}

		public IRegion getRegion() {
			return region;
		}

		public String getMatchingString() {
			return matchingString;
		}

	}

	public static StringArgument findStringArgument(IDocument document, int offset,
			boolean full) {
		boolean stop = false;
		StringBuilder matchingString = new StringBuilder("");
		IRegion region = null;
		String s = null;
		try {
			final IRegion li = document.getLineInformationOfOffset(offset);
			for (int i = offset - 1; i >= li.getOffset() && !stop; i--) {
				s = document.get(i, 1);
				if (s.equals("\"")) {
					stop = true;
				}
				if (!stop) {
					matchingString.insert(0, s);
				} else {
					for (int j = offset; j <= li.getOffset() + li.getLength(); j++) {
						s = document.get(j, 1);
						if (s.equals("\"")) {
							region = new Region(i + 1, j - i - 1);
							return new StringArgument(region, matchingString.toString());
						} else if (full) {
							matchingString.append(s);
						}
					}
				}
			}
		} catch (final BadLocationException e) {
		}		
		return null;
	}
}
