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
package org.eclipse.wst.xml.search.editor.internal.jdt.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.ui.search.ElementQuerySpecification;
import org.eclipse.jdt.ui.search.IMatchPresentation;
import org.eclipse.jdt.ui.search.IQueryParticipant;
import org.eclipse.jdt.ui.search.ISearchRequestor;
import org.eclipse.jdt.ui.search.PatternQuerySpecification;
import org.eclipse.jdt.ui.search.QuerySpecification;
import org.eclipse.wst.xml.search.core.IXMLSearchDOMNodeCollector;
import org.eclipse.wst.xml.search.core.SimpleXMLSearchEngine;
import org.eclipse.wst.xml.search.core.queryspecifications.IXMLQuerySpecification;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.eclipse.wst.xml.search.editor.internal.indexing.XMLReferencesIndexManager;
import org.eclipse.wst.xml.search.editor.internal.references.XMLReferencesManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceTo.ToType;
import org.eclipse.wst.xml.search.editor.references.validators.IXMLReferenceValidator2;
import org.eclipse.wst.xml.search.editor.util.XMLQuerySpecificationUtil;

/**
 * JDT {@link IQueryParticipant} implementation to give the capability to search
 * Java classes and Java methods used in the XML files which are managed with
 * XML references extension point
 * "org.eclipse.wst.xml.search.editor.references".
 * 
 * XML search engine will be used to retrieve from XML files :
 * <ul>
 * <li>declared classes when "toJava" is used :
 * 
 * <pre>
 * &lt;reference&gt;			
 * 				&lt;from ... /&gt;
 * 				&lt;toJava /&gt;
 * 			&lt;/reference&gt;
 * </pre>
 * 
 * </li>
 * <li>declared methods when "toJavaMethod" is used :
 * 
 * <pre>
 * &lt;reference&gt;			
 * 				&lt;from ... /&gt;
 * 				&lt;toJavaMethod /&gt;
 * 			&lt;/reference&gt;
 * </pre>
 * 
 * </li>
 * </ul>
 */
public class XMLReferenceJavaSearchParticipant implements IQueryParticipant {

