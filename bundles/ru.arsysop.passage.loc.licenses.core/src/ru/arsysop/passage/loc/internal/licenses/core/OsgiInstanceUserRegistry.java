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

import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;
import ru.arsysop.passage.loc.licenses.core.LocLicensesCore;

@Component
public class OsgiInstanceUserRegistry extends EditingDomainBasedRegistry<UserDescriptor> implements UserRegistry, UserDomainRegistry {
	
	@Override
	protected Class<UserDescriptor> getDescriptorClass() {
		return UserDescriptor.class;
	}

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
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deactivate() {
		super.deactivate();
	}

	@Override
	public String getFileExtension() {
		return LocLicensesCore.FILE_EXTENSION_USERS;
	}

}
