/*******************************************************************************
 * Copyright (c) 2018 ArSysOp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package ru.arsysop.passage.loc.edit.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.loc.edit.ui.DomainRegistryExplorerPart;

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
