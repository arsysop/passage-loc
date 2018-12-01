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
package ru.arsysop.passage.lic.inspector.ui.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.event.Event;

import ru.arsysop.passage.lic.base.LicensingConfigurations;
import ru.arsysop.passage.lic.runtime.AccessManager;

public class LicensingInspectorAddon {

	private final EnvironmentInfo environmentInfo;
	private final AccessManager accessManager;

	@Inject
	public LicensingInspectorAddon(EnvironmentInfo environmentInfo, AccessManager accessManager) {
		this.environmentInfo = environmentInfo;
		this.accessManager = accessManager;
	}

	@Inject
	@Optional
	public void applicationStarted(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		String[] args = environmentInfo.getNonFrameworkArgs();
		Object configuration = LicensingConfigurations.extractProductIdentifier(args);
		accessManager.executeAccessRestrictions(configuration);
	}

}
