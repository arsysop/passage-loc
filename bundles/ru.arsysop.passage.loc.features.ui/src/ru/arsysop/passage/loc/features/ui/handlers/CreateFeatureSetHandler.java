
package ru.arsysop.passage.loc.features.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;

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
		ClassifierInitializer valueProvider = createInitialValueProvider(eClass);
		Wizard wizard = new CreateFileWizard(registry, eObject, featureIdentifier, featureName, valueProvider);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(newTitle);
		dialog.setMessage(newMessage);
		Shell createdShell = dialog.getShell();
		createdShell.setText(newText);
		createdShell.setImage(images.getImage(eClass.getName()));
		dialog.open();
	}

	private ClassifierInitializer createInitialValueProvider(EClass eClass) {
		return new ClassifierInitializer() {

			@Override
			public String proposeObjectIdentifier() {
				return "new.feature.set"; //$NON-NLS-1$ ;
			}

			@Override
			public String proposeObjectName() {
				return "New Feature Set"; //$NON-NLS-1$ ;
			}

			@Override
			public String proposeFileName() {
				return "new_feature_set"; //$NON-NLS-1$ ;
			}
		};
	}

}