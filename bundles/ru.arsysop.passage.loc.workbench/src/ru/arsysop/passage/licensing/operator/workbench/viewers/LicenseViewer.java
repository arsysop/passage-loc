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
package ru.arsysop.passage.licensing.operator.workbench.viewers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.lic.registry.FeatureRegistry;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.licensing.operator.LicenseDescriptor;
import ru.arsysop.passage.licensing.operator.LicenseService;
import ru.arsysop.passage.licensing.operator.workbench.dialogs.AbstractDialog;
import ru.arsysop.passage.licensing.operator.workbench.utils.PassageUI;

public class LicenseViewer {
	public static String ID = "ru.arsysop.passage.licensing.operator.workbench.part.license";
	public static String TITLE = "Registry of the licenses";
	private TableViewer viewer;
	
	@Inject
	UserRegistry userService;

	@Inject
	ProductRegistry productService;

	@Inject
	FeatureRegistry componentService;

	@Inject
	LicenseService licenseService;

	@Inject
	StreamCodec permissionCodec;

	@PostConstruct
	public void postConstruct(Composite parent) {

		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(new GridLayout());

		GridData layoutDataBase = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		base.setLayoutData(layoutDataBase);

		GridData layoutDataTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		viewer = new TableViewer(base);
		viewer.getTable().setLayoutData(layoutDataTable);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		TableViewerColumn clnId = new TableViewerColumn(viewer, SWT.LEAD);
		clnId.getColumn().setText("id");
		clnId.getColumn().setWidth(40);
		clnId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
//					return ((LicensePackDescriptor) element).getId();
				}
				return "";
			}
		});

		TableViewerColumn clmComponentName = new TableViewerColumn(viewer, SWT.LEAD);
		clmComponentName.getColumn().setText("Component name");
		clmComponentName.getColumn().setWidth(150);
		clmComponentName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
					return ((LicenseDescriptor) element).getFeatureVersion().getFeature().getName();
				}
				return "";
			}
		});

		TableViewerColumn clmComponentVersion = new TableViewerColumn(viewer, SWT.LEAD);
		clmComponentVersion.getColumn().setText("Component version");
		clmComponentVersion.getColumn().setWidth(150);
		clmComponentVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
					return ((LicenseDescriptor) element).getFeatureVersion().getVersion();
				}
				return "";
			}
		});

		TableViewerColumn clmProductID = new TableViewerColumn(viewer, SWT.LEAD);
		clmProductID.getColumn().setText("Product id");
		clmProductID.getColumn().setWidth(100);
		clmProductID.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
					return ((LicenseDescriptor) element).getProductVersion().getProduct().getIdentifier();
				}
				return "";
			}
		});

		TableViewerColumn clmProductName = new TableViewerColumn(viewer, SWT.LEAD);
		clmProductName.getColumn().setText("Product name");
		clmProductName.getColumn().setWidth(150);
		clmProductName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
					return ((LicenseDescriptor) element).getProductVersion().getProduct().getName();
				}
				return "";
			}
		});

		TableViewerColumn clmUserName = new TableViewerColumn(viewer, SWT.LEAD);
		clmUserName.getColumn().setText("Client name");
		clmUserName.getColumn().setWidth(100);
		clmUserName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
					return ((LicenseDescriptor) element).getProductVersion().toString();
				}
				return "";
			}
		});

		TableViewerColumn clmUserEmail = new TableViewerColumn(viewer, SWT.LEAD);
		clmUserEmail.getColumn().setText("Client email");
		clmUserEmail.getColumn().setWidth(150);
		clmUserEmail.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
//					return ((LicensePack) element).getUserInfo().getEmail();
				}
				return "";
			}
		});

		TableViewerColumn clmExpDate = new TableViewerColumn(viewer, SWT.LEAD);
		clmExpDate.getColumn().setText("Experation date");
		clmExpDate.getColumn().setWidth(100);
		clmExpDate.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
