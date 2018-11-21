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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.lic.model.api.FeatureVersion;
import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.api.ProductVersion;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.registry.FeatureRegistry;
import ru.arsysop.passage.licensing.operator.workbench.dialogs.AbstractDialog;
import ru.arsysop.passage.licensing.operator.workbench.utils.PassageUI;

public class FeatureViewer {

	public static final String ID = "ru.arsysop.passage.licensing.operator.workbench.part.component";
	public static final String TITLE = "Registry of the components";
	public static final String TITLE_DIALOG = "Component information";
	private TableViewer tableViewer;
	private TreeViewer treeViewer;
	private TabFolder tabFolder;
	
	@Inject
	FeatureRegistry componentService;

	@PostConstruct
	public void postConstruct(Composite parent) {

		GridData layoutDataBase = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);

		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(layoutDataBase);
		TabItem itemTable = new TabItem(tabFolder, SWT.NONE);
		itemTable.setText("Table view");

		TabItem itemTree = new TabItem(tabFolder, SWT.NONE);
		itemTree.setText("Tree view");

		Composite baseTableCmps = new Composite(tabFolder, SWT.BORDER);
		baseTableCmps.setLayout(new GridLayout());
		baseTableCmps.setLayoutData(layoutDataBase);
		itemTable.setControl(baseTableCmps);
		createTableViewer(baseTableCmps);

		Composite baseTreeCmps = new Composite(tabFolder, SWT.BORDER);
		baseTreeCmps.setLayout(new GridLayout());
		baseTreeCmps.setLayoutData(layoutDataBase);
		itemTree.setControl(baseTreeCmps);
		createTreeViewer(baseTreeCmps);

