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
package ru.arsysop.passage.loc.workbench.wizards;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emfforms.swt.core.EMFFormsSWTConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.loc.workbench.LocWokbench;

public class CreateFileWizardPage extends WizardPage {

	protected Text txtResourceURI;
	protected Text txtId;
	protected Text txtName;
	private Button resourceURIBrowseFileSystemButton;

	protected ModifyListener validator = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			setPageComplete(validatePage());
		}
	};

	private String extension;
	private InitialValuesProvider valuesProvider;
	private boolean createName;
	private boolean createId;
	private EObject eObject;
	private boolean createForm;

	public CreateFileWizardPage(EObject eObject, String pageName, String extension, InitialValuesProvider valueProvider,
			boolean createId, boolean createName, boolean createForm) {
		super(pageName);

		this.extension = extension;
		this.valuesProvider = valueProvider;
		this.createId = createId;
		this.createName = createName;
		this.eObject = eObject;
		this.createForm = createForm;

	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 3;
			layout.verticalSpacing = 12;
			composite.setLayout(layout);

			GridData data = new GridData();
			data.verticalAlignment = GridData.FILL;
			data.grabExcessVerticalSpace = true;
			data.horizontalAlignment = GridData.FILL;
			composite.setLayoutData(data);
		}

		createFileControls(composite);
		createOtherControls(composite);
		initControls(valuesProvider);

		setPageComplete(validatePage());
		setControl(composite);
	}

	protected void createFileControls(Composite composite) {
		if (createId) {
			Label idFieldLabel = new Label(composite, SWT.LEFT);
			{
				idFieldLabel.setText("&Identifier:");
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = false;
				data.horizontalSpan = 1;
				idFieldLabel.setLayoutData(data);
			}

			txtId = new Text(composite, SWT.BORDER);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				data.horizontalSpan = 2;
				txtId.setLayoutData(data);
			}
		}

		if (createName) {
			Label nameFieldILabel = new Label(composite, SWT.LEFT);
			{
				nameFieldILabel.setText("&Name:");
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = false;
				data.horizontalSpan = 1;
				nameFieldILabel.setLayoutData(data);
			}

			txtName = new Text(composite, SWT.BORDER);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				data.horizontalSpan = 2;
				txtName.setLayoutData(data);
			}
		}
		Label resourceURILabel = new Label(composite, SWT.LEFT);
		{
			resourceURILabel.setText("&File:");
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.grabExcessHorizontalSpace = false;
			data.horizontalSpan = 1;
			resourceURILabel.setLayoutData(data);
		}

		txtResourceURI = new Text(composite, SWT.BORDER);
		{
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.grabExcessHorizontalSpace = true;
			data.horizontalSpan = 1;
			txtResourceURI.setLayoutData(data);
		}
		resourceURIBrowseFileSystemButton = new Button(composite, SWT.PUSH);
		resourceURIBrowseFileSystemButton.setText("Browse ...");
		txtResourceURI.addModifyListener(validator);
		resourceURIBrowseFileSystemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String selected = LocWokbench.selectSavePath(getShell(), extension);
				if (selected != null) {
					txtResourceURI.setText(selected);
				}
			}
		});
	}

	protected void initControls(InitialValuesProvider valuesProvider) {
		String basePath = getBasePath();
		String fileName = valuesProvider.getInitialFileName();
		String resourceURI = basePath + File.separator + fileName + '.' + extension;
		txtResourceURI.setText(resourceURI);
		if (txtId != null) {
			txtId.setText(valuesProvider.getInitialIdentifierValue());
		}
		if (txtName != null) {
			txtName.setText(valuesProvider.getInitialNameValue());
		}
	}

	protected String getBasePath() {
		return System.getProperty("user.home");
	}

	protected boolean validatePage() {
		URI fileURI = getFileURI();
		boolean validationResult = true;
		if (fileURI == null || fileURI.isEmpty()) {
			setMessage("Please specify a file path");
			validationResult = false;
		}
		if (createId) {
			String textId = getIdentifier();
			if (textId == null || textId.isEmpty()) {
				setMessage("Please specify the identifier");
				validationResult = false;
			}
		}
		if (createName) {
			String textName = getName();
			if (textName == null || textName.isEmpty()) {
				setMessage("Please specify the name");
				validationResult = false;
			}
		}
		return validationResult;
	}

	public String getIdentifier() {
		if (txtId == null) {
			return "";
		}
		return txtId.getText();
	}

	public String getName() {
		if (txtName == null) {
			return "";
		}
		return txtName.getText();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			txtResourceURI.setFocus();
		}
	}

	public URI getFileURI() {
		try {
			String text = txtResourceURI.getText();
			if (text != null && !text.endsWith('.' + extension)) {
				text = text + '.' + extension;
			}
			return URI.createFileURI(text);
		} catch (Exception exception) {
			// Ignore
		}
		return null;
	}

	public void selectFileField() {
		txtResourceURI.selectAll();
		txtResourceURI.setFocus();
	}

	protected void createOtherControls(Composite composite) {
		if (createForm) {
			Composite base = new Composite(composite, SWT.NONE);

			GridLayout layout = new GridLayout(1, false);
			base.setLayout(layout);

			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			base.setLayoutData(data);

			final VViewModelProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
			properties.addInheritableProperty(EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_KEY,
					EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_VALUE);
			try {
				ECPSWTViewRenderer.INSTANCE.render(base, eObject, properties);
			} catch (ECPRendererException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
