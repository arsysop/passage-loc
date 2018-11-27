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
package ru.arsysop.passage.loc.licenses.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;

public class CreateLicenseHandler {

	@Execute
	public void execute(Shell shell, LicenseDomainRegistry registry) {
		EditingDomain editingDomain = registry.getEditingDomain();
		EClass license = LicPackage.Literals.LICENSE;
		WizardDialog dialog = new WizardDialog(shell, new CreateFileWizard(editingDomain, license));
		dialog.create();
		dialog.setTitle("License");
		dialog.setMessage("Please specify a file name to store licensing data");;
		dialog.getShell().setText("New License");
		dialog.open();
	}
		
}