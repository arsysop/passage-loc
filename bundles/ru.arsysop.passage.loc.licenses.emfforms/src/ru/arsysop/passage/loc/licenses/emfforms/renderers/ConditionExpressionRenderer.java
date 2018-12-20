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
package ru.arsysop.passage.loc.licenses.emfforms.renderers;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.base.ui.LicensingColors;
import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.api.LicenseGrant;
import ru.arsysop.passage.loc.jface.LocImages;
import ru.arsysop.passage.loc.workbench.dialogs.ManageTextValuesDialog;

public class ConditionExpressionRenderer extends SimpleControlSWTControlSWTRenderer {

	private static final String EXPRESSION_EMPTY = ""; //$NON-NLS-1$
	private static final String EXPRESSION_SEPARATOR = ";"; //$NON-NLS-1$
	
	private final LicensingImages licensingImages;
	private final LicensingColors licensingColors;
	private final LocImages locImages;

	private Composite base;
	private Text txtConditionExpression;

	@Inject
	public ConditionExpressionRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);
		this.licensingImages = viewContext.getService(LicensingImages.class);
		this.licensingColors = viewContext.getService(LicensingColors.class);
		this.locImages = viewContext.getService(LocImages.class);
	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		if (control instanceof Text) {
			final Binding binding = getDataBindingContext().bindValue(
					WidgetProperties.text(SWT.Modify).observe(txtConditionExpression), getModelValue(),
					withPreSetValidation(new UpdateValueStrategy()), null);
			return new Binding[] { binding };
		}

		return new Binding[] {};
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		base = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		base.setLayout(layout);
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		GridData txtGD = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		txtConditionExpression = new Text(base, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		txtConditionExpression.setLayoutData(txtGD);
		txtConditionExpression.setText(getCurrentValue());
		txtConditionExpression.setEditable(false);

		GridData btnGD = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		btnGD.heightHint = 28;
		btnGD.widthHint = 60;
		Button btnConditionExpressionEdit = new Button(base, SWT.PUSH);
		btnConditionExpressionEdit.setText("Edit...");
		btnConditionExpressionEdit.setImage(locImages.getImage(LocImages.IMG_TOOL_EDIT));
		btnConditionExpressionEdit.setLayoutData(btnGD);
		btnConditionExpressionEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = Display.getDefault().getActiveShell();
				ManageTextValuesDialog dialog = new ManageTextValuesDialog(shell, getCurrentValue(), EXPRESSION_SEPARATOR);
				dialog.create();
				Shell dialogShell = dialog.getShell();
				dialogShell.setText("Condition Expession");
				dialogShell.setImage(licensingImages.getImage(LicensingImages.IMG_DEFAULT));
				if (dialog.open() == Dialog.OK) {
					txtConditionExpression.setText(dialog.getResultValue());
				}
			}
		});

		return txtConditionExpression;
	}

	@Override
	protected void dispose() {
		super.dispose();
		if (base != null && !base.isDisposed()) {
			for (Control control : base.getChildren()) {
				if (control != null) {
					control.dispose();
				}
			}
		}
	}

	protected String getCurrentValue() {
		String conditionType = null;
		EObject domainModel = getViewModelContext().getDomainModel();
		if (domainModel instanceof LicenseGrant) {
			conditionType = ((LicenseGrant) domainModel).getConditionExpression();
		}
		if (conditionType == null) {
			conditionType = EXPRESSION_EMPTY;
		}
		return conditionType;
	}

	@Override
	protected String getUnsetText() {
		return EXPRESSION_EMPTY;
	}

	@Override
	protected void setValidationColor(Control control, Color validationColor) {
		if (control instanceof Text) {
			Text textControl = ((Text) control);
			if (textControl.getText().isEmpty()) {
				control.setBackground(licensingColors.getColor(LicensingColors.COLOR_VALIDATION_ERROR));
			} else {
				control.setBackground(licensingColors.getColor(LicensingColors.COLOR_VALIDATION_OK));
			}
		}
	}

}
