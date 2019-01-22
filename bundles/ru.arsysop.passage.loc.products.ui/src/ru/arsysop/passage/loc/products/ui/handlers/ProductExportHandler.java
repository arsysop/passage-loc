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

import java.util.List;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.products.core.ProductsCore;

public class ProductExportHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ProductVersion productVersion, Shell shell, ProductDomainRegistry registry, StreamCodec streamCodec) {
		try {
			List<String> exportProductKeys = ProductsCore.exportProductKeys(productVersion, registry, streamCodec);
			String format = "Product keys exported succesfully: \n\n %s \n %s \n";
			String message = String.format(format, exportProductKeys.toArray());
			MessageDialog.openInformation(shell, "Product Key Export", message);
		} catch (CoreException e) {
			IStatus status = e.getStatus();
			Bundle bundle = Platform.getBundle(status.getPlugin());
			Platform.getLog(bundle).log(status);
			ErrorDialog.openError(shell, "Error", "Error during product key export", status);
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional ProductVersion productVersion) {
		return productVersion != null;
	}

}