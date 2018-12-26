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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.model.api.ProductVersion;
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
public class ProductDomainRegistryImpl extends EditingDomainBasedRegistry implements ProductRegistry, ProductDomainRegistry {
	
	private final Map<String, ProductLine> productLineIndex = new HashMap<>();
	private final Map<String, Product> productIndex = new HashMap<>();
	private final Map<String, Map<String, ProductVersion>> productVersionIndex = new HashMap<>();

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
	public void activate(Map<String, Object> properties) {
		super.activate(properties);
	}

	@Deactivate
	@Override
	public void deactivate(Map<String, Object> properties) {
		Collection<Map<String, ProductVersion>> values = productVersionIndex.values();
		for (Map<String, ProductVersion> map : values) {
			map.clear();
		}
		productVersionIndex.clear();
		productIndex.clear();
		productLineIndex.clear();
		super.deactivate(properties);
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
	public Iterable<ProductLineDescriptor> getProductLines() {
		return new ArrayList<>(productLineIndex.values());
	}

	@Override
	public ProductLineDescriptor getProductLine(String identifier) {
		return productLineIndex.get(identifier);
	}

	@Override
	public Iterable<ProductDescriptor> getProducts() {
		return new ArrayList<>(productIndex.values());
	}

	@Override
	public Iterable<ProductDescriptor> getProducts(String productLineId) {
		ProductLine productLine = productLineIndex.get(productLineId);
		if (productLine == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(productLine.getProducts());
	}

	@Override
	public ProductDescriptor getProduct(String productId) {
		return productIndex.get(productId);
	}

	@Override
	public Iterable<ProductVersionDescriptor> getProductVersions() {
		List<ProductVersionDescriptor> list = new ArrayList<>();
		Collection<Map<String, ProductVersion>> values = productVersionIndex.values();
		for (Map<String, ProductVersion> map : values) {
			list.addAll(map.values());
		}
		return list;
	}

	@Override
	public Iterable<ProductVersionDescriptor> getProductVersions(String productId) {
		Map<String, ProductVersion> map = productVersionIndex.get(productId);
		if (map == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(map.values());
	}

	@Override
	public ProductVersionDescriptor getProductVersion(String product, String version) {
		Map<String, ProductVersion> map = productVersionIndex.get(product);
		if (map == null) {
			return null;
		}
		return map.get(version);
	}

	@Override
	public Iterable<ProductVersionFeatureDescriptor> getProductVersionFeatures() {
		List<ProductVersionFeatureDescriptor> productVersionFeatures = new ArrayList<>();
		Iterable<ProductVersionDescriptor> productVersions = getProductVersions();
		for (ProductVersionDescriptor productVersion : productVersions) {
			Iterable<? extends ProductVersionFeatureDescriptor> features = productVersion.getProductVersionFeatures();
			features.forEach(productVersionFeatures::add);
		}
		return productVersionFeatures;
	}

	@Override
	public Iterable<ProductVersionFeatureDescriptor> getProductVersionFeatures(String productId, String version) {
		ProductVersionDescriptor productVersion = getProductVersion(productId, version);
		if (productVersion == null) {
			return Collections.emptyList();
		}
		List<ProductVersionFeatureDescriptor> result = new ArrayList<>();
		productVersion.getProductVersionFeatures().forEach(result::add);
		return result;
	}

	@Override
	protected EContentAdapter createContentAdapter() {
		return new ProductDomainRegistryTracker(this);
	}

	@Override
	protected void afterLoad(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof ProductLine) {
				ProductLine productLine = (ProductLine) eObject;
				addedProductLine(productLine);
			}
		}
	}

	protected void addedProductLine(ProductLine productLine) {
		String identifier = productLine.getIdentifier();
		productLineIndex.put(identifier, productLine);
		EList<Product> products = productLine.getProducts();
		for (Product product : products) {
			addedProduct(product);
		}
	}

	protected void addedProduct(Product product) {
		String identifier = product.getIdentifier();
		productIndex.put(identifier, product);
		EList<ProductVersion> productVersions = product.getProductVersions();
		for (ProductVersion productVersion : productVersions) {
			addedProductVersion(product, productVersion);
		}
	}

	protected void addedProductVersion(Product product, ProductVersion productVersion) {
		String identifier = product.getIdentifier();
		Map<String, ProductVersion> map = productVersionIndex.computeIfAbsent(identifier, key -> new HashMap<>());
		String version = productVersion.getVersion();
		map.put(version, productVersion);
	}

	@Override
	protected void beforeUnload(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof ProductLine) {
				ProductLine productLine = (ProductLine) eObject;
				removedProductLine(productLine);
			}
		}
	}

	protected void removedProductLine(ProductLine productLine) {
		String identifier = productLine.getIdentifier();
		productLineIndex.remove(identifier);
		EList<Product> products = productLine.getProducts();
		for (Product product : products) {
			removedProduct(product);
		}
	}

	protected void removedProduct(Product product) {
		String identifier = product.getIdentifier();
		productIndex.remove(identifier);
		EList<ProductVersion> productVersions = product.getProductVersions();
		for (ProductVersion productVersion : productVersions) {
			removedProductVersion(product, productVersion);
		}
	}

	protected void removedProductVersion(Product product, ProductVersion productVersion) {
		String identifier = product.getIdentifier();
		Map<String, ProductVersion> map = productVersionIndex.get(identifier);
		if (map != null) {
			String version = productVersion.getVersion();
			map.remove(version);
			if (map.isEmpty()) {
				productVersionIndex.remove(identifier);
			}
		}
	}

}
