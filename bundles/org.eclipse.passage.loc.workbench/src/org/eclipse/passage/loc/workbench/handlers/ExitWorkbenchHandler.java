/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
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
package org.eclipse.passage.loc.workbench.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ExitWorkbenchHandler {
	private static final String TITLE_DIALOG = "Exit dialog";
	private static final String DIALOG_MSG = "Do you want to exit the product?";

	@Inject
	private IEclipseContext context;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, EPartService partService)
			throws ExecutionException {
		if (!partService.saveAll(true)) {
			return;
		}
		if (MessageDialog.openQuestion(shell, TITLE_DIALOG, DIALOG_MSG)) {
			Object workbench = context.get(IWorkbench.class.getName());
			if (workbench != null && workbench instanceof IWorkbench) {
				((IWorkbench) workbench).close();
			}
		}
	}
}
