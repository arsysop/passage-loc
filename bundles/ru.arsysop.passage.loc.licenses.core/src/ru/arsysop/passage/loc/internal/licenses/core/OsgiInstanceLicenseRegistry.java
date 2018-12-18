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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.registry.LicensePackDescriptor;
import ru.arsysop.passage.lic.registry.LicenseRegistry;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;

@Component
public class OsgiInstanceLicenseRegistry extends EditingDomainBasedRegistry implements LicenseRegistry, LicenseDomainRegistry {
	
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
		return LicModelCore.FILE_EXTENSION_CONDITIONS;
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
	protected void afterLoad(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof LicensePack) {
				LicensePack licensePack = (LicensePack) eObject;
				addedLicensePack(licensePack);
			}
		}
	}

	protected void addedLicensePack(LicensePack licensePack) {
		String identifier = licensePack.getIdentifier();
		licensePackIndex.put(identifier, licensePack);
		String userIdentifier = licensePack.getUserIdentifier();
		List<LicensePack> userPackList = userPackIndex.computeIfAbsent(userIdentifier, key -> new ArrayList<>());
		userPackList.add(licensePack);
		String productIdentifier = licensePack.getProductIdentifier();
		Map<String, List<LicensePack>> map = productVersionPackIndex.computeIfAbsent(productIdentifier, key -> new HashMap<>());
		String productVersion = licensePack.getProductVersion();
		List<LicensePack> list = map.computeIfAbsent(productVersion, key -> new ArrayList<>());
		list.add(licensePack);
	}

	@Override
	protected void beforeUnload(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof LicensePack) {
				LicensePack licensePack = (LicensePack) eObject;
				removedLicensePack(licensePack);
			}
		}
	}

	protected void removedLicensePack(LicensePack licensePack) {
		String identifier = licensePack.getIdentifier();
		licensePackIndex.remove(identifier);
		String userIdentifier = licensePack.getUserIdentifier();

		List<LicensePack> userPackList = userPackIndex.get(userIdentifier);
		if (userPackList != null) {
			userPackList.remove(licensePack);
			if (userPackList.isEmpty()) {
				userPackIndex.remove(userIdentifier);
			}
		}

		String productIdentifier = licensePack.getProductIdentifier();
		Map<String, List<LicensePack>> map = productVersionPackIndex.get(productIdentifier);
		if (map != null) {
			String productVersion = licensePack.getProductVersion();
			List<LicensePack> list = map.get(productVersion);
			if (list != null) {
				list.remove(licensePack);
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
