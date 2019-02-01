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
package org.eclipse.passage.loc.internal.users.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.passage.lic.emf.edit.DomainContentAdapter;
import org.eclipse.passage.lic.model.api.User;
import org.eclipse.passage.lic.model.api.UserOrigin;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.loc.edit.UserDomainRegistry;

public class UserDomainRegistryTracker extends DomainContentAdapter<UserDomainRegistry> {
	
	public UserDomainRegistryTracker(UserDomainRegistry registry) {
		super(registry);
	}

	@Override
	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof UserOrigin) {
			UserOrigin userOrigin = (UserOrigin) notifier;
			switch (notification.getFeatureID(UserOrigin.class)) {
			case LicPackage.USER_ORIGIN__IDENTIFIER:
				processUserOriginIdentifier(userOrigin, notification);
				break;
			case LicPackage.USER_ORIGIN__USERS:
				processUserOriginUsers(userOrigin, notification);
				break;
			default:
				break;
			}
		} else if (notifier instanceof User) {
			User user = (User) notifier;
			switch (notification.getFeatureID(User.class)) {
			case LicPackage.USER__EMAIL:
				processUserEmail(user, notification);
			default:
				break;
			}
		}
		super.notifyChanged(notification);
	}

	protected void processUserOriginIdentifier(UserOrigin userOrigin, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterUserOrigin(oldValue);
			}
			if (newValue != null) {
				registry.registerUserOrigin(userOrigin);
			}
		default:
			break;
		}
	}

	protected void processUserOriginUsers(UserOrigin userOrigin, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof User) {
				User user = (User) newValue;
				String email = user.getEmail();
				if (email != null) {
					registry.registerUser(user);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof User) {
				User user = (User) oldValue;
				String email = user.getEmail();
				if (email != null) {
					registry.unregisterUser(email);
				}
			}
			break;

		default:
			break;
		}
	}

	protected void processUserEmail(User user, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterUser(oldValue);
			}
			if (newValue != null) {
				registry.registerUser(user);
			}
		default:
			break;
		}
	}

}
