
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
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.workbench.emfforms.CreateFormWizard;
import ru.arsysop.passage.loc.workbench.wizards.InitialValuesProvider;

public class CreateFeatureSetHandler {

	@Execute
	public void execute(Shell shell, LicensingImages images, FeatureDomainRegistry registry) {
		LicPackage ePackage = LicPackage.eINSTANCE;
		EClass eClass = ePackage.getFeatureSet();
		EObject eObject = ePackage.getEFactoryInstance().create(eClass);
		String newText = "New Feature Set";
		String newTitle = "Feature Set";
		String newMessage = "Please specify a file name to store feature data";

		EStructuralFeature featureIdentifier = ePackage.getFeatureSet_Identifier();
		EStructuralFeature featureName = ePackage.getFeatureSet_Name();
		InitialValuesProvider valueProvider = createInitialValueProvider(eClass);
		Wizard wizard = new CreateFormWizard(registry, eObject, featureIdentifier, featureName, valueProvider,false);
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
				return "New Feature Set"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialIdentifierValue() {
				return "new.feature.set"; //$NON-NLS-1$ ;
			}

			@Override
			public String getInitialFilePath() {
				return System.getProperty("user.home") + "/new_feature_set"; //$NON-NLS-1$ ;
			}
		};
	}

}