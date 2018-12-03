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

	protected Text fileField;

	protected ModifyListener validator = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			setPageComplete(validatePage());
		}
	};

	private String extension;

	public CreateFileWizardPage(String pageName, String extension) {
		super(pageName);
		this.extension = extension;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
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

		setPageComplete(validatePage());
		setControl(composite);
	}

	protected void createFileControls(Composite composite) {
		Label resourceURILabel = new Label(composite, SWT.LEFT);
		{
			resourceURILabel.setText("&File");

			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			resourceURILabel.setLayoutData(data);
		}

		Composite fileComposite = new Composite(composite, SWT.NONE);
		{
			GridData data = new GridData();
			data.horizontalAlignment = GridData.END;
			fileComposite.setLayoutData(data);

			GridLayout layout = new GridLayout();
			data.horizontalAlignment = GridData.FILL;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.numColumns = 2;
			fileComposite.setLayout(layout);
		}

		fileField = new Text(fileComposite, SWT.BORDER);
		{
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.grabExcessHorizontalSpace = true;
			data.horizontalSpan = 1;
			fileField.setLayoutData(data);
		}

		fileField.addModifyListener(validator);

		Button resourceURIBrowseFileSystemButton = new Button(fileComposite, SWT.PUSH);
		resourceURIBrowseFileSystemButton.setText("Browse ...");

		resourceURIBrowseFileSystemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String selected = LocWokbench.selectSavePath(getShell(), extension);
				if (selected != null) {
					fileField.setText(selected);
				}
			}
		});
	}

	protected void createOtherControls(Composite composite) {
	}

	protected boolean validatePage() {
		URI fileURI = getFileURI();
		if (fileURI == null || fileURI.isEmpty()) {
			setMessage("Please specify a file path");
			return false;
		}
		setMessage(null);
		setErrorMessage(null);
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			fileField.setFocus();
		}
	}

	public URI getFileURI() {
		try {
			String text = fileField.getText();
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
		fileField.selectAll();
		fileField.setFocus();
	}

}
