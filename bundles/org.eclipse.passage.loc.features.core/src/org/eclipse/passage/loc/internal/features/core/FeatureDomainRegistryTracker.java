/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.loc.internal.features.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.passage.lic.emf.edit.DomainContentAdapter;
import org.eclipse.passage.lic.model.api.Feature;
import org.eclipse.passage.lic.model.api.FeatureSet;
import org.eclipse.passage.lic.model.api.FeatureVersion;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.loc.edit.FeatureDomainRegistry;

public class FeatureDomainRegistryTracker extends DomainContentAdapter<FeatureDomainRegistry> {
	
	public FeatureDomainRegistryTracker(FeatureDomainRegistry registry) {
		super(registry);
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
		} else if (notifier instanceof FeatureVersion) {
			FeatureVersion featureVersion = (FeatureVersion) notifier;
			switch (notification.getFeatureID(FeatureVersion.class)) {
			case LicPackage.FEATURE_VERSION__VERSION:
				processFeatureVersionVersion(featureVersion, notification);
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
				String identifier = feature.getIdentifier();
				if (identifier != null) {
					registry.registerFeature(feature);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof Feature) {
				Feature feature = (Feature) oldValue;
				String identifier = feature.getIdentifier();
				if (identifier != null) {
					registry.unregisterFeature(identifier);
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

	protected void processFeatureVersionVersion(FeatureVersion featureVersion, Notification notification) {
		Feature feature = featureVersion.getFeature();
		if (feature == null) {
			//FIXME: warn
			return;
		}
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterFeatureVersion(feature.getIdentifier(), oldValue);
			}
			if (newValue != null) {
				registry.registerFeatureVersion(feature, featureVersion);
			}
		default:
			break;
		}
	}

}
