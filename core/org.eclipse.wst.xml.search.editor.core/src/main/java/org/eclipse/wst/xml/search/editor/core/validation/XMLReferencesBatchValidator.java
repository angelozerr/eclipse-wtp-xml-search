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
package org.eclipse.wst.xml.search.editor.core.validation;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.core.ValidationException;
import org.eclipse.wst.validation.internal.operations.IWorkbenchContext;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidationContext;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.validation.internal.provisional.core.IValidatorJob;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.xpath.NamespaceInfos;
import org.eclipse.wst.xml.search.core.xpath.XPathManager;
import org.eclipse.wst.xml.search.editor.core.internal.Trace;
import org.eclipse.wst.xml.search.editor.core.internal.XMLSearchEditorCorePlugin;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReference;
import org.eclipse.wst.xml.search.editor.core.references.IXMLReferencePath;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferenceContainer;
import org.eclipse.wst.xml.search.editor.core.references.XMLReferencesManager;
import org.w3c.dom.NodeList;

public class XMLReferencesBatchValidator extends AbstractValidator implements
		IValidatorJob {

	// for debugging
	static final boolean DEBUG = Boolean
			.valueOf(
					Platform
							.getDebugOption("org.eclipse.wst.xml.search.editor/debug/validator")).booleanValue(); //$NON-NLS-1$

	/**
	 * List of IResources that the currently validating file depends upon
	 */
	private Collection<IResource> fDependsOn;

	public ISchedulingRule getSchedulingRule(IValidationContext helper) {
		if (helper instanceof IWorkbenchContext) {
			/*
			 * Use a single build rule when running batch validation.
			 */
			return ResourcesPlugin.getWorkspace().getRuleFactory().buildRule();
		}
		/*
		 * For other kinds of validation, use no specific rule
		 */
		return null;
	}

	/**
	 * Validate one file. It's assumed that the file has JSP content type.
	 * 
	 * @param file
	 * @param reporter
	 */
	void validateFile(IFile file, IReporter reporter) {
		try {
			file.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, "", e);
		}
		IStructuredModel model = null;
		try {
			// get DOM model on behalf of all XML references validators
			model = StructuredModelManager.getModelManager().getModelForRead(
					file);
			if (!reporter.isCancelled() && model != null
					&& model instanceof IDOMModel) {
				reporter.removeAllMessages(this, file);
				performValidation(file, reporter, (IDOMModel) model);
			}
		} catch (IOException e) {
			Trace.trace(Trace.SEVERE, "", e);
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, "", e);
		} finally {
			if (model != null)
				model.releaseFromRead();
		}
	}

	public IStatus validateInJob(final IValidationContext helper,
			final IReporter reporter) throws ValidationException {
		Job currentJob = Job.getJobManager().currentJob();
		ISchedulingRule rule = null;
		if (currentJob != null) {
			rule = currentJob.getRule();
		}
		IWorkspaceRunnable validationRunnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					doValidate(helper, reporter);
				} catch (ValidationException e) {
					throw new CoreException(new Status(IStatus.ERROR,
							XMLSearchEditorCorePlugin.PLUGIN_ID, 0,
							XMLSearchEditorCorePlugin.PLUGIN_ID, e));
				}
			}
		};
		try {
			JavaCore.run(validationRunnable, rule, new NullProgressMonitor());
		} catch (CoreException e) {
			if (e.getCause() instanceof ValidationException) {
				throw (ValidationException) e.getCause();
			}
			throw new ValidationException(new LocalizedMessage(
					IMessage.ERROR_AND_WARNING, e.getMessage()), e);
		}
		return Status.OK_STATUS;
	}

	public void cleanup(IReporter reporter) {
		// TODO Auto-generated method stub

	}

	public void validate(IValidationContext helper, IReporter reporter)
			throws ValidationException {
		doValidate(helper, reporter);
	}

	void doValidate(IValidationContext helper, IReporter reporter)
			throws ValidationException {
		String[] uris = helper.getURIs();
		if (uris.length > 0) {
			IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
			IFile currentFile = null;
			for (int i = 0; i < uris.length && !reporter.isCancelled(); i++) {
				currentFile = wsRoot.getFile(new Path(uris[i]));
				if (currentFile != null && currentFile.exists()) {
					if (shouldValidate(currentFile)) {
						Message message = new LocalizedMessage(
								IMessage.LOW_SEVERITY, currentFile
										.getFullPath().toString().substring(1));
						reporter.displaySubtask(this, message);
						validateFile(currentFile, reporter);
					}
					if (DEBUG)
						System.out.println("validating: [" + uris[i] + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} else {
			// if uris[] length 0 -> validate() gets called for each project
			// if (helper instanceof IWorkbenchContext) {
			// IProject project = ((IWorkbenchContext) helper).getProject();
			//
			// Message message = new LocalizedMessage(IMessage.LOW_SEVERITY,
			// NLS.bind("Gathering files in {0}", project
			// .getFullPath()));
			// reporter.displaySubtask(this, message);
			//
			// JSPFileVisitor visitor = new JSPFileVisitor(reporter);
			// try {
			// // collect all jsp files for the project
			// project.accept(visitor, IResource.DEPTH_INFINITE);
			// } catch (CoreException e) {
			// if (DEBUG)
			// e.printStackTrace();
			// }
			// IFile[] files = visitor.getFiles();
			// for (int i = 0; i < files.length && !reporter.isCancelled(); i++)
			// {
			// if (shouldValidate(files[i])) {
			//
			// message = new LocalizedMessage(IMessage.LOW_SEVERITY,
			// files[i].getFullPath().toString().substring(1));
			// reporter.displaySubtask(this, message);
			//
			// validateFile(files[i], reporter);
			// }
			// if (DEBUG)
			//						System.out.println("validating: [" + files[i] + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			// }
			// }
		}
	}

	/**
	 * Determine if a given file should be validated.
	 * 
	 * @param file
	 *            The file that may be validated.
	 * @return True if the file should be validated, false otherwise.
	 */
	private static boolean shouldValidate(IFile file) {
		IResource resource = file;
		do {
			if (resource.isDerived() || resource.isTeamPrivateMember()
					|| !resource.isAccessible()
					|| resource.getName().charAt(0) == '.') {
				return false;
			}
			resource = resource.getParent();
		} while ((resource.getType() & IResource.PROJECT) == 0);

		return true;
	}

	void addDependsOn(IResource resource) {
		fDependsOn.add(resource);
	}

	@Override
	public ValidationResult validate(final IResource resource, int kind,
			ValidationState state, IProgressMonitor monitor) {
		if (resource.getType() != IResource.FILE)
			return null;
		final ValidationResult result = new ValidationResult();
		final IReporter reporter = result.getReporter(monitor);
		fDependsOn = new HashSet<IResource>();

		IWorkspaceRunnable validationRunnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				validateFile((IFile) resource, reporter);
				result.setDependsOn((IResource[]) fDependsOn
						.toArray(new IResource[fDependsOn.size()]));
				fDependsOn.clear();
			}
		};
		Job currentJob = Job.getJobManager().currentJob();
		ISchedulingRule rule = null;
		if (currentJob != null) {
			rule = currentJob.getRule();
		}
		try {
			JavaCore.run(validationRunnable, rule, new NullProgressMonitor());
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, "", e);
		}
		return result;
	}

	private void performValidation(IFile file, IReporter reporter,
			IDOMModel model) {
		if (reporter.isCancelled()) {
			// validation, was canceled, stop the validation
			return;
		}
		XMLReferenceContainer container = XMLReferencesManager.getInstance()
				.getXMLReferenceContainer(model.getContentTypeIdentifier());
		if (container == null) {
			// the current DOM model doesn't define XML references, stop the
			// validation
			return;
		}
		Collection<IXMLReference> allReferences = container
				.getAllXMLReferences();
		if (allReferences.isEmpty()) {
			// the current DOM model doesn't define XML references, stop the
			// validation
			return;
		}
		// Loop for each XML reference to get Node by using "from" reference and
		// validate
		// each Node founded with "to" reference
		IDOMDocument document = model.getDocument();
		NamespaceInfos namespaceInfos = XPathManager.getManager()
				.getNamespaceInfo(document);
		for (IXMLReference reference : allReferences) {
			IXMLReferencePath from = reference.getFrom();			
			if (!from.hasWildCard()) {
				String path = from.getPath();
				String[] targetNodes = from.getTargetNodes();
				for (String targetNode : targetNodes) {
					String xpath = EqualsStringQueryBuilder.INSTANCE.build(
							path, null, null)
							+ "/" + targetNode;
					try {
						NodeList list = XPathManager.getManager()
								.evaluateNodeSet(null, document, xpath,
										namespaceInfos, null);
						for (int i = 0; i < list.getLength(); i++) {
							IDOMNode n = (IDOMNode) list.item(i);
							LocalizedMessage message = reference.getValidator().validate(reference, n, file, this);
							addMessage(message, file, this, reporter);
						}
					} catch (XPathExpressionException e) {
						Trace.trace(Trace.SEVERE, "", e);
					}
				}
			} else {

			}
		}

	}
	
	private void addMessage(LocalizedMessage message, IFile file, IValidator validator, IReporter reporter) {
		if (message != null) {
            reporter.removeAllMessages(validator);
			message.setTargetObject(file);
			reporter.addMessage(validator, message);
		}
	}
}
