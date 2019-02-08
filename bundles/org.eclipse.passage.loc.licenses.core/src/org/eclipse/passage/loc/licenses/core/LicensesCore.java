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
package org.eclipse.passage.loc.licenses.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.passage.lic.base.LicensingPaths;
import org.eclipse.passage.lic.model.api.LicensePack;
import org.eclipse.passage.lic.registry.ProductVersionDescriptor;
import org.eclipse.passage.lic.runtime.io.StreamCodec;
import org.eclipse.passage.loc.edit.LicenseDomainRegistry;
import org.eclipse.passage.loc.edit.LocEdit;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;

public class LicensesCore {

	public static final String BUNDLE_SYMBOLIC_NAME = "org.eclipse.passage.loc.licenses.core"; //$NON-NLS-1$

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

		if (streamCodec == null) {
			String pattern = "Unable to issue license for pack keys for version %s of %s : \n codec not found";
			String message = String.format(pattern, productVersion, productIdentifier);
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