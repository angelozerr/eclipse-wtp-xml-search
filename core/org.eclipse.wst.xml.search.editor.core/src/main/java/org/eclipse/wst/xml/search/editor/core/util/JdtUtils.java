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
package org.eclipse.wst.xml.search.editor.core.util;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.core.NamedMember;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.eclipse.wst.xml.search.editor.core.jdt.SuperTypeHierarchyCache;
import org.eclipse.wst.xml.search.editor.core.searchers.javamethod.requestor.IJavaMethodRequestor;

/**
 * Utility class that provides several helper methods for working with Eclipse's
 * JDT.
 * 
 */
public class JdtUtils {

	public static IType[] EMPTY_TYPE = new IType[0];

	/**
	 * 
	 * Returns the corresponding Java project or <code>null</code> a for given
	 * project.
	 * 
	 * @param project
	 *            the project the Java project is requested for
	 * 
	 * @return the requested Java project or <code>null</code> if the Java
	 *         project is not defined or the project is not
	 * 
	 *         accessible
	 */
	public static IJavaProject getJavaProject(IProject project) {
		if (project.isAccessible())
			try {
				if (project.hasNature(JavaCore.NATURE_ID))
					return (IJavaProject) project.getNature(JavaCore.NATURE_ID);
			} catch (CoreException e) {
				Trace.trace(Trace.SEVERE,
						(new StringBuilder(
								"Error getting Java project for project '"))
								.append(project.getName()).append("'")
								.toString(), e);
			}
		return null;
	}

	public static IFolder getJavaProjectOutputFolder(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if (javaProject != null) {
			try {
				return ResourcesPlugin.getWorkspace().getRoot()
						.getFolder(javaProject.getOutputLocation());
			} catch (JavaModelException e) {
				Trace.trace(Trace.SEVERE, (new StringBuilder(
						"Error getting Java project output for project '"))
						.append(project.getName()).append("'").toString(), e);
			}
		}
		return null;
	}

