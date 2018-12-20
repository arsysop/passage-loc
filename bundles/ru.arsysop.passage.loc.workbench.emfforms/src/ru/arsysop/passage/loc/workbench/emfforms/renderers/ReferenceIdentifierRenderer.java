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
package ru.arsysop.passage.loc.workbench.emfforms.renderers;

import javax.inject.Inject;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.loc.workbench.dialogs.ManageTextValuesDialog;
import ru.arsysop.passage.loc.workbench.emfforms.renderers.TextWithButtonRenderer;

public class ReferenceIdentifierRenderer extends TextWithButtonRenderer {

	private static final String EXPRESSION_EMPTY = ""; //$NON-NLS-1$
	private static final String EXPRESSION_SEPARATOR = ";"; //$NON-NLS-1$
	
	@Inject
	public ReferenceIdentifierRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		Control control = super.createSWTControl(parent);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = Display.getDefault().getActiveShell();
				ManageTextValuesDialog dialog = new ManageTextValuesDialog(shell, getCurrentValue(), EXPRESSION_SEPARATOR);
				dialog.create();
				Shell dialogShell = dialog.getShell();
				dialogShell.setText("Condition Expession");
				Image image = getLicensingImages().getImage(LicensingImages.IMG_DEFAULT);
				dialogShell.setImage(image);
				if (dialog.open() == Dialog.OK) {
					text.setText(dialog.getResultValue());
				}
			}
		});

		return control;
	}
	
	@Override
	protected String getUnsetText() {
		return EXPRESSION_EMPTY;
	}

}
