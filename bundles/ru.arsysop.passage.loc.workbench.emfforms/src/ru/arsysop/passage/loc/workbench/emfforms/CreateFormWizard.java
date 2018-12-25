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
package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ru.arsysop.passage.loc.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateFormWizard extends CreateFileWizard {

	private final boolean createForm;

	public CreateFormWizard(EditingDomainRegistry registry, EObject eObject, EStructuralFeature featureIdentifier,
			EStructuralFeature featureName, InitialValuesProvider valueProvider, boolean createForm) {
		super(registry, eObject, featureIdentifier, featureName, valueProvider);
		this.createForm = createForm;

	}

	@Override
	protected CreateFileWizardPage createFilePage() {
		return new CreateFormWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(),
				eObject, valueProvider, featureIdentifier != null, featureName != null, createForm);
	}
}