	public static IResource[] getJavaProjectSrcFolders(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if (javaProject != null) {
			try {
				List<IResource> sources = new ArrayList<IResource>();
				IPackageFragmentRoot[] roots = javaProject
						.getPackageFragmentRoots();
				IPackageFragmentRoot root = null;
				for (int i = 0; i < roots.length; i++) {
					root = roots[i];
					if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
						sources.add(root.getCorrespondingResource());
					}
				}
				return sources.toArray(IMultiResourceProvider.EMPTY_RESOURCE);
			} catch (JavaModelException e) {
				Trace.trace(Trace.SEVERE, (new StringBuilder(
						"Error getting Java project src for project '"))
						.append(project.getName()).append("'").toString(), e);
			}
		}
		return null;
	}

	public static String getPackageName(IContainer container) {
		IJavaProject javaProject = getJavaProject(container.getProject());
		if (javaProject != null) {
			String containerBasePath = container.getFullPath().toString();
			IResource[] srcs = getJavaProjectSrcFolders(container.getProject());
			IResource src = null;
			for (int i = 0; i < srcs.length; i++) {
				src = srcs[i];
				String srcBasePath = src.getFullPath().toString();
				if (containerBasePath.startsWith(srcBasePath)) {
					String packageName = containerBasePath.substring(
							srcBasePath.length() + 1,
							containerBasePath.length());
					return packageName.replaceAll("/", ".");
				}
			}
			return null;
		}
		return null;
	}

	public static IJarEntryResource getJavaResourceFileFromBinary(
			IProject project, String jarNamePattern, String packageName,
			String fileName) {
		IJavaProject javaProject = getJavaProject(project);
		if (javaProject != null) {
			try {
				IPackageFragmentRoot[] roots = javaProject
						.getPackageFragmentRoots();
				IPackageFragmentRoot root = null;
				for (int i = 0; i < roots.length; i++) {
					root = roots[i];
					if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
						if (StringUtils.isEmpty(jarNamePattern)
								|| root.getElementName().startsWith(
										jarNamePattern)) {
							Object[] nonJavaResources = null;
							if (packageName != null) {
								IPackageFragment fragment = root
										.getPackageFragment(packageName);
								if (fragment != null) {
									nonJavaResources = fragment
											.getNonJavaResources();
								}
							} else {
								nonJavaResources = root.getNonJavaResources();
							}
							if (nonJavaResources != null) {
								Object nonJavaResource = null;
								for (int j = 0; j < nonJavaResources.length; j++) {
									nonJavaResource = nonJavaResources[j];
									if (nonJavaResource instanceof IJarEntryResource) {
										IJarEntryResource r = (IJarEntryResource) nonJavaResource;
										if (r.isFile()
												&& fileName.equals(r.getName())) {
											return r;
										}
									}
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				Trace.trace(Trace.SEVERE, (new StringBuilder(
						"Error getting Java project src for project '"))
						.append(project.getName()).append("'").toString(), e);
			}
		}
		return null;
	}

	public static IJarEntryResource[] getJavaResourcesFileFromBinary(
			IProject project, String jarNamePattern, String packageName,
			String fileName) {
		Collection<IJarEntryResource> files = new ArrayList<IJarEntryResource>();
		IJavaProject javaProject = getJavaProject(project);
		if (javaProject != null) {
			try {
				IPackageFragmentRoot[] roots = javaProject
						.getPackageFragmentRoots();
				IPackageFragmentRoot root = null;
				for (int i = 0; i < roots.length; i++) {
					root = roots[i];
					if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
						if (StringUtils.isEmpty(jarNamePattern)
								|| root.getElementName().contains(
										jarNamePattern)) {
							Object[] nonJavaResources = null;
							if (packageName != null) {
								IPackageFragment fragment = root
										.getPackageFragment(packageName);
								if (fragment != null) {
									nonJavaResources = fragment
											.getNonJavaResources();
								}
							} else {
								nonJavaResources = root.getNonJavaResources();
							}
							if (nonJavaResources != null) {
								Object nonJavaResource = null;
								for (int j = 0; j < nonJavaResources.length; j++) {
									nonJavaResource = nonJavaResources[j];
									if (nonJavaResource instanceof IJarEntryResource) {
										IJarEntryResource r = (IJarEntryResource) nonJavaResource;
										if (r.isFile()
												&& r.getName().contains(
														fileName)) {
											files.add(r);
										}
									}
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				Trace.trace(Trace.SEVERE, (new StringBuilder(
						"Error getting Java project src for project '"))
						.append(project.getName()).append("'").toString(), e);
			}
		}
		return files.toArray(new IJarEntryResource[files.size()]);
	}

	/**
	 * 
	 * Returns the corresponding Java type for given full-qualified class name.
	 * 
	 * @param project
	 *            the JDT project the class belongs to
	 * 
	 * @param className
	 *            the full qualified class name of the requested Java type
	 * 
	 * @return the requested Java type or null if the class is not defined or
	 *         the project is not accessible
	 */

	public static IType getJavaType(IProject project, String className) {
		IJavaProject javaProject = JdtUtils.getJavaProject(project);
		if (className != null) {
			// For inner classes replace '$' by '.'
			int pos = className.lastIndexOf('$');
			if (pos > 0) {
				className = className.replace('$', '.');
			}

			try {
				IType type = null;
				// First look for the type in the Java project
				if (javaProject != null) {
					// TODO CD not sure why we need
					type = javaProject.findType(className,
							new NullProgressMonitor());
					if (type != null) {
						return type;
					}
				}

				// Then look for the type in the referenced Java projects
				for (IProject refProject : project.getReferencedProjects()) {
					IJavaProject refJavaProject = JdtUtils
							.getJavaProject(refProject);
					if (refJavaProject != null) {
						type = refJavaProject.findType(className);
						if (type != null) {
							return type;
						}
					}
				}

				// fall back
				return null;
			} catch (CoreException e) {
				Trace.trace(Trace.SEVERE, "Error getting Java type '"
						+ className + "'", e);
			}
		}
		return null;
	}

	public static IType getSuperType(IType type) throws JavaModelException {
		if (type == null)
			return null;
		String name = type.getSuperclassName();
		if (name == null
				&& !type.getFullyQualifiedName().equals(Object.class.getName()))
			name = Object.class.getName();
		if (name != null) {
			if (type.isBinary())
				return type.getJavaProject().findType(name);
			String resolvedName = JdtUtils.resolveClassName(name, type);
			if (resolvedName != null)
				return type.getJavaProject().findType(resolvedName);
		}
		return null;
	}

	public static String resolveClassName(String className, IType type) {

		if (className == null || type == null) {
			return className;
		}

		// replace binary $ inner class name syntax with . for source level
		className = className.replace('$', '.');
		String dotClassName = new StringBuilder().append('.').append(className)
				.toString();
		IProject project = type.getJavaProject().getProject();
		try {
			// Special handling for some well-know classes
			if (className.startsWith("java.lang")
					&& getJavaType(project, className) != null) {
				return className;
			}

			// Check if the class is imported
			if (!type.isBinary()) {
				// Strip className to first segment to support
				// ReflectionUtils.MethodCallback
				int ix = className.lastIndexOf('.');
				String firstClassNameSegment = className;

				if (ix > 0) {
					firstClassNameSegment = className.substring(0, ix);
				}

				// Iterate the imports
				for (IImportDeclaration importDeclaration : type
						.getCompilationUnit().getImports()) {
					String importName = importDeclaration.getElementName();
					// Wildcard imports -> check if the package + className is a
					// valid type
					if (importDeclaration.isOnDemand()) {
						String newClassName = new StringBuilder(
								importName.substring(0, importName.length() - 1))
								.append(className).toString();
						if (getJavaType(project, newClassName) != null) {
							return newClassName;

						}
					}

					// Concrete import matching .className at the end -> check
					// if type exists
					else if (importName.endsWith(dotClassName)
							&& getJavaType(project, importName) != null) {
						return importName;
					}

					// Check if className is multi segmented
					// (ReflectionUtils.MethodCallback)
					// -> check if the first segment
					else if (!className.equals(firstClassNameSegment)) {
						if (importName.endsWith(firstClassNameSegment)) {
							String newClassName = new StringBuilder(
									importName.substring(0,
											importName.lastIndexOf('.') + 1))
									.append(className).toString();

							if (getJavaType(project, newClassName) != null) {
								return newClassName;
							}
						}
					}
				}
			}

			// Check if the class is in the same package as the type
			String packageName = type.getPackageFragment().getElementName();
			String newClassName = new StringBuilder(packageName).append(
					dotClassName).toString();

			if (getJavaType(project, newClassName) != null) {
				return newClassName;
			}

			// Check if the className is sufficient (already fully-qualified)
			if (getJavaType(project, className) != null) {
				return className;
			}

			// Check if the class is coming from the java.lang
			newClassName = new StringBuilder("java.lang").append(dotClassName)
					.toString();
			if (getJavaType(project, newClassName) != null) {
				return newClassName;
			}

			// Fall back to full blown resolution
			String[][] fullInter = type.resolveType(className);
			if (fullInter != null && fullInter.length > 0) {
				return fullInter[0][0] + "." + fullInter[0][1];
			}
		} catch (JavaModelException e) {
			Trace.trace(Trace.SEVERE, "Error resolveClassName'" + className
					+ "'", e);
		}
		return className;

	}

	public static String[] getParameterTypesString(IMethod method) {
		try {
			String parameterQualifiedTypes[] = Signature
					.getParameterTypes(method.getSignature());
			int length = parameterQualifiedTypes != null ? parameterQualifiedTypes.length
					: 0;
			String parameterPackages[] = new String[length];
			for (int i = 0; i < length; i++) {
				parameterQualifiedTypes[i] = parameterQualifiedTypes[i]
						.replace('/', '.');
				parameterPackages[i] = Signature
						.getSignatureSimpleName(parameterQualifiedTypes[i]);
			}

			return parameterPackages;
		} catch (IllegalArgumentException _ex) {
		} catch (JavaModelException _ex) {
		}
		return null;
	}

	public static String getReturnTypeString(IMethod method,
			boolean classTypesOnly) {
		try {
			String qualifiedReturnType = Signature.getReturnType(method
					.getSignature());
			if (!classTypesOnly || qualifiedReturnType.startsWith("L")
					|| qualifiedReturnType.startsWith("Q"))
				return Signature.getSignatureSimpleName(qualifiedReturnType
						.replace('/', '.'));
		} catch (IllegalArgumentException _ex) {
		} catch (JavaModelException _ex) {
		}
		return null;
	}

	public static String getPropertyNameFromMethodName(IMethod method) {
		String methodName = method.getElementName();
		int index = methodName.lastIndexOf('.');
		if (index > 0)
			methodName = methodName.substring(index + 1);
		String replaceText = methodName.substring("set".length());
		if (replaceText != null)
			replaceText = Introspector.decapitalize(replaceText);
		return replaceText;
	}

	public static IMethod findMethod(IType type, String methodNameToFind,
			IJavaMethodRequestor requestor) throws JavaModelException {
		if (type == null) {
			return null;
		}
		IMethod method = null;
		IMethod[] methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			IStatus status = requestor.matchTotally(null, methodNameToFind,
					method);
			if (status != null && status.isOK()) {
				return method;
			}
		}
		return findMethod(JdtUtils.getSuperType(type), methodNameToFind,
				requestor);
	}

	public static boolean doesImplement(IResource resource, IType type,
			IType superType) {
		if (resource == null || type == null || superType == null)
			return false;
		// if(className.startsWith("java.") || className.startsWith("javax."))
		// try
		// {
		// ClassLoader cls = getClassLoader(resource.getProject(), null);
		// Class typeClass = cls.loadClass(type.getFullyQualifiedName('$'));
		// Class interfaceClass = cls.loadClass(className);
		// return typeClass.equals(interfaceClass) ||
		// interfaceClass.isAssignableFrom(typeClass);
		// }
		// catch(Throwable _ex) { }
		return doesImplementWithJdt(resource, type, superType);
	}

	private static boolean doesImplementWithJdt(IResource resource, IType type,
			IType interfaceType) {
		// IType interfaceType = getJavaType(resource.getProject(), className);
		// if (interfaceType == null) {
		// return false;
		// }
		IType subTypes[];
		try {
			subTypes = SuperTypeHierarchyCache.getTypeHierarchy(interfaceType)
					.getAllSubtypes(interfaceType);
			if (subTypes != null) {
				for (IType subType : subTypes) {
					if (subType.equals(type)) {
						return true;
					}
				}
			}
		} catch (JavaModelException e) {
			Trace.trace(Trace.SEVERE, "Error doesImplementWithJdt", e);
		}
		return false;

	}

	public static IType[] getImplementsType(IType type) {
		try {
			return SuperTypeHierarchyCache.getTypeHierarchy(type)
					.getAllClasses();
		} catch (JavaModelException e) {
			return null;
		}
	}

	public static boolean isImplementsClass(final IJavaElement javaElement, final String className)
			throws JavaModelException {
		Assert.isNotNull(javaElement);
		if (javaElement != null && javaElement instanceof NamedMember) {
			final NamedMember method = (NamedMember) javaElement;
			final IType type = method.getDeclaringType();
			if (type != null) {
				if (type.getFullyQualifiedName().equals(
						className)) {
					return true;
				}
				return hierarchyContainsComponent(type, className);
			}
		}
		return false;
	}

	public static boolean hierarchyContainsComponent(final IType type, final String className)
			throws JavaModelException {
		Assert.isNotNull(type);
		final ITypeHierarchy hierarchy = type.newSupertypeHierarchy(null);
		if (hierarchy != null) {
			final IType[] supertypes = hierarchy.getAllSupertypes(type);
			for (final IType iType : supertypes) {
				if (iType.getFullyQualifiedName().equals(
						className)) {
					return true;
				}
			}
		}
		return false;
	}
}
