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
package ru.arsysop.passage.loc.workbench.emfforms.wizards;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;

public class CreateFormWizard extends CreateFileWizard {

	public CreateFormWizard(EditingDomainRegistry registry, EObject eObject, EStructuralFeature identifierFeature,
			EStructuralFeature nameFeature, ClassifierInitializer initializer) {
		super(registry, eObject, identifierFeature, nameFeature, initializer);
	}

	@Override
	protected CreateFileWizardPage createFilePage() {
		return new CreateFormWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(),
				eObject, initializer, identifierFeature != null, nameFeature != null);
	}
}
