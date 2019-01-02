package ru.arsysop.passage.loc.internal.products.core;

import org.osgi.service.component.annotations.Component;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.registry.ProductsRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + ProductsRegistry.DOMAIN_NAME })
public final class ProductLineClassifierInitializer implements ClassifierInitializer {
	@Override
	public String newObjectIdentifier() {
		return "new.product.line"; //$NON-NLS-1$ ;
	}

	@Override
	public String newObjectName() {
		return "New Product Line";
	}

	@Override
	public String newFileName() {
		return "new_product_line"; //$NON-NLS-1$ ;
	}

	@Override
	public String newObjectTitle() {
		return "Product Line";
	}

	@Override
	public String newObjectMessage() {
		return "New Product Line";
	}

	@Override
	public String newFileMessage() {
		return "Please specify a file name to store product data";
	}
}