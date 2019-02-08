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
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.passage.lic.base.LicensingPaths;
import org.eclipse.passage.lic.model.api.LicensePack;
import org.eclipse.passage.lic.registry.ProductVersionDescriptor;
import org.eclipse.passage.lic.runtime.io.StreamCodec;
import org.eclipse.passage.loc.edit.LicenseDomainRegistry;
import org.eclipse.passage.loc.edit.LocEdit;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;

public class LicensesCore {

	public static final String BUNDLE_SYMBOLIC_NAME = "org.eclipse.passage.loc.licenses.core"; //$NON-NLS-1$

	public static String exportLicensePack(LicensePack pack, ProductDomainRegistry productRegistry, LicenseDomainRegistry licenseRegistry, StreamCodec streamCodec) throws CoreException {
		LicensePack license = EcoreUtil.copy(pack);
		String errors = LocEdit.extractValidationError(license);
		if (errors != null) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, errors);
			throw new CoreException(error);
		}
		String productIdentifier = license.getProductIdentifier();
		String productVersion = license.getProductVersion();
		Path basePath = licenseRegistry.getBasePath();
		Path path = basePath.resolve(productIdentifier).resolve(productVersion);
		String storageKeyFolder = path.toFile().getAbsolutePath();

		String uuid = UUID.randomUUID().toString();
		Date value = new Date();
		license.setIdentifier(uuid);
		license.setIssueDate(value);
		String licenseIn = storageKeyFolder + File.separator + uuid + LicensingPaths.EXTENSION_LICENSE_DECRYPTED;
		
		URI uri = URI.createFileURI(licenseIn);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(license);
		try {
			resource.save(null);
		} catch (IOException e) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, "License Pack export error", e);
			throw new CoreException(error);
		}

		if (streamCodec == null) {
			return licenseIn;
		}

		String keyFileName = productIdentifier + '_' + productVersion;
		String privateKeyPath = storageKeyFolder + File.separator + keyFileName + LocEdit.EXTENSION_KEY_PRIVATE;
		File privateProductToken = new File(privateKeyPath);
		if (!privateProductToken.exists()) {
			String pattern = "Product private key not found: \n %s";
			String message = String.format(pattern, privateProductToken.getAbsolutePath());
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, message);
			throw new CoreException(error);
		}

		String licenseOut = storageKeyFolder + File.separator + uuid + LicensingPaths.EXTENSION_LICENSE_ENCRYPTED;
		File licenseEncoded = new File(licenseOut);
		try (FileInputStream licenseInput = new FileInputStream(licenseIn);
				FileOutputStream licenseOutput = new FileOutputStream(licenseEncoded); FileInputStream keyStream = new FileInputStream(privateProductToken)) {
			String username = productIdentifier;
			ProductVersionDescriptor pvd = productRegistry.getProductVersion(productIdentifier, productVersion);
			String password = productRegistry.createPassword(pvd);
			streamCodec.encodeStream(licenseInput, licenseOutput, keyStream, username, password);
			return licenseOut;
		} catch (Exception e) {
			IStatus error = new Status(IStatus.ERROR, BUNDLE_SYMBOLIC_NAME, "License Pack export error", e);
			throw new CoreException(error);
		}
	}
	

}
