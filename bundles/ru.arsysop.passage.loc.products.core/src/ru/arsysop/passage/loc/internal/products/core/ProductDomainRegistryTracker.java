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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.api.ProductVersionFeature;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

public class ProductDomainRegistryTracker extends EContentAdapter {
	
	private final ProductDomainRegistry registry;
	
	public ProductDomainRegistryTracker(ProductDomainRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof ProductLine) {
			ProductLine productLine = (ProductLine) notifier;
			switch (notification.getFeatureID(ProductLine.class)) {
			case LicPackage.PRODUCT_LINE__IDENTIFIER:
				processProductLineIdentifier(productLine, notification);
				break;
			case LicPackage.PRODUCT_LINE__PRODUCTS:
				processProductLineProducts(productLine, notification);
				break;
			default:
				break;
			}
		} else if (notifier instanceof Product) {
			Product product = (Product) notifier;
			switch (notification.getFeatureID(Product.class)) {
			case LicPackage.PRODUCT__IDENTIFIER:
				processProductIdentifier(product, notification);
			case LicPackage.PRODUCT__PRODUCT_VERSIONS:
				processProductProductVersions(product, notification);
			default:
				break;
			}
		} else if (notifier instanceof ProductVersion) {
			ProductVersion productVersion = (ProductVersion) notifier;
			switch (notification.getFeatureID(ProductVersion.class)) {
			case LicPackage.PRODUCT_VERSION__VERSION:
				processProductVersionVersion(productVersion, notification);
			case LicPackage.PRODUCT_VERSION__PRODUCT_VERSION_FEATURES:
				processProductVersionProductVersionFeatures(productVersion, notification);
			default:
				break;
			}
		} else if (notifier instanceof ProductVersionFeature) {
			ProductVersionFeature productVersionFeature = (ProductVersionFeature) notifier;
			switch (notification.getFeatureID(ProductVersionFeature.class)) {
			case LicPackage.PRODUCT_VERSION_FEATURE__FEATURE_IDENTIFIER:
				processProductVersionFeatureFeatureIdentifier(productVersionFeature, notification);
			default:
				break;
			}
		}
		super.notifyChanged(notification);
	}

	protected void processProductLineIdentifier(ProductLine productLine, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterProductLine(oldValue);
			}
			if (newValue != null) {
				registry.registerProductLine(productLine);
			}
		default:
			break;
		}
	}

	protected void processProductLineProducts(ProductLine productLine, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof Product) {
				Product product = (Product) newValue;
				String identifier = product.getIdentifier();
				if (identifier != null) {
					registry.registerProduct(product);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof Product) {
				Product product = (Product) oldValue;
				String identifier = product.getIdentifier();
				if (identifier != null) {
					registry.unregisterProduct(identifier);
				}
			}
			break;

		default:
			break;
		}
	}

	protected void processProductIdentifier(Product product, Notification notification) {
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterProduct(oldValue);
			}
			if (newValue != null) {
				registry.registerProduct(product);
			}
		default:
			break;
		}
	}

	protected void processProductProductVersions(Product product, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof ProductVersion) {
				ProductVersion productVersion = (ProductVersion) newValue;
				String version = productVersion.getVersion();
				if (version != null) {
					registry.registerProductVersion(product, productVersion);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof ProductVersion) {
				ProductVersion productVersion = (ProductVersion) oldValue;
				String version = productVersion.getVersion();
				if (version != null) {
					registry.unregisterProductVersion(product.getIdentifier(), version);
				}
			}
			break;

		default:
			break;
		}
	}

	protected void processProductVersionVersion(ProductVersion productVersion, Notification notification) {
		Product product = productVersion.getProduct();
		if (product == null) {
			//FIXME: warn
			return;
		}
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				registry.unregisterProductVersion(product.getIdentifier(), oldValue);
			}
			if (newValue != null) {
				registry.registerProductVersion(product, productVersion);
			}
		default:
			break;
		}
	}

	protected void processProductVersionProductVersionFeatures(ProductVersion productVersion, Notification notification) {
		Product product = productVersion.getProduct();
		if (product == null) {
			//FIXME: warn
			return;
		}
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			if (newValue instanceof ProductVersionFeature) {
				ProductVersionFeature productVersionFeature = (ProductVersionFeature) newValue;
				String featureId = productVersionFeature.getFeatureIdentifier();
				if (featureId != null) {
					registry.registerProductVersionFeature(product, productVersion, productVersionFeature);
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof ProductVersionFeature) {
				ProductVersionFeature productVersionFeature = (ProductVersionFeature) oldValue;
				String featureId = productVersionFeature.getFeatureIdentifier();
				if (featureId != null) {
					registry.unregisterProductVersionFeature(product.getIdentifier(), productVersion.getVersion(), featureId);
				}
			}
			break;

		default:
			break;
		}
	}

	protected void processProductVersionFeatureFeatureIdentifier(ProductVersionFeature productVersionFeature, Notification notification) {
		ProductVersion productVersion = productVersionFeature.getProductVersion();
		if (productVersion == null) {
			//FIXME: warn
			return;
		}
		Product product = productVersion.getProduct();
		if (product == null) {
			//FIXME: warn
			return;
		}
		String oldValue = notification.getOldStringValue();
		String newValue = notification.getNewStringValue();
		switch (notification.getEventType()) {
		case Notification.SET:
			if (oldValue != null) {
				String productId = product.getIdentifier();
				String version = productVersion.getVersion();
				registry.unregisterProductVersionFeature(productId, version, oldValue);
			}
			if (newValue != null) {
				registry.registerProductVersionFeature(product, productVersion, productVersionFeature);
			}
		default:
			break;
		}
	}
}
