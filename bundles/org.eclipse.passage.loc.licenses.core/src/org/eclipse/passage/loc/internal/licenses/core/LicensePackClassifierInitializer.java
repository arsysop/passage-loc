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
package org.eclipse.passage.loc.internal.licenses.core;

import org.eclipse.passage.lic.emf.edit.ClassifierInitializer;
import org.eclipse.passage.lic.emf.edit.DomainRegistryAccess;
import org.eclipse.passage.lic.registry.LicensesRegistry;
import org.osgi.service.component.annotations.Component;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + LicensesRegistry.DOMAIN_NAME })
public final class LicensePackClassifierInitializer implements ClassifierInitializer {
	@Override
	public String newObjectIdentifier() {
		return "new.license.pack"; //$NON-NLS-1$
	}

	@Override
	public String newObjectName() {
		return "New License Pack";
	}

	@Override
	public String newFileName() {
		return "new_license_pack"; //$NON-NLS-1$
	}

	@Override
	public String newObjectTitle() {
		return "License Pack";
	}

	@Override
	public String newObjectMessage() {
		return "New License Pack";
	}

	@Override
	public String newFileMessage() {
		return "Please specify a file name to store license data";
	}
}