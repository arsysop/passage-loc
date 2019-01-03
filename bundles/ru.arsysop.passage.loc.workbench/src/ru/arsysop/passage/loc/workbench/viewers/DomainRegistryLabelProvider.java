package ru.arsysop.passage.loc.workbench.viewers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionFeatureDescriptor;

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
		//FIXME: provide "name" feature for ProductVersion
		if (element instanceof ProductVersionDescriptor) {
			ProductVersionDescriptor productVersion = (ProductVersionDescriptor) element;
			ProductDescriptor product = productVersion.getProduct();
			return product.getName() + ' ' + productVersion.getVersion();
		}
		if (element instanceof ProductVersionFeatureDescriptor) {
			ProductVersionFeatureDescriptor productVersionFeature = (ProductVersionFeatureDescriptor) element;
			String text = getText(productVersionFeature.getProductVersion());
			return text + ' ' + ':' + ' ' + productVersionFeature.getFeatureIdentifier() + ' ' + productVersionFeature.getFeatureVersion();
		}

		if (element instanceof FeatureVersionDescriptor) {
			FeatureVersionDescriptor productVersion = (FeatureVersionDescriptor) element;
			FeatureDescriptor feature = productVersion.getFeature();
			return feature.getName() + ' ' + productVersion.getVersion();
		}
		return delegate.getText(element);
	}

}
