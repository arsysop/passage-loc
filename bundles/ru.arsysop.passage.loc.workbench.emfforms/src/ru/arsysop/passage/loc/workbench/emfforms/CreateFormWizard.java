package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ru.arsysop.passage.loc.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateFormWizard extends CreateFileWizard {

	public CreateFormWizard(EditingDomainRegistry registry, EObject eObject, EStructuralFeature featureIdentifier,
			EStructuralFeature featureName, InitialValuesProvider valueProvider) {
		super(registry, eObject, featureIdentifier, featureName, valueProvider);

	}

	@Override
	protected CreateFileWizardPage createFilePage() {

		return new CreateFormWizardPage(CreateFileWizardPage.class.getName(), editingDomainRegistry.getFileExtension(),
				eObject, valueProvider, featureIdentifier != null, featureName != null);
	}

}
