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
package ru.arsysop.passage.loc.workbench.wizards;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.passage.lic.emf.edit.ClassifierInitializer;
import org.eclipse.passage.lic.emf.edit.DomainRegistryAccess;
import org.eclipse.passage.lic.emf.edit.EditingDomainRegistry;

import ru.arsysop.passage.loc.edit.LocEdit;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class CreateFileWizard extends Wizard {

	protected final EObject eObject;
	protected final ClassifierInitializer initializer;
	protected final EStructuralFeature identifierFeature;
	protected final EStructuralFeature nameFeature;

	private final IEclipseContext eclipseContext;
	private final EditingDomainRegistry domainRegistry;
	private final String perspectiveId;

	private CreateFileWizardPage filePage;

	public CreateFileWizard(IEclipseContext context, String domain, String perspectiveId) {
		this.eclipseContext = context;
		this.perspectiveId = perspectiveId;
		DomainRegistryAccess registryAccess = context.get(DomainRegistryAccess.class);
		EditingDomainRegistry domainRegistry = registryAccess.getDomainRegistry(domain);
		EClass eClass = domainRegistry.getContentClassifier();
		this.domainRegistry = domainRegistry;
		this.eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
		this.identifierFeature = domainRegistry.getContentIdentifierAttribute();
		this.nameFeature = domainRegistry.getContentNameAttribute();
		this.initializer = registryAccess.getClassifierInitializer(domain);
	}

	@Override
	public void addPages() {
		filePage = createFilePage(domainRegistry);
		addPage(filePage);
	}

	protected CreateFileWizardPage createFilePage(EditingDomainRegistry registry) {
		return new CreateFileWizardPage(CreateFileWizardPage.class.getName(), eObject,
				domainRegistry.getFileExtension(), initializer, identifierFeature != null, nameFeature != null);
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
			if (identifierFeature != null) {
				eObject.eSet(identifierFeature, filePage.getIdentifier());
			}

			if (nameFeature != null) {
				eObject.eSet(nameFeature, filePage.getName());
			}

			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor progressMonitor) {
					ResourceSet resourceSet = new ResourceSetImpl();
					Resource resource = resourceSet.createResource(fileURI);
					resource.getContents().add(eObject);
					LocEdit.save(resource);
					LocWokbench.switchPerspective(eclipseContext, perspectiveId);
					domainRegistry.registerSource(fileURI.toFileString());
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
