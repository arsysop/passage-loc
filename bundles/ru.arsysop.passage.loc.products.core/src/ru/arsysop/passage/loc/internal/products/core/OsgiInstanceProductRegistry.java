package ru.arsysop.passage.loc.internal.products.core;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;

@Component
public class OsgiInstanceProductRegistry extends EditingDomainBasedRegistry<ProductDescriptor> implements ProductRegistry {
	
	@Override
	protected Class<ProductDescriptor> getDescriptorClass() {
		return ProductDescriptor.class;
	}

	@Reference
	@Override
	public void bindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		super.bindEnvironmentInfo(environmentInfo);
	}
	
	@Override
	public void unbindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		super.unbindEnvironmentInfo(environmentInfo);
	}
	
	@Reference
	@Override
	public void bindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		super.bindFactoryProvider(factoryProvider);
	}
	
	@Override
	public void unbindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		super.unbindFactoryProvider(factoryProvider);
	}
	
	@Activate
	public void activate() {
		super.activate();
	}

	@Override
	public String createPassword(ProductVersionDescriptor productVersion) {
		String name = productVersion.getProduct().getName();
		String version = productVersion.getVersion();
		return name + "###" + version;
	}

	@Override
	protected String getSourceDefault() {
		String areaValue = environmentInfo.getProperty("osgi.instance.area");
		Path instance = Paths.get(URI.create(areaValue));
		Path passagePath = instance.resolve(".passage");
		try {
			Files.createDirectories(passagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Path productsPath = passagePath.resolve("products.lic");
		return productsPath.toFile().getAbsolutePath();
	}

}
