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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import ru.arsysop.passage.loc.edit.EditingDomainRegistry;

public class CreateFileWizard extends Wizard {
	
	protected final EditingDomainRegistry editingDomainRegistry;

	private final EClass eClass;
	private CreateFileWizardPage filePage;
	
	public CreateFileWizard(EditingDomainRegistry registry, EClass eClass) {
		this.editingDomainRegistry = registry;
		this.eClass = eClass;
	}
	
	
	@Override
	public void addPages() {
		filePage = createFilePage();
		addPage(filePage);
	}


	protected CreateFileWizardPage createFilePage() {
		return new CreateFileWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension());
	}

	@Override
	public boolean performFinish() {
		try {
			final URI fileURI = filePage.getFileURI();
			if (new File(fileURI.toFileString()).exists()) {
				String message = String.format("The file \"%s\" already exists.  Do you want to replace the existing file?", fileURI.toFileString());
				if (!MessageDialog.openQuestion(getShell(), "Question", message)) {
					filePage.selectFileField();
					return false;
				}
			}
			
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor progressMonitor) {
						try {
							ResourceSet resourceSet = editingDomainRegistry.getEditingDomain().getResourceSet();
							Resource resource = resourceSet.createResource(fileURI);
							EObject rootObject = createInitialModel();
							if (rootObject != null) {
								resource.getContents().add(rootObject);
							}
							Map<Object, Object> options = new HashMap<Object, Object>();
							options.put(XMLResource.OPTION_ENCODING, StandardCharsets.UTF_8.name());
							resource.save(options);
						}
						catch (Exception exception) {
							//FIXME:
							exception.printStackTrace();
						}
						finally {
							progressMonitor.done();
						}
					}
				};

			getContainer().run(false, false, operation);

			return true;			
		}
		catch (Exception exception) {
			//FIXME:
			exception.printStackTrace();
			return false;
		}
	}


	protected EObject createInitialModel() {
		EPackage ePackage = eClass.getEPackage();
		return ePackage.getEFactoryInstance().create(eClass);
	}

}
