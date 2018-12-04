package ru.arsysop.passage.loc.workbench.emfforms;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emfforms.swt.core.EMFFormsSWTConstants;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizardPage;

public class CreateFormWizardPage extends CreateFileWizardPage {
	
	private final EObject eObject;

	public CreateFormWizardPage(String pageName, String extension, EObject object) {
		super(pageName, extension);
		this.eObject = object;
	}
	
	@Override
	protected void createOtherControls(Composite composite) {
		// TODO Auto-generated method stub
		final VViewModelProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
		properties.addInheritableProperty(EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_KEY,
				EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_VALUE);
		try {
			ECPSWTViewRenderer.INSTANCE.render(composite, eObject, properties);
		} catch (ECPRendererException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
