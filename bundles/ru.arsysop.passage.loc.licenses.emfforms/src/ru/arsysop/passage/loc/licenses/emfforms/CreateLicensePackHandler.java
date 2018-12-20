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
package ru.arsysop.passage.loc.licenses.emfforms;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.workbench.emfforms.CreateFormWizard;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateLicensePackHandler {

	@Execute
	public void execute(Shell shell, LicensingImages images, LicenseDomainRegistry registry) {
		LicPackage ePackage = LicPackage.eINSTANCE;
		EClass eClass = ePackage.getLicensePack();
		EObject eObject = ePackage.getEFactoryInstance().create(eClass);
		String newText = "New License Pack";
		String newTitle = "License Pack";
		String newMessage = "Please specify a file name to store license data";
		InitialValuesProvider valueProvider = createInitialValueProvider(eClass);
		//TODO: SK
		EStructuralFeature featureIdentifier = LicPackage.eINSTANCE.getLicensePack_Identifier();

		Wizard wizard = new CreateFormWizard(registry, eObject, null, null, valueProvider);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(newTitle);
		dialog.setMessage(newMessage);
		Shell createdShell = dialog.getShell();
		createdShell.setText(newText);
		createdShell.setImage(images.getImage(eClass.getName()));
		dialog.open();
	}

	private InitialValuesProvider createInitialValueProvider(EClass eClass) {
		return new InitialValuesProvider() {

			@Override
			public String getInitialNameValue() {
				return "New License Pack"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialIdentifierValue() {
				return "new.license.pack"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialFilePath() {
				return System.getProperty("user.home") + "/new_license_pack"; //$NON-NLS-1$ ;
			}
		};
	}

}