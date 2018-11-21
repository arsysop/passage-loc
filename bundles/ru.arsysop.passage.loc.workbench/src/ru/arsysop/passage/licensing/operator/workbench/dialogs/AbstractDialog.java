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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

abstract public class AbstractDialog extends Dialog {

	protected AbstractDialog(Shell parentShell) {
		super(parentShell);
	}

	protected GridData createAreaDefaultLayoutData() {
		GridData areaData = new GridData(SWT.FILL, SWT.FILL, true, true);
		areaData.minimumWidth = 400;
		return areaData;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData btnData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		Button btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		btnOk.setLayoutData(btnData);
		Button btnCancel = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		btnCancel.setLayoutData(btnData);
	}
	@Override
	protected Control createButtonBar(Composite parent) {
		Composite compositeButtons = new Composite(parent, SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		data.heightHint = 40;
		compositeButtons.setLayoutData(data);
		compositeButtons.setLayout(new GridLayout(1, false));
		createButtonsForButtonBar(compositeButtons);
		return compositeButtons;
	}

	protected GridData createLabelLayoutData() {
		GridData lblData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		lblData.widthHint = 100;
		return lblData;
	}
}
