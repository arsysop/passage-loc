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
package ru.arsysop.passage.loc.internal.products.core;

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

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.FeatureVersion;
import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.lic.registry.FeatureRegistry;
import ru.arsysop.passage.lic.registry.FeatureSetDescriptor;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;

@Component
public class OsgiInstanceFeatureRegistry extends EditingDomainBasedRegistry implements FeatureRegistry, FeatureDomainRegistry {
	
	private final Map<String, FeatureSet> featureSetIndex = new HashMap<>();
	private final Map<String, Feature> featureIndex = new HashMap<>();
	private final Map<String, Map<String, FeatureVersion>> featureVersionIndex = new HashMap<>();
	
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
		Collection<Map<String, FeatureVersion>> values = featureVersionIndex.values();
		for (Map<String, FeatureVersion> map : values) {
			map.clear();
		}
		featureVersionIndex.clear();
		featureIndex.clear();
		featureSetIndex.clear();
		super.deactivate(properties);
	}

	@Override
	public String getFileExtension() {
		return LicModelCore.FILE_EXTENSION_FEATURES;
	}

	@Override
	public Iterable<FeatureSetDescriptor> getFeatureSets() {
		return new ArrayList<>(featureSetIndex.values());
	}
	
	@Override
	public FeatureSetDescriptor getFeatureSet(String identifier) {
		return featureSetIndex.get(identifier);
	}

	@Override
	public Iterable<FeatureDescriptor> getFeatures() {
		return new ArrayList<>(featureIndex.values());
	}

	@Override
	public Iterable<FeatureDescriptor> getFeatures(String featureSetId) {
		FeatureSet featureSet = featureSetIndex.get(featureSetId);
		if (featureSet == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(featureSet.getFeatures());
	}

	@Override
	public FeatureDescriptor getFeature(String identifier) {
		return featureIndex.get(identifier);
	}
	
	@Override
	public Iterable<FeatureVersionDescriptor> getFeatureVersions() {
		List<FeatureVersionDescriptor> list = new ArrayList<>();
		Collection<Map<String, FeatureVersion>> values = featureVersionIndex.values();
		for (Map<String, FeatureVersion> map : values) {
			list.addAll(map.values());
		}
		return list;
	}

	@Override
	public Iterable<FeatureVersionDescriptor> getFeatureVersions(String featureId) {
		Map<String, FeatureVersion> map = featureVersionIndex.get(featureId);
		if (map == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(map.values());
	}

	@Override
	public FeatureVersionDescriptor getFeatureVersion(String featureId, String version) {
		Map<String, FeatureVersion> map = featureVersionIndex.get(featureId);
		if (map == null) {
			return null;
		}
		return map.get(version);
	}

	@Override
	protected void afterLoad(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof FeatureSet) {
				FeatureSet featureSet = (FeatureSet) eObject;
				addedFeatureSet(featureSet);
			}
		}
	}

	protected void addedFeatureSet(FeatureSet featureSet) {
		String identifier = featureSet.getIdentifier();
		featureSetIndex.put(identifier, featureSet);
		EList<Feature> features = featureSet.getFeatures();
		for (Feature feature : features) {
			addedFeature(feature);
		}
	}

	protected void addedFeature(Feature feature) {
		String identifier = feature.getIdentifier();
		featureIndex.put(identifier, feature);
		EList<FeatureVersion> featureVersions = feature.getFeatureVersions();
		for (FeatureVersion featureVersion : featureVersions) {
			addedFeatureVersion(feature, featureVersion);
		}
	}

	protected void addedFeatureVersion(Feature feature, FeatureVersion featureVersion) {
		String identifier = feature.getIdentifier();
		Map<String, FeatureVersion> map = featureVersionIndex.computeIfAbsent(identifier, key -> new HashMap<>());
		String version = featureVersion.getVersion();
		map.put(version, featureVersion);
	}

	@Override
	protected void beforeUnload(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof FeatureSet) {
				FeatureSet featureSet = (FeatureSet) eObject;
				removedFeatureSet(featureSet);
			}
		}
	}

	protected void removedFeatureSet(FeatureSet featureSet) {
		String identifier = featureSet.getIdentifier();
		featureSetIndex.remove(identifier);
		EList<Feature> features = featureSet.getFeatures();
		for (Feature feature : features) {
			removedFeature(feature);
		}
	}

	protected void removedFeature(Feature feature) {
		String identifier = feature.getIdentifier();
		featureIndex.remove(identifier);
		EList<FeatureVersion> featureVersions = feature.getFeatureVersions();
		for (FeatureVersion featureVersion : featureVersions) {
			removedFeatureVersion(feature, featureVersion);
		}
	}

	protected void removedFeatureVersion(Feature feature, FeatureVersion featureVersion) {
		String identifier = feature.getIdentifier();
		Map<String, FeatureVersion> map = featureVersionIndex.get(identifier);
		if (map != null) {
			String version = featureVersion.getVersion();
			map.remove(version);
			if (map.isEmpty()) {
				featureVersionIndex.remove(identifier);
			}
		}
	}

}
