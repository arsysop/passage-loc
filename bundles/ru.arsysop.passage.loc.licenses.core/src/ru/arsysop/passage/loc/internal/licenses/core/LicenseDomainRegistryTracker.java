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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;

public class LicenseDomainRegistryTracker extends EContentAdapter {

	private final LicenseDomainRegistry registry;

	public LicenseDomainRegistryTracker(LicenseDomainRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof LicensePack) {
			LicensePack licensePack = (LicensePack) notifier;
			switch (notification.getFeatureID(FeatureSet.class)) {
			case LicPackage.LICENSE_PACK__IDENTIFIER:
				processLicensePackIdentifier(licensePack, notification);
				break;
				//FIXME: over identifiers
			default:
				break;
			}
		}
		super.notifyChanged(notification);
	}

	protected void processLicensePackIdentifier(LicensePack licensePack, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterLicensePack(oldValue);
			}
			if (newValue != null) {
				registry.registerLicensePack(licensePack);
			}
		default:
			break;
		}
	}

}
