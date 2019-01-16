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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.api.ProductVersionFeature;
import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductLineDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionFeatureDescriptor;
import ru.arsysop.passage.lic.registry.ProductsEvents;
import ru.arsysop.passage.lic.registry.ProductsRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + ProductsRegistry.DOMAIN_NAME,
		DomainRegistryAccess.PROPERTY_FILE_EXTENSION + '=' + ProductsRegistry.FILE_EXTENSION_XMI })
public class ProductDomainRegistryImpl extends EditingDomainBasedRegistry
		implements ProductRegistry, ProductDomainRegistry, EditingDomainRegistry {

	private final Map<String, ProductLine> productLineIndex = new HashMap<>();
	private final Map<String, Product> productIndex = new HashMap<>();
	private final Map<String, Map<String, ProductVersion>> productVersionIndex = new HashMap<>();
	private final Map<String, Map<String, Map<String, ProductVersionFeature>>> productVersionFeatureIndex = new HashMap<>();

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
	public void bindEventAdmin(EventAdmin eventAdmin) {
		super.bindEventAdmin(eventAdmin);
	}

	@Override
	public void unbindEventAdmin(EventAdmin eventAdmin) {
		super.unbindEventAdmin(eventAdmin);
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
		Collection<Map<String, Map<String, ProductVersionFeature>>> versionValues = productVersionFeatureIndex.values();
		for (Map<String, Map<String, ProductVersionFeature>> versions : versionValues) {
			Collection<Map<String, ProductVersionFeature>> values = versions.values();
			for (Map<String, ProductVersionFeature> map : values) {
				productVersionFeatures.addAll(map.values());
			}
		}
		return productVersionFeatures;
	}

	@Override
	public Iterable<ProductVersionFeatureDescriptor> getProductVersionFeatures(String productId, String version) {
		Map<String, Map<String, ProductVersionFeature>> versions = productVersionFeatureIndex.get(productId);
		if (versions == null) {
			return Collections.emptyList();
		}
		Map<String, ProductVersionFeature> map = versions.get(version);
		if (map == null) {
			return Collections.emptyList();
		}
		List<ProductVersionFeatureDescriptor> result = new ArrayList<>();
		map.values().forEach(result::add);
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
				registerProductLine(productLine);
			}
		}
	}

	@Override
	public void registerProductLine(ProductLine productLine) {
		String identifier = productLine.getIdentifier();
		ProductLine existing = productLineIndex.put(identifier, productLine);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_LINE_CREATE, productLine));
		EList<Product> products = productLine.getProducts();
		for (Product product : products) {
			registerProduct(product);
		}
	}

	@Override
	public void registerProduct(Product product) {
		String identifier = product.getIdentifier();
		Product existing = productIndex.put(identifier, product);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_CREATE, product));
		EList<ProductVersion> productVersions = product.getProductVersions();
		for (ProductVersion productVersion : productVersions) {
			registerProductVersion(product, productVersion);
		}
	}

	@Override
	public void registerProductVersion(Product product, ProductVersion productVersion) {
		String identifier = product.getIdentifier();
		Map<String, ProductVersion> versions = productVersionIndex.computeIfAbsent(identifier, key -> new HashMap<>());
		String version = productVersion.getVersion();
		ProductVersion existing = versions.put(version, productVersion);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_VERSION_CREATE, productVersion));
	}

	@Override
	public void registerProductVersionFeature(Product product, ProductVersion productVersion,
			ProductVersionFeature productVersionFeature) {
		String identifier = product.getIdentifier();
		Map<String, Map<String, ProductVersionFeature>> versions = productVersionFeatureIndex
				.computeIfAbsent(identifier, key -> new HashMap<>());
		String version = productVersion.getVersion();
		Map<String, ProductVersionFeature> features = versions.computeIfAbsent(version, key -> new HashMap<>());
		String featureIdentifier = productVersionFeature.getFeatureIdentifier();
		ProductVersionFeature existing = features.put(featureIdentifier, productVersionFeature);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_VERSION_FEATURE_CREATE, productVersionFeature));
	}

	@Override
	protected void beforeUnload(EList<EObject> contents) {
		for (EObject eObject : contents) {
			if (eObject instanceof ProductLine) {
				ProductLine productLine = (ProductLine) eObject;
				unregisterProductLine(productLine.getIdentifier());
			}
		}
	}

	@Override
	public void unregisterProductLine(String productLineId) {
		ProductLine removed = productLineIndex.remove(productLineId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_LINE_DELETE, removed));
			EList<Product> products = removed.getProducts();
			for (Product product : products) {
				unregisterProduct(product.getIdentifier());
			}
		}
	}

	@Override
	public void unregisterProduct(String productId) {
		Product removed = productIndex.remove(productId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_DELETE, removed));
			EList<ProductVersion> productVersions = removed.getProductVersions();
			for (ProductVersion productVersion : productVersions) {
				unregisterProductVersion(productId, productVersion.getVersion());
			}
		}
	}

	@Override
	public void unregisterProductVersion(String productId, String version) {
		Map<String, ProductVersion> versions = productVersionIndex.get(productId);
		if (versions != null) {
			ProductVersion removed = versions.remove(version);
			if (removed != null) {
				eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_VERSION_DELETE, removed));
			}
			if (versions.isEmpty()) {
				productVersionIndex.remove(productId);
			}
		}
	}

	@Override
	public void unregisterProductVersionFeature(String productId, String version, String featureId) {
		Map<String, Map<String, ProductVersionFeature>> versions = productVersionFeatureIndex.get(productId);
		if (versions != null) {
			Map<String, ProductVersionFeature> features = versions.get(version);
			if (features != null) {
				ProductVersionFeature removed = features.remove(featureId);
				if (removed != null) {
					eventAdmin.postEvent(createEvent(ProductsEvents.PRODUCT_VERSION_FEATURE_DELETE, removed));
				}
				if (features.isEmpty()) {
					versions.remove(version);
				}
			}
			if (versions.isEmpty()) {
				productVersionFeatureIndex.remove(productId);
			}
		}
	}

	@Override
	public EClass getContentClassifier() {
		return LicPackage.eINSTANCE.getProductLine();
	}

	@Override
	public EStructuralFeature getContentIdentifierAttribute() {
		return LicPackage.eINSTANCE.getProductLine_Identifier();
	}

	@Override
	public EStructuralFeature getContentNameAttribute() {
		return LicPackage.eINSTANCE.getProductLine_Name();
	}

}