	/**
	 * Singleton instance of the UI participant for the configuration search
	 * matches
	 */
	private IMatchPresentation uiParticipant;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.ui.search.IQueryParticipant#estimateTicks(org.eclipse
	 * .jdt.ui.search.QuerySpecification)
	 */
	public int estimateTicks(QuerySpecification query) {
		if (isValid(query)) {
			return 50;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.search.IQueryParticipant#getUIParticipant()
	 */
	public synchronized IMatchPresentation getUIParticipant() {
		if (uiParticipant == null) {
			uiParticipant = new XMLUIParticipant();
		}
		return uiParticipant;
	}

	public void search(final ISearchRequestor requestor,
			final QuerySpecification query, IProgressMonitor monitor)
			throws CoreException {
		if (!isValid(query)) {
			return;
		}
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask("", 11); //$NON-NLS-1$
		try {
			// Get Java project, class name and method name to search
			IJavaProject javaProject = null;
			String className = null;
			String methodName = null;
			if (query instanceof ElementQuerySpecification) {
				// Java Search launched with Java Editor with Ctrl+Shift+G
				ElementQuerySpecification elementQuery = (ElementQuerySpecification) query;
				IJavaElement element = elementQuery.getElement();
				if (element instanceof IMember) {
					IMember member = (IMember) element;
					javaProject = member.getJavaProject();
					if (member.getElementType() == IJavaElement.TYPE) {
						// Java class
						IType type = (IType) member;
						className = type.getFullyQualifiedName('.');
					} else if (member.getElementType() == IJavaElement.METHOD) {
						IMethod method = (IMethod) member;
						methodName = method.getElementName();
						className = method.getDeclaringType()
								.getFullyQualifiedName('.');
					}
				} else {
					return;
				}
			} else if (query instanceof PatternQuerySpecification) {
				// Java Search launched with Java UI Dialog Search
				PatternQuerySpecification patternQuery = (PatternQuerySpecification) query;
				if (patternQuery.getSearchFor() == IJavaSearchConstants.METHOD) {
					methodName = patternQuery.getPattern();
				} else {
					className = patternQuery.getPattern();
				}
			}
			if (monitor.isCanceled()) {
				return;
			}
			monitor.worked(1);
			if (className == null && methodName == null) {
				return;
			}
			ToType toType = (!StringUtils.isEmpty(methodName) ? ToType.JAVA_METHOD
					: ToType.JAVA);
			Collection<IXMLReference> references = XMLReferencesManager
					.getInstance().getXMLReferencesForToType(toType);
			if (references.size() < 1) {
				return;
			}
			XMLReferencesIndexManager.getDefault().flushIndexedFiles(
					javaProject);
			searchXMLReferences(query.getScope(), requestor, className,
					methodName, javaProject, references, toType,
					new SubProgressMonitor(monitor, 7));
		} finally {
			monitor.done();
		}
	}

	private void searchXMLReferences(IJavaSearchScope scope,
			ISearchRequestor requestor, String className, String methodName,
			IJavaProject javaProject, Collection<IXMLReference> references,
			ToType toType, SubProgressMonitor monitor) {
		if (javaProject != null) {
			search(requestor, className, methodName, javaProject.getProject(),
					references, toType, monitor);
		}
	}

	private void search(final ISearchRequestor requestor, String className,
			String methodName, IProject project,
			Collection<IXMLReference> references, ToType toType,
			IProgressMonitor monitor) {
		IXMLSearchDOMNodeCollector collector = null;
		String contentTypeId = null;
		String javaTypeName = null;
		// Create list of projects
		Set<IProject> projects = createProjects(project);
		// loop for each references with to type.
		for (IXMLReference reference : references) {
			String[] contentTypeIds = reference.getContentTypeIds();
			for (int i = 0; i < contentTypeIds.length; i++) {
				contentTypeId = contentTypeIds[i];
				if (toType == ToType.JAVA) {
					javaTypeName = className;
				} else {
					javaTypeName = methodName;
				}

				collector = search(requestor, javaTypeName, monitor, project,
						collector, contentTypeId, reference);

				for (IProject p : projects) {
					collector = search(requestor, javaTypeName, monitor, p,
							collector, contentTypeId, reference);
				}
			}
		}
	}

	/**
	 * Returns a set of projects which reference the given project.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	private Set<IProject> createProjects(IProject project) {
		Set<IProject> projects = new HashSet<IProject>();
		addProjects(projects, project);
		return projects;
	}

	private void addProjects(Set<IProject> projects, IProject project) {
		if (!projects.contains(project)) {
			projects.add(project);
			IProject[] referencingProjects = project.getReferencingProjects();
			for (int i = 0; i < referencingProjects.length; i++) {
				addProjects(projects, referencingProjects[i]);
			}
		}
	}

	private IXMLSearchDOMNodeCollector search(final ISearchRequestor requestor,
			String javaTypeName, IProgressMonitor monitor, IProject project,
			IXMLSearchDOMNodeCollector collector, String contentTypeId,
			IXMLReference reference) {
		if(reference.getValidator() instanceof IXMLReferenceValidator2) {
			IXMLReferenceValidator2 validator = (IXMLReferenceValidator2)reference.getValidator();
			if(!validator.isValidTarget(project)) {
				return collector;
			}
		}
		Collection<IFile> indexedFiles = XMLReferencesIndexManager.getDefault()
				.getIndexedFiles(project, contentTypeId, monitor);
		if (indexedFiles != null && indexedFiles.size() > 0) {
			if (collector == null) {
				collector = new JDTSearchDOMNodeCollector(requestor);
			}

			IXMLReferencePath referencePath = reference.getFrom();
			IXMLQuerySpecification xmlQuerySpecification = XMLQuerySpecificationUtil
					.getQuerySpecification(referencePath);
			if (xmlQuerySpecification != null) {
				String query = referencePath.getQuery(null, javaTypeName,
						xmlQuerySpecification.getEqualsStringQueryBuilder(),
						true);
				if (query != null) {

					SimpleXMLSearchEngine.getDefault().search(
							indexedFiles.toArray(new IResource[indexedFiles
									.size()]),
							xmlQuerySpecification.getRequestor(),
							xmlQuerySpecification.getVisitor(), query, null,
							null, collector, null, null, monitor);
				}
			}
		}
		return collector;
	}

	/**
	 * Determines if the current query should be considered or not
	 * 
	 * @param query
	 *            the current query
	 * @return true if the query should be considered, false otherwise
	 */
	private boolean isValid(QuerySpecification query) {
		switch (query.getLimitTo()) {
		case IJavaSearchConstants.REFERENCES:
		case IJavaSearchConstants.ALL_OCCURRENCES: {
			break;
		}
		default: {
			return false;
		}
		}
		if (query instanceof ElementQuerySpecification) {
			IJavaElement element = ((ElementQuerySpecification) query)
					.getElement();
			return element.getElementType() == IJavaElement.TYPE
					|| element.getElementType() == IJavaElement.METHOD;
		}
		if (query instanceof PatternQuerySpecification) {
			PatternQuerySpecification patternQuery = (PatternQuerySpecification) query;
			switch (patternQuery.getSearchFor()) {
			case IJavaSearchConstants.UNKNOWN:
			case IJavaSearchConstants.TYPE:
			case IJavaSearchConstants.CLASS:
			case IJavaSearchConstants.CLASS_AND_INTERFACE:
			case IJavaSearchConstants.METHOD: {
				return true;
			}
			}
		}
		return false;
	}
}
