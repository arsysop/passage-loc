package ru.arsysop.passage.loc.internal.features.core;

import org.osgi.service.component.annotations.Component;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.registry.FeaturesRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + FeaturesRegistry.DOMAIN_NAME })
public final class FeatureSetClassifierInitializer implements ClassifierInitializer {
	@Override
	public String newObjectIdentifier() {
		return "new.feature.set"; //$NON-NLS-1$ ;
	}

	@Override
	public String newObjectName() {
		return "New Feature Set";
	}

	@Override
	public String newFileName() {
		return "new_feature_set"; //$NON-NLS-1$ ;
	}

	@Override
	public String newObjectTitle() {
		return "Feature Set";
	}

	@Override
	public String newObjectMessage() {
		return "New Feature Set";
	}

	@Override
	public String newFileMessage() {
		return "Please specify a file name to store feature data";
	}
}