//					return LicenseDescriptorValidator.reductionDate(((LicensePack) element).getExpireDate());
				}
				return "";
			}
		});

		TableViewerColumn clmToken = new TableViewerColumn(viewer, SWT.LEAD);
		clmToken.getColumn().setText("Token");
		clmToken.getColumn().setWidth(300);
		clmToken.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof LicenseDescriptor) {
//					return ((LicensePack) element).getToken();
				}
				return "";
			}
		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(licenseService.getLicenses());

		Composite bottomBar = new Composite(base, SWT.BORDER);
		bottomBar.setLayout(new GridLayout(3, false));
		GridData dataBottomBar = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		dataBottomBar.heightHint = 40;
		bottomBar.setLayoutData(dataBottomBar);

		Button bntCreateLicense = new Button(bottomBar, SWT.PUSH);
		bntCreateLicense.setText("Create license");
		bntCreateLicense.setLayoutData(PassageUI.getButtonLayoutData());
		bntCreateLicense.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = Display.getDefault().getActiveShell();
				LicenseGenDialog dialog = new LicenseGenDialog(shell);
				dialog.open();
			}
		});

		Button btnDecodeCheck = new Button(bottomBar, SWT.PUSH);
		btnDecodeCheck.setText("Check params");
		btnDecodeCheck.setLayoutData(PassageUI.getButtonLayoutData());
		btnDecodeCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = viewer.getTable().getSelection();
				if (items != null && items.length == 1) {
					if (items[0].getData() instanceof LicenseDescriptor) {
						LicenseDescriptor license = (LicenseDescriptor) items[0].getData();
						Path pathLicense = Paths.get(null/*license.getToken()*/);

						ProductVersionDescriptor productVersion = license.getProductVersion();
						Path pathProductPublicToken = Paths.get(productVersion.getInstallationToken());
						try (FileInputStream licenseInput = new FileInputStream(pathLicense.toFile()); ByteArrayOutputStream licenseOutput = new ByteArrayOutputStream(); FileInputStream keyInput = new FileInputStream(pathProductPublicToken.toFile()) ) {
							permissionCodec.decodeStream(licenseInput, licenseOutput, keyInput, null);
							StringBuffer decBuffer = new StringBuffer();
							String licenseContent = new String(licenseOutput.toByteArray());
							decBuffer.append(licenseContent);

							MessageDialog.openInformation(Display.getDefault().getActiveShell(), "License",
									decBuffer.toString());
						} catch (Exception exc) {
							MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", exc.getMessage());
						}

					}
				}
			}
		});

	}

	class LicenseGenDialog extends AbstractDialog {
		private Shell activeShell = Display.getDefault().getActiveShell();
		private DateTime dtime;
		private Combo cmbProduct;
		private FeatureVersionDescriptor featureVersionSelected;
		private UserDescriptor userInfoSelected;
		private ProductVersionDescriptor productVersionSelected;
		private Text txtSelectFloating;

		protected LicenseGenDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite cmps = new Composite(parent, SWT.BORDER);
			cmps.setLayoutData(createAreaDefaultLayoutData());
			cmps.setLayout(new GridLayout(2, false));

			Label lblProduct = new Label(cmps, SWT.NONE);
			lblProduct.setText("Select product:");
			lblProduct.setLayoutData(createLabelLayoutData());

			cmbProduct = new Combo(cmps, SWT.BORDER);
			final Iterable<ProductDescriptor> products = new ArrayList<>();
			for (ProductDescriptor iter : products) {
				for (ProductDescriptor productDescriptor : products) {
					if (products instanceof Product) {
						Product product = (Product) products;
						//FIXME: get versions
						cmbProduct.add(iter.getName());
					}
				}
			}
			cmbProduct.select(0);
			cmbProduct.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			cmbProduct.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = cmbProduct.getSelectionIndex();
					//FIXME: rework
					productVersionSelected = null;
				}
			});
			//FIXME: rework
			productVersionSelected = null;

			Label lblComponent = new Label(cmps, SWT.NONE);
			lblComponent.setText("Select component:");
			lblComponent.setLayoutData(createLabelLayoutData());

			Combo cmbComponentSelection = new Combo(cmps, SWT.BORDER);
			
			List<FeatureDescriptor> components = new ArrayList<>();

			for (FeatureDescriptor iter : components) {
				cmbComponentSelection.add(iter.getName());
			}
			cmbComponentSelection.select(0);
			cmbComponentSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			cmbComponentSelection.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = cmbComponentSelection.getSelectionIndex();
					//FIXME: remove cast
					featureVersionSelected = (FeatureVersionDescriptor) components.get(index);
				}
			});
			if (components.size() > 0) {
				//FIXME: remove cast
				featureVersionSelected = (FeatureVersionDescriptor) components.get(0);
			}

			Label lblUser = new Label(cmps, SWT.NONE);
			lblUser.setText("Select client:");
			lblUser.setLayoutData(createLabelLayoutData());

			Combo cmbUserSelection = new Combo(cmps, SWT.BORDER);
			List<UserDescriptor> users = new ArrayList<>();
			for (UserDescriptor iter : users) {
				cmbUserSelection.add(iter.getFullName());
			}
			cmbUserSelection.select(0);
			cmbUserSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			cmbUserSelection.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = cmbUserSelection.getSelectionIndex();
					userInfoSelected = users.get(index);

				}
			});
			userInfoSelected = users.get(0);

			Button btnSelectFloating = new Button(cmps, SWT.CHECK);
			btnSelectFloating.setText("Floating");
			btnSelectFloating.setLayoutData(createLabelLayoutData());

			txtSelectFloating = new Text(cmps, SWT.BORDER);
			txtSelectFloating.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			txtSelectFloating.setEnabled(false);
			btnSelectFloating.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					txtSelectFloating.setEnabled(btnSelectFloating.getSelection());
				}
			});

			Label lblDate = new Label(cmps, SWT.NONE);
			lblDate.setText("Experation date:");
			lblDate.setLayoutData(createLabelLayoutData());

			dtime = new DateTime(cmps, SWT.DATE);
			dtime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			return cmps;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			GridData btnData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			Button btnEncrypt = createButton(parent, IDialogConstants.CLIENT_ID, "Create new license", false);
			btnEncrypt.setLayoutData(btnData);
			btnEncrypt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					File licenseFile;
					try {
						int floatingCount = 0;
						if(txtSelectFloating.isEnabled()) {
							floatingCount = Integer.valueOf(txtSelectFloating.getText());
						}
						licenseFile = licenseService.createLicenseFile(productVersionSelected, featureVersionSelected, userInfoSelected,
								dtime.getDay(), dtime.getMonth(), dtime.getYear(),floatingCount);
						if (licenseFile != null && licenseFile.exists()) {
							File parentFile = licenseFile.getParentFile();
							Path licenseOut = Paths.get(parentFile.getAbsolutePath(), "license.cr");
							String username = productVersionSelected.getProduct().getName();
							if (productVersionSelected != null && productVersionSelected.getSecureToken() != null) {
								File privateProductToken = new File(productVersionSelected.getSecureToken());

								try (FileInputStream licenseInput = new FileInputStream(licenseFile);
										FileOutputStream licenseOutput = new FileOutputStream(licenseOut.toFile()); FileInputStream keyStream = new FileInputStream(privateProductToken)) {
									String password = productService.createPassword(productVersionSelected);
									permissionCodec.encodeStream(licenseInput, licenseOutput, keyStream, username, password);
								} catch (Exception exc) {
									MessageDialog.openError(activeShell, "Error", exc.getMessage());
								}

								
								LicenseDescriptor newLicense = licenseService.createLicenseData(productVersionSelected, featureVersionSelected,
										userInfoSelected, licenseOut.toFile(), dtime.getDay(), dtime.getMonth(),
										dtime.getYear(), floatingCount);
								viewer.add(newLicense);
								MessageDialog.openInformation(activeShell, "License", "File encrypted successfully.");
								close();
							} else {
								String msg = String.format("Secure token for product %s is undefined",
										username);
								MessageDialog.openError(activeShell, "License", msg);
							}
						}
					} catch (Exception exc) {
						MessageDialog.openError(activeShell, "License", exc.getMessage());
					}
				}
			});
			Button btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
			btnOk.setLayoutData(btnData);
		}
	}

}
