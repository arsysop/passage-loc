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
package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.jface.LicensingImages;
import ru.arsysop.passage.loc.workbench.emfforms.wizards.CreateFormWizard;

public class LocWorkbenchEmfforms {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.workbench.emfforms"; //$NON-NLS-1$

	public static void createDomainContentObject(IEclipseContext context, String domain, String perspectiveId) {
		DomainRegistryAccess registryAccess = context.get(DomainRegistryAccess.class);
		LicensingImages images = context.get(LicensingImages.class);
	
		EditingDomainRegistry registry = registryAccess.getDomainRegistry(domain );
		ClassifierInitializer initializer = registryAccess.getClassifierInitializer(domain);
	
		EClass eClass = registry.getContentClassifier();
	
		Wizard wizard = new CreateFormWizard(context, domain, perspectiveId);
		Shell shell = context.get(Shell.class);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(initializer.newObjectTitle());
		dialog.setMessage(initializer.newFileMessage());
	
		Shell createdShell = dialog.getShell();
		createdShell.setText(initializer.newObjectMessage());
		createdShell.setImage(images.getImage(eClass.getName()));
	
		dialog.open();
	}
}
