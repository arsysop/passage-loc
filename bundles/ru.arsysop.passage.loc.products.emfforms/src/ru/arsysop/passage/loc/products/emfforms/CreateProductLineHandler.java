 
package ru.arsysop.passage.loc.products.emfforms;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.workbench.emfforms.CreateFormWizard;

public class CreateProductLineHandler {

	@Execute
	public void execute(Shell shell, ProductDomainRegistry registry) {
		EClass eClass = LicPackage.Literals.PRODUCT_LINE;
		Wizard wizard = new CreateFormWizard(registry, eClass);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle("Product Line");
		dialog.setMessage("Please specify a file name to store product data");;
		Shell createdShell = dialog.getShell();
		createdShell.setText("New Product Line");
		ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
		try {
			imageRegistry.put("product", ImageDescriptor.createFromURL(new URL("platform:/plugin/ru.arsysop.passage.lic.model.edit/icons/full/obj16/product.png")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createdShell.setImage(imageRegistry.get("product"));
		dialog.open();
	}
		
}