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

import java.util.Collections;

import org.eclipse.passage.lic.emf.edit.DomainRegistryAccess;
import org.eclipse.passage.lic.emf.edit.SelectionCommandAdvisor;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.lic.registry.LicensesRegistry;
import org.eclipse.passage.loc.edit.LicenseDomainRegistry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + LicensesRegistry.DOMAIN_NAME })
public class LicensesSelectionCommandAdvisor implements SelectionCommandAdvisor {
	
	private LicenseDomainRegistry registry;
	
	@Reference
	public void bindDomainRegistry(LicenseDomainRegistry registry) {
		this.registry = registry;
	}

	public void unbindDomainRegistry(LicenseDomainRegistry registry) {
		this.registry = null;
	}

	@Override
	public String getSelectionTitle(String classifier) {
		if (LicPackage.eINSTANCE.getLicensePack().getName().equals(classifier)) {
			return "Select License Pack";
		}
		return null;
	}

	@Override
	public Iterable<?> getSelectionInput(String classifier) {
		if (registry == null) {
			return Collections.emptyList();
		}
		if (LicPackage.eINSTANCE.getLicensePack().getName().equals(classifier)) {
			return registry.getLicensePacks();
		}
		return Collections.emptyList();
	}

}
