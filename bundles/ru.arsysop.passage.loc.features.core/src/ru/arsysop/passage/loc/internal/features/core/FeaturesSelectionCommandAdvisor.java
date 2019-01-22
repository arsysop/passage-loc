/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
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
package ru.arsysop.passage.loc.internal.features.core;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.SelectionCommandAdvisor;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + FeaturesRegistry.DOMAIN_NAME })
public class FeaturesSelectionCommandAdvisor implements SelectionCommandAdvisor {
	
	private FeatureDomainRegistry registry;
	
	@Reference
	public void bindDomainRegistry(FeatureDomainRegistry registry) {
		this.registry = registry;
	}

	public void unbindDomainRegistry(FeatureDomainRegistry registry) {
		this.registry = null;
	}

	@Override
	public String getSelectionTitle(String classifier) {
		if (LicPackage.eINSTANCE.getFeatureSet().getName().equals(classifier)) {
			return "Select Feature Set";
		}
		if (LicPackage.eINSTANCE.getFeature().getName().equals(classifier)) {
			return "Select Feature";
		}
		if (LicPackage.eINSTANCE.getFeatureVersion().getName().equals(classifier)) {
			return "Select Feature Version";
		}
		return null;
	}

	@Override
	public Iterable<?> getSelectionInput(String classifier) {
		if (registry == null) {
			return Collections.emptyList();
		}
		if (LicPackage.eINSTANCE.getFeatureSet().getName().equals(classifier)) {
			return registry.getFeatureSets();
		}
		if (LicPackage.eINSTANCE.getFeature().getName().equals(classifier)) {
			return registry.getFeatures();
		}
		if (LicPackage.eINSTANCE.getFeatureVersion().getName().equals(classifier)) {
			return registry.getFeatureVersions();
		}
		return Collections.emptyList();
	}

}
