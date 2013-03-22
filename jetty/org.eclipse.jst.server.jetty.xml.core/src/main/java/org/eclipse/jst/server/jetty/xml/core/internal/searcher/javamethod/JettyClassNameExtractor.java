package org.eclipse.jst.server.jetty.xml.core.internal.searcher.javamethod;

import static org.eclipse.wst.xml.search.core.util.StringUtils.isEmpty;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.server.jetty.xml.core.JettyConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToXML;
import org.eclipse.wst.xml.search.editor.references.XMLReferencesUtil;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.AbstractClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.requestor.GetterJavaMethodRequestor;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;
import org.eclipse.wst.xml.search.editor.util.XMLSearcherForXMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JettyClassNameExtractor extends AbstractClassNameExtractor
		implements JettyConstants {

	public static final IClassNameExtractor INSTANCE = new JettyClassNameExtractor();

	@Override
	public String doExtractClassName(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		if (!(node.getNodeType() == Node.ELEMENT_NODE)) {
			return null;
		}
		Element element = ((Element) node);

		// 1) Search for class name attribute in the Element
		String className = element.getAttribute(CLASS_ATTR);
		if (!isEmpty(className)) {
			return className;
		}

		// 2) Search into PARENT Element
		Node parent = element.getParentNode();
		if (!(parent != null && parent.getNodeType() == Node.ELEMENT_NODE)) {
			return null;
		}
		Element parentElement = ((Element) parent);

		// 2.1) Search for class name attribute in the PARENT element
		className = parentElement.getAttribute(CLASS_ATTR);
		if (!isEmpty(className)) {
			return className;
		}

		String elementName = parentElement.getNodeName();
		if (REF_ELT.equals(elementName)) {
			IDOMAttr refIdAttr = (IDOMAttr) parentElement
					.getAttributeNode(ID_ATTR);
			if (refIdAttr == null) {
				return null;
			}
			IXMLReference reference = XMLReferencesUtil.getXMLReference(
					refIdAttr, file);
			if (reference == null) {
				return null;
			}
			IXMLReferenceToXML referenceToXML = (IXMLReferenceToXML) reference
					.getTo().get(0);
			// 2.1) Search for class name attribute in the Ref (referenced)
			// Element
			RefElementCollector collector = new RefElementCollector();
			XMLSearcherForXMLUtils.search(refIdAttr, null, file,
					referenceToXML, collector, false);
			IDOMElement refElement = collector.getRefElement();
			if (refElement != null) {
				className = refElement.getAttribute(CLASS_ATTR);
				if (!isEmpty(className)) {
					return className;
				}
			}
			return null;
		}

		if (GET_ELT.equals(elementName)) {
			String classNameForGet = doExtractClassName(parentElement, file,
					pathForClass, findByAttrName, findByParentNode,
					xpathFactoryProviderId, namespaceInfo);
			if (isEmpty(classNameForGet)) {
				return null;
			}
			String methodNameToFind = parentElement.getAttribute(NAME_ATTR);
			if (isEmpty(methodNameToFind)) {
				return null;
			}
			IType type = JdtUtils.getJavaType(file.getProject(),
					classNameForGet);
			if (type == null) {
				return null;
			}
			try {
				IMethod method = JdtUtils.findMethod(type, methodNameToFind,
						GetterJavaMethodRequestor.INSTANCE);
				if (method != null) {
					String returnType = method.getReturnType();
					if (returnType.startsWith("L")) {
						returnType = returnType.substring(1, returnType
								.length());
					}
					if (returnType.endsWith(";")) {
						returnType = returnType.substring(0, returnType
								.length() - 1);
					}
					return returnType;
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		return null;
	}

}
