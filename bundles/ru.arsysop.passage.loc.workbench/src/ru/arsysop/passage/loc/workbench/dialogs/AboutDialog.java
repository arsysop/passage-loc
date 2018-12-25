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
package ru.arsysop.passage.loc.workbench.dialogs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;

public class AboutDialog extends Dialog {

	private static final String PRODUCT_NAME = "%product.name"; //$NON-NLS-1$
	private static final String ABOUT_TEXT = "%aboutText"; //$NON-NLS-1$
	private static final String ABOUT_IMAGE = "%aboutImage"; //$NON-NLS-1$
	private static final String ABOUT_TITLE = "%aboutTitle"; //$NON-NLS-1$

	private static final String PRODUCT_BUNDLE_URL = "platform:/plugin/%s"; //$NON-NLS-1$
	private static final String PRODUCT_BUNDLE_ABOUT_IMAGE = "%s//%s"; //$NON-NLS-1$
	private Image productLogo;

	private final TranslationService translations;
	private final LicensingImages licensingImages;
	private final IApplicationContext applicationContext;
	private final String bundleUrl;

	public AboutDialog(Shell parentShell, IEclipseContext context) {
		super(parentShell);
		translations = context.get(TranslationService.class);
		licensingImages = context.get(LicensingImages.class);
		applicationContext = context.get(IApplicationContext.class);
		bundleUrl = String.format(PRODUCT_BUNDLE_URL, applicationContext.getBrandingBundle().getSymbolicName());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String pattern = translations.translate(ABOUT_TITLE, bundleUrl);
		String name = translations.translate(PRODUCT_NAME, bundleUrl);
		newShell.setText(NLS.bind(pattern, name));
		newShell.setImage(licensingImages.getImage(LicensingImages.IMG_DEFAULT));;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout workLayout = getLayoutDialogArea();
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(workLayout);
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		String productImg = translations.translate(ABOUT_IMAGE, bundleUrl);

		if (productImg != null) {
			URL url = getUrl(productImg);
			if (url != null) {
				ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(url);
				if (imgDescriptor != null) {
					productLogo = imgDescriptor.createImage();
				}
			}
		}

		GridData lblGridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		Label lblProductLogo = new Label(base, SWT.BORDER);
		if (productLogo != null) {
			lblProductLogo.setImage(productLogo);
		}
		lblProductLogo.setLayoutData(lblGridData);

		GridData txtData = new GridData(SWT.FILL, SWT.FILL, true, true);
		String productDesription = translations.translate(ABOUT_TEXT, bundleUrl);

		StyledText txtProductDescription = new StyledText(base, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		txtProductDescription.setLayoutData(txtData);
		txtProductDescription.setText(productDesription);
		txtProductDescription.setEditable(false);

		return parent;
	}

	private GridLayout getLayoutDialogArea() {
		GridLayout workLayout = new GridLayout(2, false);
		workLayout.marginHeight = 0;
		workLayout.marginWidth = 0;
		workLayout.verticalSpacing = 0;
		workLayout.horizontalSpacing = 5;

		return workLayout;
	}

	private URL getUrl(String productImg) {
		URL url = null;
		try {
			url = new URL(String.format(PRODUCT_BUNDLE_ABOUT_IMAGE, bundleUrl, productImg));
		} catch (MalformedURLException e) {
			Logger.getLogger(AboutDialog.class.getName()).log(Level.INFO, e.getMessage(), e);
		}
		return url;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData btnData = new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1);
		Button btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		btnOk.setLayoutData(btnData);
	}

	@Override
	public boolean close() {
		if (productLogo != null) {
			productLogo.dispose();
			productLogo = null;
		}
		return super.close();
	}
}
