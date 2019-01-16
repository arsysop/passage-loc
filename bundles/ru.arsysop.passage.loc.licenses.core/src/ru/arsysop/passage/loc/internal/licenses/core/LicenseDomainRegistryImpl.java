/*******************************************************************************
 * Copyright (c) 2018 ArSysOp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package ru.arsysop.passage.loc.internal.licenses.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.LicensePackDescriptor;
import ru.arsysop.passage.lic.registry.LicenseRegistry;
import ru.arsysop.passage.lic.registry.LicensesEvents;
import ru.arsysop.passage.lic.registry.LicensesRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + LicensesRegistry.DOMAIN_NAME,
		DomainRegistryAccess.PROPERTY_FILE_EXTENSION + '=' + LicensesRegistry.FILE_EXTENSION_XMI })
public class LicenseDomainRegistryImpl extends EditingDomainBasedRegistry
		implements LicenseRegistry, LicenseDomainRegistry, EditingDomainRegistry {

	private final Map<String, LicensePack> licensePackIndex = new HashMap<>();
	private final Map<String, List<LicensePack>> userPackIndex = new HashMap<>();
	private final Map<String, Map<String, List<LicensePack>>> productVersionPackIndex = new HashMap<>();

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
	public void bindEventAdmin(EventAdmin eventAdmin) {
		super.bindEventAdmin(eventAdmin);
	}

	@Override
	public void unbindEventAdmin(EventAdmin eventAdmin) {
		super.unbindEventAdmin(eventAdmin);
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
	public void activate(Map<String, Object> properties) {
		super.activate(properties);
	}

	@Deactivate
	@Override
	public void deactivate(Map<String, Object> properties) {
		Collection<Map<String, List<LicensePack>>> productPacks = productVersionPackIndex.values();
		for (Map<String, List<LicensePack>> versionPacks : productPacks) {
			Collection<List<LicensePack>> packs = versionPacks.values();
			for (List<LicensePack> list : packs) {
				list.clear();
			}
			versionPacks.clear();
		}
		productPacks.clear();
		Collection<List<LicensePack>> packs = userPackIndex.values();
		for (List<LicensePack> list : packs) {
			list.clear();
		}
		userPackIndex.clear();
		licensePackIndex.clear();
		super.deactivate(properties);
	}

	@Override
	public String getFileExtension() {
		return LicensesRegistry.FILE_EXTENSION_XMI;
	}

	@Override
	public LicensePackDescriptor getLicensePack(String identifier) {
		return licensePackIndex.get(identifier);
	}

	@Override
	public Iterable<LicensePackDescriptor> getLicensePacks() {
		return new ArrayList<>(licensePackIndex.values());
	}

	@Override
	public Iterable<LicensePackDescriptor> getUserLicensePacks(String userId) {
		List<LicensePack> list = userPackIndex.get(userId);
		if (list == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public Iterable<LicensePackDescriptor> getProductVersionLicensePacks(String productId, String version) {
		Map<String, List<LicensePack>> map = productVersionPackIndex.get(productId);
		if (map == null) {
			return Collections.emptyList();
		}
		List<LicensePack> list = map.get(version);
		if (list == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	protected EContentAdapter createContentAdapter() {
		return new LicenseDomainRegistryTracker(this);
	}

	@Override
	protected void afterLoad(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof LicensePack) {
				LicensePack licensePack = (LicensePack) eObject;
				registerLicensePack(licensePack);
			}
		}
	}

	@Override
	public void registerLicensePack(LicensePack licensePack) {
		String identifier = licensePack.getIdentifier();
		LicensePack existing = licensePackIndex.put(identifier, licensePack);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(LicensesEvents.LICENSE_PACK_CREATE, licensePack));
		String userIdentifier = licensePack.getUserIdentifier();
		List<LicensePack> userPackList = userPackIndex.computeIfAbsent(userIdentifier, key -> new ArrayList<>());
		userPackList.add(licensePack);
		String productIdentifier = licensePack.getProductIdentifier();
		Map<String, List<LicensePack>> map = productVersionPackIndex.computeIfAbsent(productIdentifier,
				key -> new HashMap<>());
		String productVersion = licensePack.getProductVersion();
		List<LicensePack> list = map.computeIfAbsent(productVersion, key -> new ArrayList<>());
		list.add(licensePack);
	}

	@Override
	protected void beforeUnload(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof LicensePack) {
				LicensePack licensePack = (LicensePack) eObject;
				unregisterLicensePack(licensePack.getIdentifier());
			}
		}
	}

	@Override
	public void unregisterLicensePack(String identifier) {
		LicensePack removed = licensePackIndex.remove(identifier);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(LicensesEvents.LICENSE_PACK_DELETE, removed));
			String userIdentifier = removed.getUserIdentifier();

			List<LicensePack> userPackList = userPackIndex.get(userIdentifier);
			if (userPackList != null) {
				userPackList.remove(removed);
				if (userPackList.isEmpty()) {
					userPackIndex.remove(userIdentifier);
				}
			}

			String productIdentifier = removed.getProductIdentifier();
			Map<String, List<LicensePack>> map = productVersionPackIndex.get(productIdentifier);
			if (map != null) {
				String productVersion = removed.getProductVersion();
				List<LicensePack> list = map.get(productVersion);
				if (list != null) {
					list.remove(removed);
					if (list.isEmpty()) {
						map.remove(productVersion);
					}
				}
				if (map.isEmpty()) {
					productVersionPackIndex.remove(productIdentifier);
				}
			}
		}

	}

	@Override
	public EClass getContentClassifier() {
		return LicPackage.eINSTANCE.getLicensePack();
	}

	@Override
	public EStructuralFeature getContentIdentifierAttribute() {
		return LicPackage.eINSTANCE.getLicensePack_Identifier();
	}

	@Override
	public EStructuralFeature getContentNameAttribute() {
		return null;
	}

}
