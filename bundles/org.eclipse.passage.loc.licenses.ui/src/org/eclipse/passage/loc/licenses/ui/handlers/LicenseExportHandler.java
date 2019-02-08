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
package org.eclipse.passage.loc.licenses.ui.handlers;

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
import org.eclipse.passage.lic.model.api.LicensePack;
import org.eclipse.passage.lic.runtime.io.StreamCodec;
import org.eclipse.passage.loc.edit.LicenseDomainRegistry;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;
import org.eclipse.passage.loc.licenses.core.LicensesCore;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

public class LicenseExportHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) LicensePack licensePack, IEclipseContext context) {
		ProductDomainRegistry productRegistry = context.get(ProductDomainRegistry.class);
		LicenseDomainRegistry licenseRegistry = context.get(LicenseDomainRegistry.class);
		StreamCodec streamCodec = context.get(StreamCodec.class);
		Shell shell = context.get(Shell.class);
		try {
			String exportLicense = LicensesCore.exportLicensePack(licensePack, productRegistry , licenseRegistry , streamCodec );
			String format = "License pack exported succesfully: \n\n %s \n";
			String message = String.format(format, exportLicense);
			MessageDialog.openInformation(shell , "License Pack Export", message);
		} catch (CoreException e) {
			IStatus status = e.getStatus();
			Bundle bundle = Platform.getBundle(status.getPlugin());
			Platform.getLog(bundle).log(status);
			ErrorDialog.openError(shell, "Error", "Error during license pack export", e.getStatus());
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional LicensePack licensePack) {
		return licensePack != null;
	}

}