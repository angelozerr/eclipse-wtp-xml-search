package org.eclipse.wst.xml.search.editor.statics;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.core.statics.IStaticValueVisitor;

public class JavaStringLiteralDocumentVisitor implements IStaticValueVisitor {

	public void visit(Object selectedNode, IFile file, final String matching,
			final boolean startsWith, final IStaticValueCollector collector) {
		final IFile javaFile = getFile(selectedNode, file);

		final ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setSource(JavaCore.createCompilationUnitFrom(javaFile));
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor visitor = new ASTVisitor() {

			public boolean visit(org.eclipse.jdt.core.dom.StringLiteral node) {

				ASTNode parent = accept(node);
				if (parent != null) {
					String stringValue = node.getLiteralValue();

					boolean match = (startsWith ? stringValue
							.startsWith(matching) : stringValue
							.equals(matching));
					if (match) {
						collector.add(new StaticValueDocument(stringValue,
								parent.toString(), node.getStartPosition() + 1,
								stringValue.length(), javaFile));
					}
				}

				return true;
			};

		};
		cu.accept(visitor);
	}

	protected ASTNode accept(StringLiteral node) {
		ASTNode parent = node.getParent();
		return (parent != null
				&& parent.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION ? parent
				: null);
	}

	protected IFile getFile(Object selectedNode, IFile file) {
		return file;
	}

}
