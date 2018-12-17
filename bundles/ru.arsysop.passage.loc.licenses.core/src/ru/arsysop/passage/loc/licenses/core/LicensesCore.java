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
package ru.arsysop.passage.loc.licenses.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ru.arsysop.passage.lic.base.LicensingPaths;
import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.LocEdit;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

public class LicensesCore {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.products.core"; //$NON-NLS-1$

	public static String exportLicensePack(LicensePack licensePack, ProductDomainRegistry productRegistry, LicenseDomainRegistry licenseRegistry, StreamCodec streamCodec) throws CoreException {
		String packIdentifier = licensePack.getIdentifier();
		if (packIdentifier != null) {
			String pattern = "License Pack already exported: \n %s";
			String message = String.format(pattern, packIdentifier);
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
			throw new CoreException(error);
		}
		Date issueDate = licensePack.getIssueDate();
		if (issueDate != null) {
			String pattern = "License Pack already exported: \n %s";
			String message = String.format(pattern, issueDate);
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
			throw new CoreException(error);
		}
		String errors = LocEdit.extractValidationError(licensePack);
		if (errors != null) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, errors);
			throw new CoreException(error);
		}
		String productIdentifier = licensePack.getProductIdentifier();
		String productVersion = licensePack.getProductVersion();
		Path basePath = licenseRegistry.getBasePath();
		Path path = basePath.resolve(productIdentifier).resolve(productVersion);
		String storageKeyFolder = path.toFile().getAbsolutePath();
		String keyFileName = productIdentifier + '_' + productVersion;
		String privateKeyPath = storageKeyFolder + File.separator + keyFileName + LocEdit.EXTENSION_KEY_PRIVATE;
		File privateProductToken = new File(privateKeyPath);
		if (!privateProductToken.exists()) {
			String pattern = "Product private key not found: \n %s";
			String message = String.format(pattern, privateProductToken.getAbsolutePath());
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
			throw new CoreException(error);
		}

		String uuid = UUID.randomUUID().toString();
		Date value = new Date();
		String licenseOut = storageKeyFolder + File.separator + uuid + LicensingPaths.EXTENSION_LICENSE_ENCRYPTED;
		String licenseFile = licensePack.eResource().getURI().toFileString();
		File licenseEncoded = new File(licenseOut);
		try (FileInputStream licenseInput = new FileInputStream(licenseFile);
				FileOutputStream licenseOutput = new FileOutputStream(licenseEncoded); FileInputStream keyStream = new FileInputStream(privateProductToken)) {
			String username = productIdentifier;
			ProductVersionDescriptor pvd = productRegistry.getProductVersion(productIdentifier, productVersion);
			String password = productRegistry.createPassword(pvd);
			streamCodec.encodeStream(licenseInput, licenseOutput, keyStream, username, password);
			licensePack.setIdentifier(uuid);;
			licensePack.setIssueDate(value);;
			return licenseOut;
		} catch (Exception e) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, "License Pack export error", e);
			throw new CoreException(error);
		}
	}
	

}
