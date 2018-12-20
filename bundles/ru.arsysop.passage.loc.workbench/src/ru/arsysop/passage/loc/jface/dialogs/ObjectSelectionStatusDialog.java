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
package ru.arsysop.passage.loc.jface.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.base.ui.StatusLine;

public abstract class ObjectSelectionStatusDialog extends ObjectSelectionDialog {

	private StatusLine statusLine;

	private IStatus lastStatus;

	private LicensingImages licensingImages;

	public ObjectSelectionStatusDialog(Shell parent, LicensingImages licensingImages) {
		super(parent);
		this.licensingImages = licensingImages;
	}

	protected abstract void computeResult();

	protected void updateStatus(IStatus status) {
		lastStatus = status;
		if (statusLine != null && !statusLine.isDisposed()) {
			updateButtonsEnableState(status);
			statusLine.setStatus(status);
		}
	}

	/**
	 * Update the status of the ok button to reflect the given status. Subclasses
	 * may override this method to update additional buttons.
	 * 
	 * @param status
	 */
	protected void updateButtonsEnableState(IStatus status) {
		Button okButton = getButton(IDialogConstants.OK_ID);
		if (okButton != null && !okButton.isDisposed()) {
			okButton.setEnabled(!status.matches(IStatus.ERROR));
		}
	}

	@Override
	protected void okPressed() {
		computeResult();
		super.okPressed();
	}

	@Override
	public void create() {
		super.create();
		if (lastStatus != null) {
			updateStatus(lastStatus);
		}
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginLeft = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setFont(font);

		if (isHelpAvailable()) {
			createHelpControl(composite);
		}
		statusLine = new StatusLine(composite, licensingImages);
		statusLine.setAlignment(SWT.LEFT);
		statusLine.setStatus(null);
		statusLine.setFont(font);
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		boolean helpAvailable = isHelpAvailable();
		setHelpAvailable(false);
		super.createButtonBar(composite);
		setHelpAvailable(helpAvailable);
		return composite;
	}

}
