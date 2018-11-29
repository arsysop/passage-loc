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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.runtime.io.ConditionCodec;
import ru.arsysop.passage.licensing.operator.workbench.dialogs.AbstractDialog;
import ru.arsysop.passage.licensing.operator.workbench.utils.PassageUI;
import ru.arsysop.passage.loc.workbench.viewers.StructuredSelectionListener;

public class ProductViewer {

	public static String ID = "ru.arsysop.passage.licensing.operator.workbench.part.product";
	public static String TITLE = "Registry of the products";

	@Inject
	ProductRegistry productService;

	@Inject
	ConditionCodec conditionCodec;

	@Inject
	ESelectionService selectionService;

	private TableViewer viewer;
	private ISelectionChangedListener selectionChangeListener;

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

		TableViewerColumn clnIndicator = new TableViewerColumn(viewer, SWT.LEAD);
		clnIndicator.getColumn().setText("");
		clnIndicator.getColumn().setWidth(30);
		clnIndicator.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

		});

		TableViewerColumn clnId = new TableViewerColumn(viewer, SWT.LEAD);
		clnId.getColumn().setText("Identifier");
		clnId.getColumn().setWidth(250);
		clnId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Product) {
					return ((Product) element).getIdentifier();
				}
				return "";
			}
		});

		TableViewerColumn clnName = new TableViewerColumn(viewer, SWT.LEAD);
		clnName.getColumn().setText("Name");
		clnName.getColumn().setWidth(120);
		clnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Product) {
					return ((Product) element).getName();
				}
				return "";
			}
		});

		TableViewerColumn clmVersion = new TableViewerColumn(viewer, SWT.LEAD);
		clmVersion.getColumn().setText("Version");
		clmVersion.getColumn().setWidth(100);
		clmVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof ProductVersion) {
					return ((ProductVersion) element).getVersion();
				}
				return "";
			}
		});

		TableViewerColumn clmDescription = new TableViewerColumn(viewer, SWT.LEAD);
		clmDescription.getColumn().setText("Description");
		clmDescription.getColumn().setWidth(150);
		clmDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Product) {
					return ((Product) element).getDescription();
				}
				return "";
			}
		});

		TableViewerColumn clmTokenPub = new TableViewerColumn(viewer, SWT.LEAD);
		clmTokenPub.getColumn().setText("Installation token");
		clmTokenPub.getColumn().setWidth(100);
		clmTokenPub.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof ProductVersion) {
					return ((ProductVersion) element).getInstallationToken();
				}
				return "";
			}
		});

		TableViewerColumn clmTokenPrv = new TableViewerColumn(viewer, SWT.LEAD);
		clmTokenPrv.getColumn().setText("Secure token");
		clmTokenPrv.getColumn().setWidth(100);
		clmTokenPrv.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof ProductVersion) {
					return ((ProductVersion) element).getSecureToken();
				}
				return "";
			}
		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		Composite bottomBar = new Composite(base, SWT.BORDER);
		bottomBar.setLayout(new GridLayout(3, false));
		GridData dataBottomBar = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		dataBottomBar.heightHint = 40;

		Button btnAddNewProduct = new Button(bottomBar, SWT.PUSH);
		btnAddNewProduct.setText("Add");
		btnAddNewProduct.setLayoutData(PassageUI.getButtonLayoutData());
		btnAddNewProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProductVersionDialog dialog = new ProductVersionDialog(Display.getDefault().getActiveShell(), null);
				if (dialog.open() == Window.OK) {
					ProductVersion product = dialog.getProductVersion();
					viewer.add(product);
//					productService.registerProductVersion(product);
				}
			}
		});

		Button btnEditProduct = new Button(bottomBar, SWT.PUSH);
		btnEditProduct.setText("Edit");
		btnEditProduct.setLayoutData(PassageUI.getButtonLayoutData());
		btnEditProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (viewer.getStructuredSelection().getFirstElement() instanceof ProductVersion) {
					ProductVersion product = (ProductVersion) viewer.getStructuredSelection().getFirstElement();
					ProductVersionDialog dialog = new ProductVersionDialog(Display.getDefault().getActiveShell(),
							product);
					if (dialog.open() == Window.OK) {
						viewer.refresh();
					}
				}
			}
		});

		Button btnGenerateKeys = new Button(bottomBar, SWT.PUSH);
		btnGenerateKeys.setText("Register");
		btnGenerateKeys.setLayoutData(PassageUI.getButtonLayoutData());
		Iterable<ProductDescriptor> productDescriptors = productService.getDescriptors();
		btnGenerateKeys.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selectedItem = viewer.getStructuredSelection();
				if (!selectedItem.isEmpty()) {
					if (selectedItem.getFirstElement() instanceof Product) {
						Product selProduct = (Product) selectedItem.getFirstElement();
						java.util.Optional<ProductDescriptor> optionalProduct = StreamSupport
								.stream(productDescriptors.spliterator(), false)
								.filter(p -> p.getName().equals(selProduct.getName())).findFirst();
						if (optionalProduct.isPresent()) {
							// FIXME:AF:extract from UI and rework
							ProductVersion productVersion = (ProductVersion) optionalProduct.get();
							Product product = productVersion.getProduct();
							try {
								String storageKeyFolder = product.getIdentifier() + File.separator
										+ productVersion.getVersion();
								Path path = Paths.get(storageKeyFolder);
								Files.createDirectories(path);
								String publicKeyPath = storageKeyFolder + File.separator + "public.pub";
								String privateKeyPath = storageKeyFolder + File.separator + "private.skr";
								conditionCodec.createKeyPair(publicKeyPath, privateKeyPath, product.getName(),
										productService.createPassword(productVersion), 1024);
								productVersion.setInstallationToken(publicKeyPath);
								productVersion.setSecureToken(privateKeyPath);

								MessageDialog.openInformation(Display.getDefault().getActiveShell(),
										"Product registration", "Product registered succesfully");

								viewer.refresh();
							} catch (Exception exc) {
								// TODO: log
								exc.printStackTrace();
							}
						}
					}
				}
			}
		});

		bottomBar.setLayoutData(dataBottomBar);

		Menu contextMenu = new Menu(viewer.getTable());
		MenuItem itemShowPublicToken = new MenuItem(contextMenu, SWT.NONE);
		itemShowPublicToken.setText("Show public token");
		itemShowPublicToken.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object firstElement = viewer.getStructuredSelection().getFirstElement();
				if (firstElement instanceof ProductVersion) {
					ProductVersion product = (ProductVersion) firstElement;
					String msg = "No public token created";
					if (product.getInstallationToken() != null && !product.getInstallationToken().isEmpty()) {
						File filePublicToken = new File(product.getInstallationToken());
						try {
							List<String> lines = Files.readAllLines(filePublicToken.toPath());
							msg = lines.stream().map(Object::toString).collect(Collectors.joining("\n"));
						} catch (IOException e1) {
						}
					}
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Token public", msg);
				}
			}
		});

		MenuItem itemShowPrivateToken = new MenuItem(contextMenu, SWT.NONE);
		itemShowPrivateToken.setText("Show private token");
		itemShowPrivateToken.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object firstElement = viewer.getStructuredSelection().getFirstElement();
				if (firstElement instanceof ProductVersion) {
					ProductVersion product = (ProductVersion) firstElement;
					String msg = "No private token created";
					if (product.getSecureToken() != null && !product.getSecureToken().isEmpty()) {
						msg = product.getSecureToken();
					}
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Token private", msg);
				}
			}
		});
		selectionChangeListener = new StructuredSelectionListener(selectionService);
		viewer.addSelectionChangedListener(selectionChangeListener);
		viewer.getTable().setMenu(contextMenu);
		viewer.setInput(productDescriptors);
		Iterator<ProductDescriptor> iterator = productDescriptors.iterator();
		if (iterator.hasNext()) {
			ProductDescriptor first = iterator.next();
			viewer.setSelection(new StructuredSelection(first));
		}
	}

	@Focus
	void setFocus() {
		viewer.getControl().setFocus();
	}

	@PreDestroy
	public void dispose() {
		viewer.removeSelectionChangedListener(selectionChangeListener);
	}

	class ProductVersionDialog extends AbstractDialog {
		private ProductVersion productVersion = null;
		private Text txtId;
		private Text txtName;
		private Text txtVersion;
		private Text txtDescription;

		public ProductVersionDialog(Shell shell, ProductVersion product) {
			super(shell);
			this.productVersion = product;
		}

		public ProductVersion getProductVersion() {
			return productVersion;
		}

		private void updateProductInfo() {
			if (productVersion == null) {
				productVersion = LicFactory.eINSTANCE.createProductVersion();
			}
			productVersion.setVersion(txtVersion.getText());
		}

		protected ProductVersionDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite cmps = new Composite(parent, SWT.BORDER);
			cmps.setLayoutData(createAreaDefaultLayoutData());
			cmps.setLayout(new GridLayout(2, false));

			Label lblTitle = new Label(cmps, SWT.CENTER);
			lblTitle.setText("Product information");
			lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

			Label lblIdentifier = new Label(cmps, SWT.NONE);
			lblIdentifier.setText("Identifier");
			lblIdentifier.setLayoutData(createLabelLayoutData());

			txtId = new Text(cmps, SWT.BORDER);
			txtId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			Label lblName = new Label(cmps, SWT.NONE);
			lblName.setText("Name");
			lblName.setLayoutData(createLabelLayoutData());

			txtName = new Text(cmps, SWT.BORDER);
			txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			Label lblVersion = new Label(cmps, SWT.NONE);
			lblVersion.setText("Version");
			lblVersion.setLayoutData(createLabelLayoutData());

			txtVersion = new Text(cmps, SWT.BORDER);
			txtVersion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			Label lblNotes = new Label(cmps, SWT.NONE);
			lblNotes.setText("Notes");
			lblNotes.setLayoutData(createLabelLayoutData());

			txtDescription = new Text(cmps, SWT.BORDER);
			txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			if (productVersion != null) {
				txtId.setText(productVersion.getProduct().getIdentifier());
				txtName.setText(productVersion.getProduct().getName());
				txtVersion.setText(productVersion.getVersion());
				txtDescription.setText(productVersion.getProduct().getDescription());
			}

			return super.createDialogArea(parent);
		}

		@Override
		protected void buttonPressed(int buttonId) {
			if (buttonId == IDialogConstants.OK_ID) {
				updateProductInfo();
			}
			super.buttonPressed(buttonId);
		}

	}

}
