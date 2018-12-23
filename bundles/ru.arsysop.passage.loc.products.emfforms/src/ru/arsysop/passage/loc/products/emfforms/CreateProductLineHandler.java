
package ru.arsysop.passage.loc.products.emfforms;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.workbench.emfforms.CreateFormWizard;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateProductLineHandler {

	@Execute
	public void execute(Shell shell, LicensingImages images, ProductDomainRegistry registry) {
		LicPackage ePackage = LicPackage.eINSTANCE;
		EClass eClass = ePackage.getProductLine();
		EObject eObject = ePackage.getEFactoryInstance().create(eClass);
		String newText = "New Product Line";
		String newTitle = "Product Line";
		String newMessage = "Please specify a file name to store product data";
		InitialValuesProvider valueProvider = createInitialValueProvider(eClass);
		EStructuralFeature featureIdentifier = ePackage.getProductLine_Identifier();
		EStructuralFeature featureName = ePackage.getProductLine_Name();
		Wizard wizard = new CreateFormWizard(registry, eObject, featureIdentifier, featureName ,valueProvider ,false);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(newTitle);
		dialog.setMessage(newMessage);

		Shell createdShell = dialog.getShell();
		createdShell.setText(newText);
		createdShell.setImage(images.getImage(eClass.getName()));
		dialog.open();
	}

	private InitialValuesProvider createInitialValueProvider(EClass eClass) {
		return new InitialValuesProvider() {

			@Override
			public String getInitialNameValue() {
				return "New Product Line"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialIdentifierValue() {
				return "new.product.line"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialFileName() {
				return "new_product_line"; //$NON-NLS-1$ ;
			}
		};
	}

}