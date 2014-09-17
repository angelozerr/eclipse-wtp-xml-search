package org.apache.struts2.ide.validators.core.internal.search.javamethod;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.AbstractClassNameExtractor;
import org.eclipse.wst.xml.search.editor.searchers.javamethod.classnameprovider.IClassNameExtractor;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;
import org.w3c.dom.Node;

public class ValidatorClassNameExtractor extends AbstractClassNameExtractor {

	public static final IClassNameExtractor INSTANCE = new ValidatorClassNameExtractor();

	@Override
	public String[] doExtractClassNames(Node node, IFile file,
			String pathForClass, String findByAttrName,
			boolean findByParentNode, String xpathFactoryProviderId,
			NamespaceInfos namespaceInfo) throws XPathExpressionException {
		String fileName = file.getName();
		int index = fileName.indexOf('-');
		if (index == -1) {
			return null;
		}
		String className = fileName.substring(0, index);
		String packageName = JdtUtils.getPackageName(file.getParent());
		if (packageName != null) {
			return new String[] { packageName + "." + className };
		}
		return new String[] { className };
	}

}
