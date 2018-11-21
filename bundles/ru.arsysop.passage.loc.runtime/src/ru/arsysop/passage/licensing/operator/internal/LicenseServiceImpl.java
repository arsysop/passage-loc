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
package ru.arsysop.passage.licensing.operator.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.licensing.operator.LicensingAdmin;
import ru.arsysop.passage.licensing.operator.LicensingDescriptor;
import ru.arsysop.passage.licensing.operator.LicensingService;

/**
 * 
 * @author Kovalchuk Sergey
 *
 */
public class LicenseServiceImpl implements LicensingService {

	private static final String PRODUCT_PUBLIC_KEY_ATTR = "publicKey";
	private static final String PRODUCT_VERSION = "productVersion";

	private MultiStatus licenseServiceErrorStatus = new MultiStatus(LicensingAdmin.BUNDLE_SYMBOLIC_NAME, IStatus.OK, "",
			null);

	List<LicensingDescriptor> licenseDescriptors = new ArrayList<>();

	ProductDescriptor productDecsriptor;

	@Override
	public List<LicensingDescriptor> getLicenseDescriptors() {
		return licenseDescriptors;
	}

	@Override
	public MultiStatus getServiceErrorStatus() {
		return licenseServiceErrorStatus;
	}

	private ProductDescriptor createProductDescriptor() {
		LicFactory factory = LicPackage.eINSTANCE.getLicFactory();
		Product productDecsriptor = factory.createProduct();
		ProductVersion productVersionDescriptor = factory.createProductVersion();
		IProduct productInfo = Platform.getProduct();
		if (productInfo == null) {
			String msg = NLS.bind("Product definition could not be found", null);
			licenseServiceErrorStatus.add(new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg));
			return productDecsriptor;
		}
		String productId = productInfo.getId();
		if (productId == null || productId.isEmpty()) {
			String msg = NLS.bind("Product id could not be found", null);
			licenseServiceErrorStatus.add(new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg));
			return productDecsriptor;
		}
		productDecsriptor.setIdentifier(productId);

		String productPublicKey = productInfo.getProperty(PRODUCT_PUBLIC_KEY_ATTR);
		if (productPublicKey == null || productPublicKey.isEmpty()) {
			String msg = NLS.bind("Public key for product id {0} not defined.", productId);
			licenseServiceErrorStatus.add(new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg));
			return productDecsriptor;
		}
		String productVersion = productInfo.getProperty(PRODUCT_VERSION);
		if (productVersion == null || productVersion.isEmpty()) {
			String msg = NLS.bind("Product version for product {0} not defined.", productId);
			licenseServiceErrorStatus.add(new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg));
			return productDecsriptor;
		}
		productVersionDescriptor.setVersion(productVersion);
		String resolvedPath = productPublicKey;
		File userHomeProductPath = new File(resolvedPath);
		if (userHomeProductPath.exists()) {
			String productTokenPath = userHomeProductPath.getAbsolutePath().toString();
			productVersionDescriptor.setInstallationToken(productTokenPath);
		} else {
			String msg = NLS.bind("Public key by path <{0}> for product id <{1}> does not exists.", resolvedPath,
					productId);
			licenseServiceErrorStatus.add(new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg));
		}
		return productDecsriptor;
	}

	@Override
	public ProductDescriptor getProductDescriptor() {
		if (productDecsriptor == null) {
			productDecsriptor = createProductDescriptor();
		}
		return productDecsriptor;
	}

}
