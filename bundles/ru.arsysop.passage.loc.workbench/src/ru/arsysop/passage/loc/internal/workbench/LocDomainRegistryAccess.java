package ru.arsysop.passage.loc.internal.workbench;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;

@Component
public class LocDomainRegistryAccess implements DomainRegistryAccess {
	
	private final Map<String, EditingDomainRegistry> domainRegistries = new HashMap<>();
	private final Map<String, String> fileExtensions = new HashMap<>();

	@Override
	public EditingDomainRegistry getDomainRegistry(String name) {
		return domainRegistries.get(name);
	}

	@Override
	public String getFileExtension(String name) {
		return fileExtensions.get(name);
	}

}
