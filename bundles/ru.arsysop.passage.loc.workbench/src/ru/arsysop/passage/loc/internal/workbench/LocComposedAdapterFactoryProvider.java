package ru.arsysop.passage.loc.internal.workbench;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import org.eclipse.passage.lic.model.edit.providers.LicItemProviderAdapterFactory;
import org.osgi.service.component.annotations.Component;

@Component
public class LocComposedAdapterFactoryProvider implements ComposedAdapterFactoryProvider {

	private final ComposedAdapterFactory adapterFactory;
	
	public LocComposedAdapterFactoryProvider() {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new LicItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	@Override
	public ComposedAdapterFactory getComposedAdapterFactory() {
		return adapterFactory;
	}

}
