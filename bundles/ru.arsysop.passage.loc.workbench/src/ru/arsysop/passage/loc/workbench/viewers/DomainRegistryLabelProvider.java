package ru.arsysop.passage.loc.workbench.viewers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.arsysop.passage.lic.base.ui.LicensingImages;

public class DomainRegistryLabelProvider extends LabelProvider {
	
	private final LicensingImages licensingImages;
	private final AdapterFactoryLabelProvider delegate;
	
	public DomainRegistryLabelProvider(LicensingImages licensingImages, AdapterFactory adapterFactory) {
		super();
		this.licensingImages = licensingImages;
		this.delegate = new AdapterFactoryLabelProvider(adapterFactory);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Resource) {
			return licensingImages.getImage(LicensingImages.IMG_DEFAULT);
		}
		return delegate.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		return delegate.getText(element);
	}

}
