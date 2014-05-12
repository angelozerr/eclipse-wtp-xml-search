/*******************************************************************************
 * Copyright (c) 2005, 2007 Spring IDE Developers and others
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *         
 *******************************************************************************/
package org.eclipse.wst.xml.search.editor.internal.contentassist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.jdt.internal.corext.util.TypeFilter;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProposal;
import org.eclipse.jdt.internal.ui.text.java.LazyJavaTypeCompletionProposal;
import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.text.java.CompletionProposalComparator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.search.editor.contentassist.IContentAssistProposalRecorder;
import org.eclipse.wst.xml.search.editor.internal.jdt.SuperTypeHierarchyCache;
import org.eclipse.wst.xml.search.editor.internal.util.EditorUtils;
import org.eclipse.wst.xml.search.editor.util.JdtUtils;

public class JavaCompletionUtils {

	private static final CompletionProposalComparator COMPARATOR = new CompletionProposalComparator();
	private static ILabelProvider JAVA_LABEL_PROVIDER;
	public static final int FLAG_INTERFACE = 4;
	public static final int FLAG_CLASS = 8;
	public static final int FLAG_PACKAGE = 16;

	static {
		JAVA_LABEL_PROVIDER = new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_DEFAULT | 2048);
	}

	public static void addClassValueProposals(String prefix, IFile file,
			IContentAssistProposalRecorder recorder) {
		if (prefix == null || prefix.length() == 0)
			return;

		try {
			ICompilationUnit unit = createSourceCompilationUnit(file, prefix);
			char enclosingChar = prefix.lastIndexOf('$') <= 0 ? '.' : '$';
			prefix = prefix.replace('$', '.');
			if (prefix.lastIndexOf('.') > 0) {
				String rootClass = prefix.substring(0, prefix.lastIndexOf('.'));
				IType type = JdtUtils.getJavaType(file.getProject(), rootClass);
				if (type != null) {
					IType aitype[];
					int j = (aitype = type.getTypes()).length;
					for (int i = 0; i < j; i++) {
						IType innerType = aitype[i];
						if (Flags.isPrivate(innerType.getFlags())
								&& innerType.getFullyQualifiedName('.')
										.startsWith(prefix))
							recorder
									.recordProposal(
											JAVA_LABEL_PROVIDER
													.getImage(innerType),
											10,
											JAVA_LABEL_PROVIDER
													.getText(innerType),
											innerType
													.getFullyQualifiedName(enclosingChar),
											innerType);
					}

				}
			}
			String sourceStart = (new StringBuilder(
					"public class _xxx {\n    public void main(String[] args) {\n        "))
					.append(prefix).toString();
			String packageName = null;
			int dot = prefix.lastIndexOf('.');
			if (dot > -1) {
				packageName = prefix.substring(0, dot);
				sourceStart = (new StringBuilder("package ")).append(
						packageName).append(";\n").append(sourceStart)
						.toString();
			}
			String source = (new StringBuilder(String.valueOf(sourceStart)))
					.append("\n    }\n}").toString();
			setContents(unit, source);
			JavaCompletionProposalCollector collector = new JavaCompletionProposalCollector(
					unit);
			unit.codeComplete(sourceStart.length(), collector,
					DefaultWorkingCopyOwner.PRIMARY);
			org.eclipse.jdt.ui.text.java.IJavaCompletionProposal props[] = collector
					.getJavaCompletionProposals();
			ICompletionProposal proposals[] = order(props);
			ICompletionProposal aicompletionproposal[];
			int l = (aicompletionproposal = proposals).length;
			for (int k = 0; k < l; k++) {
				ICompletionProposal comProposal = aicompletionproposal[k];
				processJavaCompletionProposal(recorder, comProposal,
						packageName, enclosingChar);
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	private static ICompilationUnit createSourceCompilationUnit(IFile file,
			String prefix) throws JavaModelException {
		IProgressMonitor progressMonitor = EditorUtils.getProgressMonitor();
		IJavaProject project = JavaCore.create(file.getProject());
		IPackageFragment root = getPackageFragment(project, prefix);
		ICompilationUnit unit = root.getCompilationUnit("_xxx.java")
				.getWorkingCopy(
						CompilationUnitHelper.getInstance()
								.getWorkingCopyOwner(),
						CompilationUnitHelper.getInstance()
								.getProblemRequestor(), progressMonitor);
		progressMonitor.done();
		return unit;
	}

	private static IPackageFragment getPackageFragment(IJavaProject project,
			String prefix) throws JavaModelException {
		int dot = prefix.lastIndexOf('.');
		if (dot > -1) {
			String packageName = prefix.substring(0, dot);
			IPackageFragmentRoot aipackagefragmentroot1[];
			int l = (aipackagefragmentroot1 = project.getPackageFragmentRoots()).length;
			for (int j = 0; j < l; j++) {
				IPackageFragmentRoot root = aipackagefragmentroot1[j];
				IPackageFragment p = root.getPackageFragment(packageName);
				if (p != null && p.exists())
					return p;
			}

			IPackageFragment packages[] = project.getPackageFragments();
			IPackageFragment aipackagefragment[];
			int j1 = (aipackagefragment = packages).length;
			for (int i1 = 0; i1 < j1; i1++) {
				IPackageFragment p = aipackagefragment[i1];
				if (p.getElementName().equals(packageName))
					return p;
			}

		} else {
			IPackageFragmentRoot aipackagefragmentroot[];
			int k = (aipackagefragmentroot = project
					.getAllPackageFragmentRoots()).length;
			for (int i = 0; i < k; i++) {
				IPackageFragmentRoot p = aipackagefragmentroot[i];
				if (p.getKind() == 1)
					return p.getPackageFragment("");
			}

		}
		return project.getPackageFragments()[0];
	}

	private static ICompletionProposal[] order(ICompletionProposal proposals[]) {
		Arrays.sort(proposals, COMPARATOR);
		return proposals;
	}

	private static void processJavaCompletionProposal(
			IContentAssistProposalRecorder recorder,
			ICompletionProposal comProposal, String packageName,
			char enclosingChar) {
		if (comProposal instanceof JavaCompletionProposal) {
			JavaCompletionProposal prop = (JavaCompletionProposal) comProposal;
			recordProposal(recorder, prop.getImage(), prop.getRelevance(), prop
					.getDisplayString(), prop.getReplacementString(), prop
					.getJavaElement());
		} else if (comProposal instanceof LazyJavaTypeCompletionProposal) {
			LazyJavaTypeCompletionProposal prop = (LazyJavaTypeCompletionProposal) comProposal;
			if (prop.getQualifiedTypeName().equals(
					(new StringBuilder(String.valueOf(packageName)))
							.append(".").append("_xxx").toString())
					|| prop.getQualifiedTypeName().equals("_xxx"))
				return;
			if (prop.getJavaElement() instanceof IType) {
				if (TypeFilter.isFiltered((IType) prop.getJavaElement()))
					return;
				String replacementString = ((IType) prop.getJavaElement())
						.getFullyQualifiedName(enclosingChar);
				recordProposal(recorder, prop.getImage(), prop.getRelevance(),
						prop.getDisplayString(), replacementString, prop
								.getJavaElement());
			}
		}
	}

	private static void recordProposal(IContentAssistProposalRecorder recorder,
			Image image, int relevance, String displayText, String replaceText,
			Object proposedObject) {
		String s = null;
		if (proposedObject instanceof IMember) {
			s = new ProposalInfo((IMember) proposedObject).getInfo(EditorUtils.getProgressMonitor());
		}
		recorder.recordProposal(image, relevance, displayText, replaceText, s);
	}

	private static void setContents(ICompilationUnit cu, String source) {
		if (cu == null)
			return;
		synchronized (cu) {
			IBuffer buffer;
			try {
				buffer = cu.getBuffer();
			} catch (JavaModelException e) {
				e.printStackTrace();
				buffer = null;
			}
			if (buffer != null)
				buffer.setContents(source);
		}
	}

	public static void addTypeHierachyAttributeValueProposals(String prefix,
			IFile file, IContentAssistProposalRecorder recorder, IType type,
			int flags) {
		// String prefix = context.getMatchString();
		// if (prefix == null || prefix.length() == 0)
		// return;
		// IType type = JdtUtils.getJavaType(file.getProject(),
		// typeName);
		try {
			if (type != null
					&& file.getProject().hasNature(
							"org.eclipse.jdt.core.javanature")
					&& !TypeFilter.isFiltered(type)) {
				ITypeHierarchy hierachy = SuperTypeHierarchyCache
						.getTypeHierarchy(type, EditorUtils
								.getProgressMonitor());
				// type.newTypeHierarchy(JavaCore
				// .create(file.getProject()),
				// new NullProgressMonitor());
				IType types[] = hierachy.getAllSubtypes(type);
				Map sortMap = new HashMap();
				IType aitype[];
				int j = (aitype = types).length;
				for (int i = 0; i < j; i++) {
					IType foundType = aitype[i];
					if ((foundType.getFullyQualifiedName().startsWith(prefix) || foundType
							.getElementName().startsWith(prefix))
							&& !sortMap.containsKey(foundType
									.getFullyQualifiedName())
							&& !Flags.isAbstract(foundType.getFlags())) {
						boolean accepted = false;
						if ((flags & 8) != 0
								&& !Flags.isInterface(foundType.getFlags()))
							accepted = true;
						else if ((flags & 4) != 0
								&& Flags.isInterface(foundType.getFlags()))
							accepted = true;
						if (accepted) {
							String displayText = null;
							if (foundType.getPackageFragment().getElementName().equals("")) {
								// handle the scenario where the class is in default package.
								displayText = String.valueOf(foundType.getElementName());
							} else {
								displayText = (new StringBuilder(String.valueOf(foundType.getElementName())))
									.append(" - ").append(foundType.getPackageFragment().getElementName()).toString();
							}
							// let the completion from wtp-xml search at the top of the list
							int refevance = 1000; 
							recordProposal(recorder, JavaPluginImages .get("org.eclipse.jdt.ui.class_obj.gif"),
								refevance, displayText, foundType.getFullyQualifiedName(), foundType);
							sortMap.put(foundType.getFullyQualifiedName(),foundType);
						}
					}
				}

			}
		} catch (JavaModelException _ex) {
		} catch (CoreException _ex) {
		}
	}
}
