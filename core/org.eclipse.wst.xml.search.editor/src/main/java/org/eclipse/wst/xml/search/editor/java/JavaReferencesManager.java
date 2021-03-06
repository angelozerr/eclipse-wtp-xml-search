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
package org.eclipse.wst.xml.search.editor.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.wst.xml.search.editor.internal.Trace;
import org.eclipse.wst.xml.search.editor.references.AbstractReferencesManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;

public class JavaReferencesManager extends
		AbstractReferencesManager<IJavaReference, IJavaElement> {

	private static final JavaReferencesManager INSTANCE = new JavaReferencesManager();
	private static final String JAVA_REFERENCES_EXTENSION_POINT = "javaReferences";

	public static JavaReferencesManager getInstance() {
		return INSTANCE;
	}

	private Collection<IJavaReference> references;

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (references == null) // not loaded yet
			return;

		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addJavaReferences(references, cf);
		} else {
			// TODO : remove references
		}
	}

	private synchronized void addJavaReferences(
			Collection<IJavaReference> references, IConfigurationElement[] cf) {
		for (IConfigurationElement ce : cf) {
			if (REFERENCES_ELT.equals(ce.getName())) {

				for (IConfigurationElement referenceDecl : ce.getChildren()) {
					parseReferenceDecl(referenceDecl, references);
				}
			}
		}
	}

	private void parseReferenceDecl(IConfigurationElement referenceDecl,
			Collection<IJavaReference> references) {
		try {
			IJavaReference reference = null;
			IXMLReferenceTo referenceTo = null;
			// IXMLExpressionParser parser = getParser(referenceDecl);
			// IXMLSearcher expressionSearcher = getSearcher(referenceDecl,
			// DEFAULT_SEARCHER_FOR_EXPRESSION);
			// reference declaration,
			// loop for to get from+to declaration
			for (IConfigurationElement fromOrTo : referenceDecl.getChildren()) {
				if (FROM_ELT.equals(fromOrTo.getName())) {
					reference = JavaReferencePathFactory.INSTANCE
							.createReference(fromOrTo);
				} else if (reference != null) {
					referenceTo = JavaReferencePathFactory.INSTANCE.createTo(
							fromOrTo, reference);
					if (referenceTo != null) {
						reference.addTo(referenceTo);
					}
				}
			}
			if (reference != null) {
				registerJavaReference(reference, references);
			}
		} catch (Throwable t) {
			Trace.trace(Trace.SEVERE, "  Could not load references", t);
		}
	}

	public void registerJavaReference(IJavaReference reference) {
		loadJavaReferencesIfNeeded();
		registerJavaReference(reference, references);
	}

	private void registerJavaReference(IJavaReference reference,
			Collection<IJavaReference> references) {
		references.add(reference);

	}

	@Override
	public IJavaReference getXMLReference(IJavaElement node,
			String contentTypeId) {
		loadJavaReferencesIfNeeded();
		for (IJavaReference reference : references) {
			if (reference.match(node)) {
				return reference;
			}
		}
		return null;
	}

	private void loadJavaReferencesIfNeeded() {
		if (references == null) {
			loadJavaReferences();
		}
	}

	private synchronized void loadJavaReferences() {
		if (references != null) {
			return;
		}
		Collection<IJavaReference> references = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					getPluginId(), JAVA_REFERENCES_EXTENSION_POINT);
			references = new ArrayList<IJavaReference>(cf.length);
			addJavaReferences(references, cf);
		} else {
			references = new ArrayList<IJavaReference>();
		}
		this.references = references;
		super.addRegistryListenerIfNeeded();
	}

	@Override
	protected String getExtensionPoint() {
		return JAVA_REFERENCES_EXTENSION_POINT;
	}
}
