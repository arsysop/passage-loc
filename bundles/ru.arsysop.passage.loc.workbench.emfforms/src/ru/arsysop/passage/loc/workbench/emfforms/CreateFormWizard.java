package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EObject;

import ru.arsysop.passage.loc.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;

public class CreateFormWizard extends CreateFileWizard {

	public CreateFormWizard(EditingDomainRegistry registry, EObject eObject, String userDir) {
		super(registry, eObject, userDir);
	}

	@Override
	protected CreateFileWizardPage createFilePage() {

		return new CreateFormWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(),
				eObject, userDir);
	}

}
