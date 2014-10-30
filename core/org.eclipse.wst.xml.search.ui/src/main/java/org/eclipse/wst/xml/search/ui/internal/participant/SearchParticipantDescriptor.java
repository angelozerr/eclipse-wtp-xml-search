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
package org.eclipse.wst.xml.search.ui.internal.participant;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.search.ui.internal.Messages;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchUIPlugin;
import org.eclipse.wst.xml.search.ui.participant.IQueryParticipant;

/**
 */
public class SearchParticipantDescriptor {
	private static final String CLASS = "class"; //$NON-NLS-1$
	private static final String NATURE = "nature"; //$NON-NLS-1$
	private static final String ID = "id"; //$NON-NLS-1$

	private IConfigurationElement fConfigurationElement;
	private boolean fEnabled; //
	private IQueryParticipant participant;

	protected SearchParticipantDescriptor(IConfigurationElement configElement) {
		fConfigurationElement = configElement;
		fEnabled = true;
	}

	/**
	 * checks whether a participant has all the proper attributes.
	 * 
	 * @return returns a status describing the result of the validation
	 */
	protected IStatus checkSyntax() {
		if (fConfigurationElement.getAttribute(ID) == null) {
			String format = Messages.SearchParticipant_error_noID;
			String message = MessageFormat.format(format,
					new Object[] { fConfigurationElement
							.getDeclaringExtension().getUniqueIdentifier() });
			return new Status(IStatus.ERROR, XMLSearchUIPlugin.PLUGIN_ID, 0,
					message, null);
		}
		if (fConfigurationElement.getAttribute(CLASS) == null) {
			String format = Messages.SearchParticipant_error_noClass;
			String message = MessageFormat.format(format,
					new Object[] { fConfigurationElement.getAttribute(ID) });
			return new Status(IStatus.ERROR, XMLSearchUIPlugin.PLUGIN_ID, 0,
					message, null);
		}
		try {
			this.participant = create();			
		} catch (CoreException e) {
			XMLSearchUIPlugin.log(e.getStatus());
			disable();
			String format = Messages.SearchParticipant_error_noClass;
			String message = MessageFormat.format(format,
					new Object[] { fConfigurationElement.getAttribute(ID) });
			return new Status(IStatus.ERROR, XMLSearchUIPlugin.PLUGIN_ID, 0,
					message, null);
		}
		return Status.OK_STATUS;
	}

	public String getID() {
		return fConfigurationElement.getAttribute(ID);
	}

	public void disable() {
		fEnabled = false;
	}

	public boolean isEnabled() {
		return fEnabled;
	}

	protected IQueryParticipant create() throws CoreException {
		try {
			return (IQueryParticipant) fConfigurationElement
					.createExecutableExtension(CLASS);
		} catch (ClassCastException e) {
			throw new CoreException(new Status(IStatus.ERROR,
					XMLSearchUIPlugin.PLUGIN_ID, 0,
					Messages.SearchParticipant_error_classCast, e));
		}
	}

	protected String getNature() {
		return fConfigurationElement.getAttribute(NATURE);
	}

	public boolean isEnabledFor(Object selectedNode) {
		return getParticipant().isEnabledFor(selectedNode);
	}

	public IQueryParticipant getParticipant() {
		return participant;
	}

}
