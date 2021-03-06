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
