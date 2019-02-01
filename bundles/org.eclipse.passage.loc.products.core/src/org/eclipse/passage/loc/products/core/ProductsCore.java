/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.loc.products.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.passage.lic.base.LicensingPaths;
import org.eclipse.passage.lic.model.api.Product;
import org.eclipse.passage.lic.model.api.ProductVersion;
import org.eclipse.passage.lic.runtime.io.StreamCodec;
import org.eclipse.passage.loc.edit.LocEdit;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;

public class ProductsCore {

	public static final String BUNDLE_SYMBOLIC_NAME = "org.eclipse.passage.loc.products.core"; //$NON-NLS-1$

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
		String identifier = product.getIdentifier();
		String version = productVersion.getVersion();
		if (streamCodec == null) {
			String pattern = "Unable to create keys for version %s of %s : \n codec not found";
			String message = String.format(pattern, version, product.getName());
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
			throw new CoreException(error);
		}
		Path basePath = registry.getBasePath();
		try {
			Path path = basePath.resolve(identifier).resolve(version);
			Files.createDirectories(path);
			String storageKeyFolder = path.toFile().getAbsolutePath();
			String keyFileName = identifier + '_' + version;
			String publicKeyPath = storageKeyFolder + File.separator + keyFileName
					+ LicensingPaths.EXTENSION_PRODUCT_PUBLIC;
			String privateKeyPath = storageKeyFolder + File.separator + keyFileName + LocEdit.EXTENSION_KEY_PRIVATE;
			streamCodec.createKeyPair(publicKeyPath, privateKeyPath, identifier,
					registry.createPassword(productVersion));
			productVersion.setInstallationToken(publicKeyPath);
			productVersion.setSecureToken(privateKeyPath);

			return Arrays.asList(publicKeyPath, privateKeyPath);
		} catch (Exception e) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, "Product key export error", e);
			throw new CoreException(error);
		}
	}

}
