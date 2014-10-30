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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.xml.search.ui.internal.XMLSearchUIPlugin;

public class SearchParticipantsExtensionPoint {

	public static final String PARTICIPANT_EXTENSION_POINT = "org.eclipse.wst.xml.search.ui.queryParticipants"; //$NON-NLS-1$

	private Set<SearchParticipantDescriptor> fActiveParticipants = null;
	private static SearchParticipantsExtensionPoint fgInstance;

	public boolean hasAnyParticipants() {
		return Platform.getExtensionRegistry().getConfigurationElementsFor(
				PARTICIPANT_EXTENSION_POINT).length > 0;
	}

	private synchronized Set<SearchParticipantDescriptor> getAllParticipants() {
		if (fActiveParticipants != null)
			return fActiveParticipants;
		IConfigurationElement[] allParticipants = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						PARTICIPANT_EXTENSION_POINT);
		fActiveParticipants = new HashSet<SearchParticipantDescriptor>(
				allParticipants.length);
		for (int i = 0; i < allParticipants.length; i++) {
			SearchParticipantDescriptor descriptor = new SearchParticipantDescriptor(
					allParticipants[i]);
			IStatus status = descriptor.checkSyntax();
			if (status.isOK()) {
				fActiveParticipants.add(descriptor);
			} else {
				XMLSearchUIPlugin.log(status);
			}
		}
		return fActiveParticipants;
	}

	private void collectParticipants(
			Set<SearchParticipantDescriptor> participants, Object selectedNode) {
		Iterator<SearchParticipantDescriptor> activeParticipants = getAllParticipants()
				.iterator();
		while (activeParticipants.hasNext()) {
			SearchParticipantDescriptor participant = activeParticipants.next();
			if (participant.isEnabled()
					&& participant.isEnabledFor(selectedNode)) {
				participants.add(participant);
			}
		}
	}

	public Set<SearchParticipantDescriptor> getSearchParticipants() {
		return getAllParticipants();
	}

	public SearchParticipantDescriptor[] getSearchParticipants(
			Object selectedNode) {
		Set<SearchParticipantDescriptor> participantSet = new HashSet<SearchParticipantDescriptor>();
		collectParticipants(participantSet, selectedNode);
		SearchParticipantDescriptor[] participants = new SearchParticipantDescriptor[participantSet
				.size()];
		return participantSet.toArray(participants);
	}

	public static SearchParticipantsExtensionPoint getInstance() {
		if (fgInstance == null)
			fgInstance = new SearchParticipantsExtensionPoint();
		return fgInstance;
	}

	public static void debugSetInstance(
			SearchParticipantsExtensionPoint instance) {
		fgInstance = instance;
	}
}
