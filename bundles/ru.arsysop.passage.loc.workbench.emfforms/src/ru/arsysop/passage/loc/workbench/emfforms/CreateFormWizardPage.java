package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EObject;

import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateFormWizardPage extends CreateFileWizardPage {

	public CreateFormWizardPage(String pageName, String extension, EObject object, InitialValuesProvider valueProvider,
			boolean createId, boolean createName, boolean createForm) {
		super(object, pageName, extension, valueProvider, createId, createName, createForm);
	}

}
