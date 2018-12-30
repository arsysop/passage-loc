package ru.arsysop.passage.loc.internal.workbench;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;

@Component
public class LocDomainRegistryAccess implements DomainRegistryAccess {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());;

	private final Map<String, EditingDomainRegistry> domainRegistries = new HashMap<>();
	private final Map<String, String> fileExtensions = new HashMap<>();
	
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	public void registerEditingDomainRegistry(EditingDomainRegistry registry, Map<String, Object> properties) {
		String domain = String.valueOf(properties.get(PROPERTY_DOMAIN_NAME));
		registerEntry(domainRegistries, domain, registry);
		String extension = String.valueOf(properties.get(PROPERTY_FILE_EXTENSION));
		registerEntry(fileExtensions, domain, extension);
	}

	public void unregisterEditingDomainRegistry(EditingDomainRegistry registry, Map<String, Object> properties) {
		String domain = String.valueOf(properties.get(PROPERTY_DOMAIN_NAME));
		unregisterEntry(domainRegistries, domain, registry);
		String extension = String.valueOf(properties.get(PROPERTY_FILE_EXTENSION));
		unregisterEntry(fileExtensions, domain, extension);
	}

	protected <K, V> void registerEntry(Map<K, V> map, K key, V value) {
		V existing = map.put(key, value);
		if (existing != null) {
			logger.warn("Replaced {} for domain {}", existing, key); //$NON-NLS-1$
		}
		logger.trace("Registered {} for domain {}", value, key); //$NON-NLS-1$
	}

	protected <K, V> void unregisterEntry(Map<K, V> map, K key, V value) {
		V existing = map.remove(key);
		if (existing == null) {
			logger.warn("Unexpected null (should be {}) for domain {}", value, key); //$NON-NLS-1$
		}
		logger.trace("Unregistered {} for domain {}", existing, key); //$NON-NLS-1$
	}

	@Override
	public EditingDomainRegistry getDomainRegistry(String name) {
		return domainRegistries.get(name);
	}

	@Override
	public String getFileExtension(String name) {
		return fileExtensions.get(name);
	}

}
