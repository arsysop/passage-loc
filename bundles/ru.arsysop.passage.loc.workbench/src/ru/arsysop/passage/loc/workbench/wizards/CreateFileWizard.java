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
package ru.arsysop.passage.loc.workbench.wizards;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.loc.edit.EditingDomainRegistry;

public class CreateFileWizard extends Wizard {

	protected final EditingDomainRegistry editingDomainRegistry;
	protected final EObject eObject;
	protected final InitialValuesProvider valueProvider;
	protected final EStructuralFeature featureIdentifier;
	protected final EStructuralFeature featureName;
	protected final boolean createForm ;
	private CreateFileWizardPage filePage;

	public CreateFileWizard(EditingDomainRegistry registry, EObject eObject, EStructuralFeature featureIdentifier,
			EStructuralFeature featureName, InitialValuesProvider valueProvider, boolean createForm) {
		this.editingDomainRegistry = registry;
		this.eObject = eObject;

		this.featureIdentifier = featureIdentifier;
		this.featureName = featureName;
		this.valueProvider = valueProvider;
		this.createForm = createForm;
		

	}

	@Override
	public void addPages() {
		filePage = createFilePage();
		addPage(filePage);
	}

	protected CreateFileWizardPage createFilePage() {
		return new CreateFileWizardPage(eObject, CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(),
				valueProvider, featureIdentifier != null, featureName != null ,createForm);
	}

	@Override
	public boolean performFinish() {
		try {
			final URI fileURI = filePage.getFileURI();
			File file = new File(fileURI.toFileString());
			if (file.exists()) {
				String absolutePath = file.getAbsolutePath();
				String message = String.format(
						"The file \"%s\" already exists.  Do you want to replace the existing file?", absolutePath);
				if (!MessageDialog.openQuestion(getShell(), "Question", message)) {
					filePage.selectFileField();
					return false;
				}
			}
			if (featureIdentifier != null) {
				eObject.eSet(featureIdentifier, filePage.getIdentifier());
			}

			if (featureName != null) {
				eObject.eSet(featureName, filePage.getName());
			}

			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor progressMonitor) {
					ResourceSet resourceSet = editingDomainRegistry.getEditingDomain().getResourceSet();
					Resource resource = resourceSet.createResource(fileURI);
					resource.getContents().add(eObject);
					LicModelCore.save(resource);
				}
			};

			getContainer().run(false, false, operation);

			return true;
		} catch (Exception exception) {
			// FIXME:
			exception.printStackTrace();
			return false;
		}
	}
	


}
