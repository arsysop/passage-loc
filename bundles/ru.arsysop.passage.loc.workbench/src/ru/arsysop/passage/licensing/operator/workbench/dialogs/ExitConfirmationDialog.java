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
package ru.arsysop.passage.licensing.operator.workbench.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ExitConfirmationDialog extends AbstractDialog {

	private static final int DIALOG_HEIGHT_DEFAULT = 120;
	private static final int DIALOG_WIDTH_DEFAULT = 400;
	private static final String CONFIMATION_EXIT_TEXT = "Do you want to exit the product?"; //$NON-NLS-1$

	public ExitConfirmationDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		CLabel lblConfirmation = new CLabel(parent, SWT.CENTER);
		lblConfirmation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lblConfirmation.setText(CONFIMATION_EXIT_TEXT);
		return super.createDialogArea(parent);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(DIALOG_WIDTH_DEFAULT, DIALOG_HEIGHT_DEFAULT);
		super.configureShell(newShell);
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		Rectangle bounds = getShell().getBounds();
		Point location = getInitialLocation(new Point(bounds.width, bounds.height));
		getShell().setBounds(location.x, location.y, bounds.width, bounds.height);
	}
}
