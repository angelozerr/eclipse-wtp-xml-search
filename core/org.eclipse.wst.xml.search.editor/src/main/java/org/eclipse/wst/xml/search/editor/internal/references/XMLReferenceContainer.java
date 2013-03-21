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
package org.eclipse.wst.xml.search.editor.internal.references;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.xml.search.editor.internal.references.XMLReferencesManager.Direction;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo.ToType;
import org.eclipse.wst.xml.search.editor.references.filters.IXMLReferenceFilter;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class XMLReferenceContainer {

	private static final String TEXT_KEY = "text()";
	private final String contentTypeId;
	private final Collection<IXMLReference> allReferences;
	private final Map<String, Collection<IXMLReferencePath>> referencesById;
	private final Map<String, Collection<IXMLReference>> fromReferencesByTargetNode;
	private Map<String, Collection<IXMLReference>> fromReferencesWithFilterByTargetNode;
	private final Map<String, Collection<IXMLReference>> toReferencesByTargetNode;

	public XMLReferenceContainer(String contentTypeId) {
		this.contentTypeId = contentTypeId;
		this.allReferences = new ArrayList<IXMLReference>();
		this.referencesById = new HashMap<String, Collection<IXMLReferencePath>>();
		this.fromReferencesByTargetNode = new HashMap<String, Collection<IXMLReference>>();
		this.fromReferencesWithFilterByTargetNode = null;
		this.toReferencesByTargetNode = new HashMap<String, Collection<IXMLReference>>();
	}

	public Collection<IXMLReference> getAllXMLReferences() {
		return allReferences;
	}

	public Collection<IXMLReference> getXMLReferences(Node node,
			Direction direction) {
		short nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			String attrName = ((Attr) node).getName();
			// attrName can be have value "id" or "wicket:id".
			// For the second case, if prefix is linked to a DTD or XML Schema,
			// the prefix must be removed.
			//if (node.getNamespaceURI() != null) {
				// Node has namespace (ex : wicket:id where wicket prefix is
				// linked to the DTD , XML Schema of wicket).
				// Remove the prefix
				int indexPrefix = attrName.indexOf(":");
				if (indexPrefix != -1) {
					attrName = attrName.substring(indexPrefix + 1,
							attrName.length());
				}
			//}
			String key = getKey(attrName, null, node.getNamespaceURI());
			return getXMLReferences(node, key, direction);
		case Node.ELEMENT_NODE:
		case Node.TEXT_NODE:
			return getXMLReferences(node, TEXT_KEY, direction);
		}
		return null;
	}

	private Collection<IXMLReference> getXMLReferences(Node node,
			String nodeName, Direction direction) {
		if (nodeName == null) {
			return null;
		}
		if (direction == Direction.TO)
			return toReferencesByTargetNode.get(nodeName);
		if (fromReferencesWithFilterByTargetNode == null) {
			return fromReferencesByTargetNode.get(nodeName);
		}
		Collection<IXMLReference> allReferences = new ArrayList<IXMLReference>(
				fromReferencesByTargetNode.size());
		fromReferencesByTargetNode.get(nodeName);
		Collection<IXMLReference> noFfilteredReferences = fromReferencesByTargetNode
				.get(nodeName);
		if (noFfilteredReferences != null) {
			allReferences.addAll(noFfilteredReferences);
		}
		Collection<IXMLReference> filteredReferences = fromReferencesWithFilterByTargetNode
				.get(nodeName);
		if (filteredReferences != null) {
			for (IXMLReference reference : filteredReferences) {
				if (reference.getFrom().getFilter().accept(node)) {
					allReferences.add(reference);
				}
			}
		}
		return allReferences;
	}

	public Collection<IXMLReferencePath> getXMLReferencesByPathTo(String path) {
		return referencesById.get(path);
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public void addReference(IXMLReference reference) {
		// cache XML reference
		allReferences.add(reference);
		// cache "From" reference
		IXMLReferenceFilter filter = reference.getFrom().getFilter();
		if (filter != null) {
			if (fromReferencesWithFilterByTargetNode == null) {
				fromReferencesWithFilterByTargetNode = new HashMap<String, Collection<IXMLReference>>();
			}
			register(reference, reference.getFrom(),
					fromReferencesWithFilterByTargetNode);
		} else {
			register(reference, reference.getFrom(), fromReferencesByTargetNode);
		}
		// cache "To" reference
		Collection<IXMLReferenceTo> to = reference.getTo();
		for (IXMLReferenceTo referenceTo : to) {
			register(reference, referenceTo, toReferencesByTargetNode);

			if (referenceTo.getType() == ToType.XML) {
				IXMLReferencePath referencePath = (IXMLReferencePath) referenceTo;
				Collection<IXMLReferencePath> refTo = referencesById
						.get(referencePath.getKeyPath());
				if (refTo == null) {
					refTo = new ArrayList<IXMLReferencePath>();
					referencesById.put(referencePath.getKeyPath(), refTo);
				}
				refTo.add(reference.getFrom());
			}
		}
	}

	private void register(IXMLReference reference, IXMLReferenceTo referenceTo,
			Map<String, Collection<IXMLReference>> references) {
		if (referenceTo.getType() != ToType.XML)
			return;

		IXMLReferencePath path = (IXMLReferencePath) referenceTo;
		// Namespaces namespaces = path.getNamespaces();
		String[] targetNodes = path.getTargetNodes();
		String targetNode = null;
		for (int i = 0; i < targetNodes.length; i++) {
			targetNode = targetNodes[i];
			if (targetNode.startsWith("@")) {
				targetNode = targetNode.substring(1, targetNode.length());
			}
			int index = targetNode.indexOf(":");
			if (index != -1) {
				targetNode = targetNode.substring(index+1, targetNode.length());
			}
			Collection<IXMLReference> referencesByTargetNode = references
					.get(targetNode);
			if (referencesByTargetNode == null) {
				referencesByTargetNode = new ArrayList<IXMLReference>();

				references.put(targetNode, referencesByTargetNode);

				// if (namespaces != null) {
				//
				//
				//
				// // List<String> prefixs = namespaces.getPrefixs();
				// // for (String prefix : prefixs) {
				// // references.put(getKey(targetNode, prefix, null),
				// // referencesByTargetNode);
				// // }
				// // List<String> uris = namespaces.getURIs();
				// // for (String uri : uris) {
				// // references.put(getKey(targetNode, null, uri),
				// // referencesByTargetNode);
				// // }
				// } else {
				// references.put(targetNode, referencesByTargetNode);
				// }
			}
			referencesByTargetNode.add(reference);
		}
	}

	public Collection<IXMLReference> getAllReferencesWithOneReferenceTo(
			ToType toType) {
		Collection<IXMLReference> allTo = new ArrayList<IXMLReference>();
		Collection<Collection<IXMLReference>> allreferences = fromReferencesByTargetNode
				.values();
		for (Collection<IXMLReference> collection : allreferences) {
			for (IXMLReference reference : collection) {
				if (hasReferenceTo(reference, toType)) {
					allTo.add(reference);
				}
			}
		}
		if (fromReferencesWithFilterByTargetNode != null) {
			allreferences = fromReferencesWithFilterByTargetNode.values();
			for (Collection<IXMLReference> collection : allreferences) {
				for (IXMLReference reference : collection) {
					if (hasReferenceTo(reference, toType)) {
						allTo.add(reference);
					}
				}
			}
		}
		return allTo;
	}

	private boolean hasReferenceTo(IXMLReference reference, ToType toType) {
		List<IXMLReferenceTo> to = reference.getTo();
		for (IXMLReferenceTo referenceTo : to) {
			if (toType == referenceTo.getType()) {
				return true;
			}
		}
		return false;
	}

	public Collection<String> getContentTypeIdsForToType(ToType toType) {
		Collection<String> contentTypeIds = new ArrayList<String>();
		Collection<Collection<IXMLReference>> allReferences = fromReferencesByTargetNode
				.values();
		getContentTypeIdsForToType(toType, contentTypeIds, allReferences);
		if (fromReferencesWithFilterByTargetNode != null) {
			allReferences = fromReferencesWithFilterByTargetNode.values();
			getContentTypeIdsForToType(toType, contentTypeIds, allReferences);
		}
		return contentTypeIds;
	}

	private void getContentTypeIdsForToType(ToType toType,
			Collection<String> contentTypeIds,
			Collection<Collection<IXMLReference>> allReferences) {
		for (Collection<IXMLReference> references : allReferences) {
			for (IXMLReference reference : references) {
				if (hasReferenceTo(reference, toType)) {
					String contentTypeId = null;
					for (int i = 0; i < reference.getContentTypeIds().length; i++) {
						contentTypeId = reference.getContentTypeIds()[i];
						if (contentTypeIds.contains(contentTypeId)) {
							contentTypeIds.add(contentTypeId);
						}
					}
				}
			}
		}
	}

	public Collection<IXMLReference> getXMLReferencesForToType(ToType toType) {
		Collection<IXMLReference> referencesForToType = new ArrayList<IXMLReference>();
		Collection<Collection<IXMLReference>> allReferences = fromReferencesByTargetNode
				.values();
		getXMLReferencesForToType(toType, referencesForToType, allReferences);
		if (fromReferencesWithFilterByTargetNode != null) {
			allReferences = fromReferencesWithFilterByTargetNode.values();
			getXMLReferencesForToType(toType, referencesForToType,
					allReferences);
		}
		return referencesForToType;
	}

	private void getXMLReferencesForToType(ToType toType,
			Collection<IXMLReference> referencesForToType,
			Collection<Collection<IXMLReference>> allReferences) {
		for (Collection<IXMLReference> references : allReferences) {
			for (IXMLReference reference : references) {
				if (hasReferenceTo(reference, toType)) {
					referencesForToType.add(reference);
				}
			}
		}
	}

	private String getKey(String nodeName, String prefix, String namespaceURI) {
		if (prefix == null && namespaceURI == null) {
			return nodeName;
		}
//		StringBuilder key = new StringBuilder();
//		if (prefix != null) {
//			key.append(prefix);
//			key.append(":");
//		} else {
//			key.append(namespaceURI);
//			key.append("#");
//		}
//		key.append(nodeName);
//		return key.toString();
		return nodeName;
	}

}
