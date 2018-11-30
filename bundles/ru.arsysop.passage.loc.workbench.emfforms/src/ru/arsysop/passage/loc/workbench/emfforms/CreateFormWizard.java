package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EClass;

import ru.arsysop.passage.loc.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;

public class CreateFormWizard extends CreateFileWizard {

	public CreateFormWizard(EditingDomainRegistry registry, EClass eClass) {
		super(registry, eClass);
	}
	
	@Override
	protected CreateFileWizardPage createFilePage() {
		return new CreateFormWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(), createInitialModel());
	}

}
