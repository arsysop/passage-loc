/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.loc.edit.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.passage.loc.edit.ui.DomainRegistryExplorerPart;
import org.eclipse.swt.widgets.Shell;

public class DomainRegistryRemoveHandler {

	private static final String DIALOG_TITLE = "Unregister domain resource"; //$NON-NLS-1$
	private static final String DIALOG_MSG_TEMPLATE = "Unregister domain resource: '%s' ?"; //$NON-NLS-1$

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MApplication application,
			EPartService partService, IEclipseContext context) {

		Object object = partService.getActivePart().getObject();

		if (object instanceof DomainRegistryExplorerPart) {
			DomainRegistryExplorerPart registryExplorer = (DomainRegistryExplorerPart) object;
			String resource = registryExplorer.getUnregisterResourceName();
			if (resource != null && !resource.isEmpty()) {
				String dialogMsg = String.format(DIALOG_MSG_TEMPLATE, resource);
				if (MessageDialog.openConfirm(shell, DIALOG_TITLE, dialogMsg)) {
					if (registryExplorer.unregisterStructureSelectedItem()) {
						registryExplorer.updateView();
					}
				}
			}
		}

	}
}
