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
package ru.arsysop.passage.loc.internal.features.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.FeatureVersion;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;

public class FeatureDomainRegistryTracker extends EContentAdapter {
	
	private final FeatureDomainRegistry registry;
	
	public FeatureDomainRegistryTracker(FeatureDomainRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof FeatureSet) {
			FeatureSet featureSet = (FeatureSet) notifier;
			switch (notification.getFeatureID(FeatureSet.class)) {
			case LicPackage.FEATURE_SET__IDENTIFIER:
				processFeatureSetIdentifier(featureSet, notification);
				break;
			case LicPackage.FEATURE_SET__FEATURES:
				processFeatureSetFeatures(featureSet, notification);
				break;
			default:
				break;
			}
		} else if (notifier instanceof Feature) {
			Feature feature = (Feature) notifier;
			switch (notification.getFeatureID(Feature.class)) {
			case LicPackage.FEATURE__IDENTIFIER:
				processFeatureIdentifier(feature, notification);
			case LicPackage.FEATURE__FEATURE_VERSIONS:
				processFeatureFeatureVersions(feature, notification);
				break;
			default:
				break;
			}
		}
		super.notifyChanged(notification);
	}

	protected void processFeatureSetIdentifier(FeatureSet featureSet, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterFeatureSet(oldValue);
			}
			if (newValue != null) {
				registry.registerFeatureSet(featureSet);
			}
		default:
			break;
		}
	}

	protected void processFeatureSetFeatures(FeatureSet featureSet, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof Feature) {
				Feature feature = (Feature) newValue;
				if (feature.getIdentifier() != null) {
					registry.registerFeature(feature);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof Feature) {
				Feature feature = (Feature) oldValue;
				if (feature.getIdentifier() != null) {
					registry.unregisterFeature(feature.getIdentifier());
				}
			}
			break;

		default:
			break;
		}
	}

	protected void processFeatureIdentifier(Feature feature, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterFeature(oldValue);
			}
			if (newValue != null) {
				registry.registerFeature(feature);
			}
		default:
			break;
		}
	}

	protected void processFeatureFeatureVersions(Feature feature, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof FeatureVersion) {
				FeatureVersion featureVersion = (FeatureVersion) newValue;
				registry.registerFeatureVersion(feature, featureVersion);
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof FeatureVersion) {
				FeatureVersion featureVersion = (FeatureVersion) oldValue;
				registry.unregisterFeatureVersion(feature.getIdentifier(), featureVersion.getVersion());
			}
			break;
	
		default:
			break;
		}
	}


}
