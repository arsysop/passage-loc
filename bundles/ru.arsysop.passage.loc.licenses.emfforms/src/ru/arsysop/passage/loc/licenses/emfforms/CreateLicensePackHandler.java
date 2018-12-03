 
package ru.arsysop.passage.loc.licenses.emfforms;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.workbench.emfforms.CreateFormWizard;

public class CreateLicensePackHandler {

	@Execute
	public void execute(Shell shell, LicensingImages images, LicenseDomainRegistry registry) {
		LicPackage ePackage = LicPackage.eINSTANCE;
		EClass eClass = ePackage.getLicensePack();
		EObject eObject = ePackage.getEFactoryInstance().create(eClass);
		String newText = "New License Pack";
		String newTitle = "License Pack";
		String newMessage = "Please specify a file name to store license data";

		Wizard wizard = new CreateFormWizard(registry, eObject);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(newTitle);
		dialog.setMessage(newMessage);;
		Shell createdShell = dialog.getShell();
		createdShell.setText(newText);
		createdShell.setImage(images.getImage(eClass.getName()));
		dialog.open();
	}
		
}