package ru.arsysop.passage.loc.workbench.emfforms;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emfforms.swt.core.EMFFormsSWTConstants;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.LicenseGrant;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;

public class CreateFormWizardPage extends CreateFileWizardPage {

	private static final String NEW_NAME = "%s_Name";
	private static final String NEW_IDENTIFIER = "%s_Identifier";
	private final EObject eObject;

	public CreateFormWizardPage(String pageName, String extension, EObject object, String userDir) {
		super(pageName, extension, userDir);
		this.eObject = object;

	}

	@Override
	protected void createOtherControls(Composite composite) {
		final VViewModelProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
		properties.addInheritableProperty(EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_KEY,
				EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_VALUE);

		initDefaultValues();

		try {
			ECPSWTViewRenderer.INSTANCE.render(composite, eObject, properties);

		} catch (ECPRendererException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.log(Level.FINER, e.getMessage(), e);
		}
	}

	private void initDefaultValues() {
		if (eObject instanceof ProductLine) {
			ProductLine pl = (ProductLine) eObject;
			if (pl.getIdentifier() == null || pl.getIdentifier().isEmpty()) {
				String name = pl.getClass().getSimpleName();
				pl.setIdentifier(String.format(NEW_IDENTIFIER, name));
				pl.setName(String.format(NEW_NAME, name));
			}
		} else if (eObject instanceof FeatureSet) {
			FeatureSet fs = (FeatureSet) eObject;
			String name = fs.getClass().getSimpleName();
			fs.setIdentifier(String.format(NEW_IDENTIFIER, name));
			fs.setName(String.format(NEW_NAME, name));
		} else if (eObject instanceof LicenseGrant) {

		}

	}
}
