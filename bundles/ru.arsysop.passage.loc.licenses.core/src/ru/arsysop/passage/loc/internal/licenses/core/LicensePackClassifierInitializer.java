package ru.arsysop.passage.loc.internal.licenses.core;

import org.osgi.service.component.annotations.Component;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.registry.LicensesRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + LicensesRegistry.DOMAIN_NAME })
public final class LicensePackClassifierInitializer implements ClassifierInitializer {
	@Override
	public String newObjectIdentifier() {
		return "new.license.pack"; //$NON-NLS-1$
	}

	@Override
	public String newObjectName() {
		return "New License Pack";
	}

	@Override
	public String newFileName() {
		return "new_license_pack"; //$NON-NLS-1$
	}

	@Override
	public String newObjectTitle() {
		return "License Pack";
	}

	@Override
	public String newObjectMessage() {
		return "New License Pack";
	}

	@Override
	public String newFileMessage() {
		return "Please specify a file name to store license data";
	}
}