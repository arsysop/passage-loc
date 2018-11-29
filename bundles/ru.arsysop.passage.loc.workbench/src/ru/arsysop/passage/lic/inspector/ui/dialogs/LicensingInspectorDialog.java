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
package ru.arsysop.passage.lic.inspector.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.inspector.HardwareInspector;
import ru.arsysop.passage.lic.inspector.ui.dialogs.HardwareInspectorDialog;
import ru.arsysop.passage.lic.runtime.ConfigurationRequirement;
import ru.arsysop.passage.lic.runtime.RestrictionVerdict;

public class LicensingInspectorDialog extends TitleAreaDialog {

	public static final int HARDWARE_INSPECTOR_ID = IDialogConstants.CLIENT_ID + 1;

	private HardwareInspector hardwareInspector;

	private final List<RestrictionVerdict> restrictions = new ArrayList<>();

	public LicensingInspectorDialog(Shell shell, Iterable<RestrictionVerdict> verdicts) {
		super(shell);
		for (RestrictionVerdict restrictionVerdict : verdicts) {
			restrictions.add(restrictionVerdict);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Licensing");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Licensing status");
		if (restrictions.size() == 0) {
			setMessage("The product is licensed properly");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("The following features are not licensed:").append('\n');
			for (RestrictionVerdict verdict : restrictions) {
				ConfigurationRequirement requirement = verdict.getConfigurationRequirement();
				String featureIdentifier = requirement.getFeatureIdentifier();
				sb.append(featureIdentifier).append('\n');
			}
			setErrorMessage(sb.toString());
		}
		Composite area = (Composite) super.createDialogArea(parent);
		createAreaContent(area);
		Dialog.applyDialogFont(area);
		return area;
	}

	private void createAreaContent(Composite area) {
		Composite contents = new Composite(area, SWT.NONE);
		contents.setLayout(new GridLayout(1, false));
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label contacts = new Label(area, SWT.NONE);
		contacts.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).grab(true, true).create());
		contacts.setText("Please contact your Licensing Operator for details.");
		contacts.setFont(JFaceResources.getHeaderFont());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, true);
		createButton(parent, HARDWARE_INSPECTOR_ID, "Inspect", false);
		Button inspector = getButton(HARDWARE_INSPECTOR_ID);
		inspector.setEnabled(hardwareInspector != null);
	}

	public void setHardwareInspector(HardwareInspector hardwareInspector) {
		this.hardwareInspector = hardwareInspector;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case HARDWARE_INSPECTOR_ID:
			hardwareInspectorPressed();
			break;
		default:
			okPressed();
			break;
		}
	}

	protected void hardwareInspectorPressed() {
		HardwareInspectorDialog dialog = new HardwareInspectorDialog(hardwareInspector, getShell());
		dialog.open();
	}

}
