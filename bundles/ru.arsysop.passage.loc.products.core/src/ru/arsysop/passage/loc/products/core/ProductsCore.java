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
package ru.arsysop.passage.loc.products.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ru.arsysop.passage.lic.base.LicensingPaths;
import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.loc.edit.LocEdit;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

public class ProductsCore {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.products.core"; //$NON-NLS-1$

	public static List<String> exportProductKeys(ProductVersion productVersion, ProductDomainRegistry registry,
			StreamCodec streamCodec) throws CoreException {
		String installationToken = productVersion.getInstallationToken();
		if (installationToken != null) {
			File publicFile = new File(installationToken);
			if (publicFile.exists()) {
				String pattern = "Public key already defined: \n %s";
				String message = String.format(pattern, publicFile.getAbsolutePath());
				IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
				throw new CoreException(error);
			}
		}
		String secureToken = productVersion.getSecureToken();
		if (secureToken != null) {
			File privateFile = new File(secureToken);
			if (privateFile.exists()) {
				String pattern = "Private key already defined: \n %s";
				String message = String.format(pattern, privateFile.getAbsolutePath());
				IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
				throw new CoreException(error);
			}
		}

		Product product = productVersion.getProduct();
		String errors = LocEdit.extractValidationError(product);
		if (errors != null) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, errors);
			throw new CoreException(error);
		}
		try {
			String identifier = product.getIdentifier();
			String version = productVersion.getVersion();
			Path basePath = registry.getBasePath();
			Path path = basePath.resolve(identifier).resolve(version);
			Files.createDirectories(path);
			String storageKeyFolder = path.toFile().getAbsolutePath();
			String keyFileName = identifier + '_' + version;
			String publicKeyPath = storageKeyFolder + File.separator + keyFileName
					+ LicensingPaths.EXTENSION_PRODUCT_PUBLIC;
			String privateKeyPath = storageKeyFolder + File.separator + keyFileName + LocEdit.EXTENSION_KEY_PRIVATE;
			streamCodec.createKeyPair(publicKeyPath, privateKeyPath, identifier,
					registry.createPassword(identifier, version), 1024);
			productVersion.setInstallationToken(publicKeyPath);
			productVersion.setSecureToken(privateKeyPath);

			return Arrays.asList(publicKeyPath, privateKeyPath);
		} catch (Exception e) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, "Product key export error", e);
			throw new CoreException(error);
		}
	}

}
