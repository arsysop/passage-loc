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
package ru.arsysop.passage.loc.internal.products.core;

import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductLineDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionFeatureDescriptor;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

@Component
public class OsgiInstanceProductRegistry extends EditingDomainBasedRegistry implements ProductRegistry, ProductDomainRegistry {
	
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
	public String createPassword(ProductVersionDescriptor descriptor) {
		String id = null;
		String version = null;
		if (descriptor != null) {
			ProductDescriptor product = descriptor.getProduct();
			if (product != null) {
				id = product.getIdentifier();
			}
			version = descriptor.getVersion();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(id).append("###").append(version); //$NON-NLS-1$
		return sb.toString();
	}

	@Override
	public String getFileExtension() {
		return LicModelCore.FILE_EXTENSION_PRODUCTS;
	}

	@Override
	public ProductLineDescriptor getProductLine(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDescriptor getProduct(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductLineDescriptor> getProductLines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductDescriptor> getProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductDescriptor> getProducts(String productLineId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductVersionDescriptor> getProductVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductVersionDescriptor> getProductVersions(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductVersionDescriptor getProductVersion(String product, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductVersionFeatureDescriptor> getProductVersionFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductVersionFeatureDescriptor> getProductVersionFeatures(String productId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

}