		createButtonsBar(parent);

	}

	private void createTreeViewer(Composite baseTreeCmps) {
		GridData layoutDataTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		treeViewer = new TreeViewer(baseTreeCmps);
		treeViewer.getTree().setLayoutData(layoutDataTable);
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof Product) {
					return true;
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return new Object[]{inputElement};
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return null;
			}
		});

		TreeViewerColumn columnProductName = new TreeViewerColumn(treeViewer, SWT.NONE);
		columnProductName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}
		});

		TreeViewerColumn columnComponentName = new TreeViewerColumn(treeViewer, SWT.NONE);
		columnComponentName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Feature) {
					Feature component = (Feature) element;
					return component.getName();
				}
				return "";
			}
		});
		TreeViewerColumn columnComponentVersion = new TreeViewerColumn(treeViewer, SWT.NONE);
		columnComponentVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof FeatureVersion) {
					FeatureVersion component = (FeatureVersion) element;
					return component.getVersion();
				}
				return "";
			}
		});

		treeViewer.setInput(componentService.getDescriptors());
	}

	private void createTableViewer(Composite baseTableCmps) {
		GridData layoutDataTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableViewer = new TableViewer(baseTableCmps);
		tableViewer.getTable().setLayoutData(layoutDataTable);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		TableViewerColumn clnId = new TableViewerColumn(tableViewer, SWT.LEAD);
		clnId.getColumn().setText("Identifier");
		clnId.getColumn().setWidth(200);
		clnId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Feature) {
					return ((Feature) element).getIdentifier();
				}
				return "";
			}
		});

		TableViewerColumn clnName = new TableViewerColumn(tableViewer, SWT.LEAD);
		clnName.getColumn().setText("Name");
		clnName.getColumn().setWidth(200);
		clnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Feature) {
					return ((Feature) element).getName();
				}
				return "";
			}
		});

		TableViewerColumn clmVersion = new TableViewerColumn(tableViewer, SWT.LEAD);
		clmVersion.getColumn().setText("Version");
		clmVersion.getColumn().setWidth(100);
		clmVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof FeatureVersion) {
					return ((FeatureVersion) element).getVersion();
				}
				return "";
			}
		});

		TableViewerColumn clmDescription = new TableViewerColumn(tableViewer, SWT.LEAD);
		clmDescription.getColumn().setText("Description");
		clmDescription.getColumn().setWidth(150);
		clmDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Feature) {
					return ((Feature) element).getDescription();
				}
				return "";
			}
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(componentService.getDescriptors());

		Menu contextMenu = new Menu(tableViewer.getTable());
		MenuItem itemShowPublicToken = new MenuItem(contextMenu, SWT.NONE);
		itemShowPublicToken.setText("Show public token");
		itemShowPublicToken.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object firstElement = tableViewer.getStructuredSelection().getFirstElement();
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
				Object firstElement = tableViewer.getStructuredSelection().getFirstElement();
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

		tableViewer.getTable().setMenu(contextMenu);

	}

	private void createButtonsBar(Composite cmps) {
		Composite bottomBar = new Composite(cmps, SWT.BORDER);
		bottomBar.setLayout(new GridLayout(3, false));
		GridData dataBottomBar = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		dataBottomBar.heightHint = 40;
		bottomBar.setLayoutData(dataBottomBar);

		Button btnAddNewComponent = new Button(bottomBar, SWT.PUSH);
		btnAddNewComponent.setText("Add");
		btnAddNewComponent.setLayoutData(PassageUI.getButtonLayoutData());
		btnAddNewComponent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = new Shell(Display.getDefault().getActiveShell());
				ComponentDialog dialog = new ComponentDialog(shell, null);
				if (dialog.open() == Window.OK) {
					Feature created = dialog.getComponentInfo();
					tableViewer.add(created);

					try {
						componentService.insertDescriptors(Collections.singleton(created));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		Button btnEditProduct = new Button(bottomBar, SWT.PUSH);
		btnEditProduct.setText("Edit");
		btnEditProduct.setLayoutData(PassageUI.getButtonLayoutData());
		btnEditProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColumnViewer viewer = null;
				if (tabFolder.getSelectionIndex() == -1) {
					return;
				} else if (tabFolder.getSelectionIndex() == 0) {
					viewer = tableViewer;
				} else if (tabFolder.getSelectionIndex() == 1) {
					viewer = treeViewer;
				}

				if (viewer.getStructuredSelection().getFirstElement() instanceof Feature) {
					Feature component = (Feature) viewer.getStructuredSelection().getFirstElement();
					ComponentDialog dialog = new ComponentDialog(Display.getDefault().getActiveShell(), component);
					if (dialog.open() == Window.OK) {
						viewer.refresh();
					}
				}
			}
		});

	}

	class ComponentDialog extends AbstractDialog {
		private Feature component = null;

		private Text txtId;
		private Text txtName;
		private Text txtDescription;

		public ComponentDialog(Shell shell, Feature component) {
			super(shell);
			this.component = component;
		}

		public Feature getComponentInfo() {
			return component;
		}

		private void updateComponentInfo() {
			if (component == null) {
				component = LicFactory.eINSTANCE.createFeature();
			}
			component.setIdentifier(txtId.getText());
			component.setName(txtName.getText());
			component.setDescription(txtDescription.getText());
		}

		protected ComponentDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite cmps = new Composite(parent, SWT.BORDER);
			cmps.setLayoutData(createAreaDefaultLayoutData());
			cmps.setLayout(new GridLayout(2, false));

			Label lblTitle = new Label(cmps, SWT.CENTER);

			lblTitle.setText(TITLE_DIALOG);
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

			Label lblNotes = new Label(cmps, SWT.NONE);
			lblNotes.setText("Notes");
			lblNotes.setLayoutData(createLabelLayoutData());

			txtDescription = new Text(cmps, SWT.BORDER);
			txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			if (component != null) {
				txtId.setText(component.getIdentifier());
				txtName.setText(component.getName());
				txtDescription.setText(component.getDescription());
			}

			return super.createDialogArea(parent);
		}

		@Override
		protected void buttonPressed(int buttonId) {
			if (buttonId == IDialogConstants.OK_ID) {
				updateComponentInfo();
			}
			super.buttonPressed(buttonId);
		}
	}

}
