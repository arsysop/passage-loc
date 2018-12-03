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
package ru.arsysop.passage.loc.products.ui.handlers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.products.core.ProductsCore;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class ProductExportHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ProductVersion productVersion, Shell shell, ProductDomainRegistry registry, StreamCodec streamCodec) {
		String installationToken = productVersion.getInstallationToken();
		if (installationToken != null) {
			File publicFile = new File(installationToken);
			if (publicFile.exists()) {
				String pattern = "Public key already defined: \n %s";
				MessageDialog.openError(shell, "Error", String.format(pattern, publicFile.getAbsolutePath()));
				return;
			}
		}
		String secureToken = productVersion.getSecureToken();
		if (secureToken != null) {
			File privateFile = new File(secureToken);
			if (privateFile.exists()) {
				String pattern = "Private key already defined: \n %s";
				MessageDialog.openError(shell, "Error", String.format(pattern, privateFile.getAbsolutePath()));
				return;
			}
		}

		Product product = productVersion.getProduct();
		String errors = LocWokbench.extractValidationError(product);
		if (errors != null) {
			MessageDialog.openError(shell, "Error", errors);
			return;
		}
		try {
			String identifier = product.getIdentifier();
			String version = productVersion.getVersion();
			Path basePath = registry.getBasePath();
			Path path = basePath.resolve(identifier).resolve(version);
			Files.createDirectories(path);
			String storageKeyFolder = path.toFile().getAbsolutePath();
			String keyFileName = identifier + '_' + version;
			String publicKeyPath = storageKeyFolder + File.separator + keyFileName + ".pub";
			String privateKeyPath = storageKeyFolder + File.separator + keyFileName + ".skr";
			streamCodec.createKeyPair(publicKeyPath, privateKeyPath, product.getName(),
					registry.createPassword(productVersion), 1024);
			productVersion.setInstallationToken(publicKeyPath);
			productVersion.setSecureToken(privateKeyPath);

			String format = "Product keys exported succesfully: \n\n %s \n %s \n";
			String message = String.format(format, publicKeyPath, privateKeyPath);
			MessageDialog.openInformation(shell, "Product Key Export", message);

		} catch (Exception e) {
			// TODO: log
			e.printStackTrace();
			IStatus error = new Status(IStatus.ERROR, ProductsCore.BUNDLE_SYMBOLIC_NAME, "Product key export error", e);
			ErrorDialog.openError(shell, "Error", "Error during product key export", error);
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional ProductVersion productVersion) {
		return productVersion != null;
	}

}