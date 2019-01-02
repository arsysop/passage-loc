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
package ru.arsysop.passage.loc.internal.users.core;

import org.osgi.service.component.annotations.Component;

import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.registry.UsersRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + UsersRegistry.DOMAIN_NAME })
public final class UserOriginClassifierInitializer implements ClassifierInitializer {

	@Override
	public String newObjectIdentifier() {
		return "new.user.origin";
	}

	@Override
	public String newObjectName() {
		return "New User Origin";
	}

	@Override
	public String newFileName() {
		return "new_user_origin"; //$NON-NLS-1$ ;
	}

	@Override
	public String newObjectTitle() {
		return "User Origin";
	}

	@Override
	public String newObjectMessage() {
		return "New User Origin";
	}

	@Override
	public String newFileMessage() {
		return "Please specify a file name to store user data";
	}
}