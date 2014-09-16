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
package org.eclipse.wst.xml.search.editor.core.references;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.core.namespaces.Namespaces;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.internal.references.XMLReferencePathFactory;
import org.eclipse.wst.xml.search.editor.core.internal.references.validators.XMLReferenceValidatorsManager;
import org.eclipse.wst.xml.search.editor.core.internal.searchers.expressions.XMLExpressionParserManager;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferenceTo.ToType;
import org.eclipse.wst.xml.search.editor.core.references.validators.DefaultDOMNodeValidator;
import org.eclipse.wst.xml.search.editor.core.references.validators.IXMLReferenceValidator;
import org.eclipse.wst.xml.search.editor.core.searchers.expressions.IXMLExpressionParser;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLReferencesManager extends
		AbstractReferencesManager<IXMLReference, Node> {

	private static final String XPATH_FACTORY_ID_ATTR = "xpathFactoryId";
	private static final String VALIDATOR_ID_ATTR = "validatorId";
	private static final String PARSER_ID_ATTR = "parserId";
	private static final String ID_ATTR = "id";

	private static final XMLReferencesManager INSTANCE = new XMLReferencesManager();
	private static final String XML_REFERENCES_EXTENSION_POINT = "xmlReferences";

	private Map<String, XMLReferenceContainer> referencesContainerByContentTypeId = null;

	private Collection<String> contentTypeIds = null;

	public static XMLReferencesManager getInstance() {
		return INSTANCE;
	}

	enum Direction {
		FROM, TO
	}

	// ---------- XML Refernce

	public IXMLReference getXMLReference(Node node, String contentTypeId) {
		if (node == null)
			return null;
		Collection<IXMLReference> references = getXMLReferences(node,
				Direction.FROM, contentTypeId);
		if (references == null) {
			return null;
		}
		Node testNode = getTestNode(node);
		for (IXMLReference reference : references) {
			if (match(testNode, node, reference.getFrom()))
				return reference;
		}
		return null;
	}

	private Node getTestNode(Node node) {
		short nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ((Attr) node).getOwnerElement();
		case Node.TEXT_NODE:
			return ((Text) node).getParentNode();
		}
		return node;
	}

	public boolean match(Node node, IXMLReferencePath referencePath) {
		return match(getTestNode(node), node, referencePath);
	}

	public boolean match(Node parentNode, Node node,
			IXMLReferencePath referencePath) {
		if (referencePath == null) {
			return false;
		}
		if (!referencePath.match(parentNode)) {
			return false;
		}
		Namespaces namespaces = referencePath.getNamespaces();
		if (namespaces == null) {
			return true;
		}
		if (referencePath.getTargetNodes()[0].indexOf(':') == -1) {
			return true;
		}
		return namespaces.match(node);
	}

	public Collection<IXMLReference> getXMLReferences(Node node,
			Direction direction, String contentTypeId) {
		loadXMLReferencesIfNeeded();
		if (contentTypeId == null) {
			List<IXMLReference> references = new ArrayList<IXMLReference>();
			Collection<XMLReferenceContainer> containers = referencesContainerByContentTypeId
					.values();
			for (XMLReferenceContainer container : containers) {
				Collection<IXMLReference> referencesByContainer = container
						.getXMLReferences(node, direction);
				if (referencesByContainer != null) {
					references.addAll(referencesByContainer);
				}
			}
			return references;
		}
		XMLReferenceContainer container = referencesContainerByContentTypeId
				.get(contentTypeId);
		return container == null ? null : container.getXMLReferences(node,
				direction);
	}

	// XML reference inversed

	public List<IXMLReference> getXMLReferenceInversed(Node node,
			String contentTypeId) {
		if (node == null)
			return null;
		Collection<IXMLReference> references = getXMLReferences(node,
				Direction.TO, null);
		if (references == null) {
			return null;
		}
		List<IXMLReference> matchedPaths = null;
		Node parentNode = getTestNode(node);
		for (IXMLReference reference : references) {
			Collection<IXMLReferenceTo> to = reference.getTo();
			for (IXMLReferenceTo referenceTo : to) {
				if (!(referenceTo.getType() == ToType.XML)) {
					continue;
				}
				IXMLReferencePath referencePath = (IXMLReferencePath) referenceTo;
				if (match(parentNode, node, referencePath)) {
					if (matchedPaths == null) {
						matchedPaths = new ArrayList<IXMLReference>();
					}
					if (!matchedPaths.contains(reference)) {
						matchedPaths.add(reference);
					}
				}

				continue;
			}
		}
		return matchedPaths;
	}

	public Collection<IXMLReferencePath> getXMLReferencesByPathTo(String path,
			String contentTypeId) {
		Collection<IXMLReferencePath> referencePaths = new ArrayList<IXMLReferencePath>();
		Collection<XMLReferenceContainer> containers = referencesContainerByContentTypeId
				.values();
		for (XMLReferenceContainer container : containers) {
			Collection<IXMLReferencePath> o = container
					.getXMLReferencesByPathTo(path);
			if (o != null) {
				referencePaths.addAll(o);
			}
		}
		//
		// XMLReferenceContainer container = referencesContainerByContentTypeId
		// .get(contentTypeId);
		// return container == null ? null : container
		// .getXMLReferencesByPathTo(path);
		return referencePaths;
	}

	private synchronized void loadXMLReferences() {
		if (referencesContainerByContentTypeId != null) {
			return;
		}
		this.contentTypeIds = new ArrayList<String>();
		Map<String, XMLReferenceContainer> referencesContainerByContentTypeId = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					getPluginId(), XML_REFERENCES_EXTENSION_POINT);
			referencesContainerByContentTypeId = new HashMap<String, XMLReferenceContainer>();
			addXMLReferences(referencesContainerByContentTypeId, cf);
		} else {
			referencesContainerByContentTypeId = new HashMap<String, XMLReferenceContainer>();
		}
		this.referencesContainerByContentTypeId = referencesContainerByContentTypeId;
		super.addRegistryListenerIfNeeded();
	}

	private synchronized void addXMLReferences(
			Map<String, XMLReferenceContainer> referencesContainerByContentTypeId,
			IConfigurationElement[] cf) {

		for (IConfigurationElement ce : cf) {
			if (REFERENCES_ELT.equals(ce.getName())) {
				String ids = ce
						.getAttribute(XMLReferencePathFactory.CONTENT_TYPE_IDS_ATTR);
				String[] defaultContentTypeIds = (ids != null ? ids.split(",")
						: StringUtils.EMPTY_ARRAY);
				// Update collection of contenttype ids supported with XML
				// reference.
				String contentTypeId = null;
				for (int i = 0; i < defaultContentTypeIds.length; i++) {
					contentTypeId = defaultContentTypeIds[i];
					if (!contentTypeIds.contains(contentTypeId)) {
						contentTypeIds.add(contentTypeId);
					}
				}

				String xpathFactoryId = ce.getAttribute(XPATH_FACTORY_ID_ATTR);
				for (IConfigurationElement referenceDecl : ce.getChildren()) {
					parseReferenceDecl(referenceDecl, defaultContentTypeIds,
							xpathFactoryId, referencesContainerByContentTypeId);
				}
			}
		}
	}

	private void parseReferenceDecl(
			IConfigurationElement referenceDecl,
			String[] defaultContentTypeIds,
			String defaultXpathFactoryId,
			Map<String, XMLReferenceContainer> referencesContainerByContentTypeId) {
		try {
			IXMLReference reference = null;
			IXMLReferenceTo referenceTo = null;
			IXMLReferenceValidator validator = getValidator(referenceDecl);
			if (validator == null) {
				validator = DefaultDOMNodeValidator.INSTANCE;
			}
			IXMLExpressionParser parser = getParser(referenceDecl);
			// reference declaration,
			// loop for to get from+to declaration
			for (IConfigurationElement fromOrTo : referenceDecl.getChildren()) {
				if (FROM_ELT.equals(fromOrTo.getName())) {
					reference = XMLReferencePathFactory.INSTANCE
							.createReference(fromOrTo, defaultContentTypeIds,
									validator, parser);

				} else if (reference != null) {
					referenceTo = XMLReferencePathFactory.INSTANCE.createTo(
							fromOrTo, reference);
					if (referenceTo != null) {
						reference.addTo(referenceTo);
					}
				}
			}
			if (reference != null) {
				registerXMLReference(reference,
						referencesContainerByContentTypeId);
			}
		} catch (Throwable t) {
			Trace.trace(Trace.SEVERE, "  Could not load references", t);
		}
	}

	private IXMLReferenceValidator getValidator(IConfigurationElement element) {
		String validatorId = element.getAttribute(VALIDATOR_ID_ATTR);
		if (StringUtils.isEmpty(validatorId)) {
			return null;
		}
		return XMLReferenceValidatorsManager.getDefault().getValidator(
				validatorId);
	}

	private IXMLExpressionParser getParser(IConfigurationElement element) {
		String parserId = element.getAttribute(PARSER_ID_ATTR);
		if (StringUtils.isEmpty(parserId)) {
			return null;
		}
		return XMLExpressionParserManager.getDefault().getParser(parserId);
	}

	@Override
	protected void handleExtensionDelta(IExtensionDelta delta) {
		if (referencesContainerByContentTypeId == null) // not loaded yet
			return;

		if (delta.getKind() == IExtensionDelta.ADDED) {
			IConfigurationElement[] cf = delta.getExtension()
					.getConfigurationElements();
			addXMLReferences(referencesContainerByContentTypeId, cf);
		} else {
			// TODO : remove references
		}
	}

	public void registerXMReference(IXMLReference reference) {
		loadXMLReferencesIfNeeded();
		registerXMLReference(reference, referencesContainerByContentTypeId);
	}

	private void registerXMLReference(
			IXMLReference reference,
			Map<String, XMLReferenceContainer> referencesContainerByContentTypeId) {
		String[] contentTypeIds = reference.getContentTypeIds();
		if (contentTypeIds != null) {
			for (String contentTypeId : contentTypeIds) {
				XMLReferenceContainer container = referencesContainerByContentTypeId
						.get(contentTypeId);
				if (container == null) {
					container = new XMLReferenceContainer(contentTypeId);
					referencesContainerByContentTypeId.put(contentTypeId,
							container);
				}
				container.addReference(reference);
			}
		}
	}

	@Override
	protected String getExtensionPoint() {
		return XML_REFERENCES_EXTENSION_POINT;
	}

	public Collection<IXMLReference> getAllReferencesWithOneReferenceTo(
			ToType toType) {
		loadXMLReferencesIfNeeded();
		List<IXMLReference> to = new ArrayList<IXMLReference>();
		Collection<XMLReferenceContainer> containers = referencesContainerByContentTypeId
				.values();
		for (XMLReferenceContainer container : containers) {
			to.addAll(container.getAllReferencesWithOneReferenceTo(toType));
		}
		return to;
	}

	public XMLReferenceContainer getXMLReferenceContainer(String contentTypeId) {
		loadXMLReferencesIfNeeded();
		return referencesContainerByContentTypeId.get(contentTypeId);
	}

	private void loadXMLReferencesIfNeeded() {
		if (referencesContainerByContentTypeId == null) {
			loadXMLReferences();
		}
	}

	public Collection<String> getContentTypeIds() {
		loadXMLReferencesIfNeeded();
		return contentTypeIds;
	}

	public Collection<IXMLReference> getXMLReferencesForToType(ToType toType) {
		loadXMLReferencesIfNeeded();
		List<IXMLReference> references = new ArrayList<IXMLReference>();
		Collection<XMLReferenceContainer> containers = referencesContainerByContentTypeId
				.values();
		for (XMLReferenceContainer container : containers) {
			references.addAll(container.getXMLReferencesForToType(toType));
		}
		return references;
	}
}
