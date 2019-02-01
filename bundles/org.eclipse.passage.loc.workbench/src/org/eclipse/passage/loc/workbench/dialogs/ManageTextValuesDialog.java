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
package org.eclipse.passage.loc.workbench.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ManageTextValuesDialog extends Dialog {
	
	private final String splitter;

	private final String initialValues;

	private Text editor;
	private String resultValues;

	public ManageTextValuesDialog(Shell parentShell, String values, String splitter) {
		super(parentShell);
		this.splitter = splitter;
		this.initialValues = values;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(400, 500);
	}

	public String getResultValue() {
		return resultValues;
	}

	private String prepareResultValues() {
		
		StringBuilder builder = new StringBuilder();
		for (String item : editor.getText().split("\n")) {
			String filtred = item.trim();
			if (filtred.isEmpty()) {
				continue;
			}
			if (builder.length() > 0) {
				builder.append(splitter);
			}
			builder.append(filtred);
		}
		return builder.toString();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == Dialog.OK) {
			resultValues = prepareResultValues();
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayout(new GridLayout(1, false));
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(new GridLayout(1, false));
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		editor = new Text(base, SWT.MULTI);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if (initialValues != null) {
			String replaced = initialValues.replace(splitter, "\n");
			editor.setText(replaced);
		}
		return parent;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
