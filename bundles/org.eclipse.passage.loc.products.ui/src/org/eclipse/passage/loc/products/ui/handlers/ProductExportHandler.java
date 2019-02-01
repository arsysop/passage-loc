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
package org.eclipse.passage.loc.products.ui.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.passage.lic.model.api.ProductVersion;
import org.eclipse.passage.lic.runtime.io.StreamCodec;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;
import org.eclipse.passage.loc.products.core.ProductsCore;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

public class ProductExportHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ProductVersion productVersion,
			IEclipseContext context) {
		ProductDomainRegistry registry = context.get(ProductDomainRegistry.class);
		StreamCodec streamCodec = context.get(StreamCodec.class);
		Shell shell = context.get(Shell.class);
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