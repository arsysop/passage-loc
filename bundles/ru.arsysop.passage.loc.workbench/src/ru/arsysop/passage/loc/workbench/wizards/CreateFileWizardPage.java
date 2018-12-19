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

import org.eclipse.emf.common.util.URI;
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

	private static final String URI_TEMPLATE = "%s.%s";
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
	private InitialValuesProvider valueProvider;

	public CreateFileWizardPage(String pageName, String extension, InitialValuesProvider valueProvider) {
		super(pageName);
		this.extension = extension;
		this.valueProvider = valueProvider;

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
		initControls();

		setPageComplete(validatePage());
		setControl(composite);
	}

	protected void createFileControls(Composite composite) {
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

	private void initControls() {
		String resourceURI = String.format(URI_TEMPLATE, valueProvider.getInitialFilePath(), extension);
		txtResourceURI.setText(resourceURI);
		txtId.setText(valueProvider.getInitialIdentifierValue());
		txtName.setText(valueProvider.getInitialNameValue());
	}

	protected boolean validatePage() {
		URI fileURI = getFileURI();
		boolean validationResult = true;
		if (fileURI == null || fileURI.isEmpty()) {
			setMessage("Please specify a file path");
			validationResult = false;
		}

		String textId = getIdentifier();
		if (textId == null || textId.isEmpty()) {
			setMessage("Please specify the identifier");
			validationResult = false;
		}

		String textName = getName();
		if (textName == null || textName.isEmpty()) {
			setMessage("Please specify the name");
			validationResult = false;
		}

		return validationResult;
	}

	public String getIdentifier() {
		return txtId.getText();
	}

	public String getName() {
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

}
