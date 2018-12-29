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
package ru.arsysop.passage.loc.edit;

import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.api.ProductVersionFeature;
import ru.arsysop.passage.lic.registry.DescriptorRegistry;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;

public interface ProductDomainRegistry extends ProductRegistry, EditingDomainRegistry, DescriptorRegistry {

	String createPassword(ProductVersionDescriptor productVersion);

	void registerProductLine(ProductLine productLine);

	void registerProduct(Product product);

	void registerProductVersion(Product product, ProductVersion productVersion);

	void registerProductVersionFeature(Product product, ProductVersion productVersion,
			ProductVersionFeature productVersionFeature);

	void unregisterProductLine(String productLineId);

	void unregisterProduct(String productId);

	void unregisterProductVersion(String productId, String version);

	void unregisterProductVersionFeature(String productId, String version, String featureId);

}
