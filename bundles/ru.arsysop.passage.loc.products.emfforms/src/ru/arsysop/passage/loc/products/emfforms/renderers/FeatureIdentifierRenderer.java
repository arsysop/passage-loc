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
package ru.arsysop.passage.loc.products.emfforms.renderers;

import javax.inject.Inject;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.products.ui.ProductsUi;
import ru.arsysop.passage.loc.workbench.emfforms.renderers.TextWithButtonRenderer;

public class FeatureIdentifierRenderer extends TextWithButtonRenderer {

	private static final String IDENTIFIER_EMPTY = ""; //$NON-NLS-1$

	private final FeatureDomainRegistry registry;
	
	@Inject
	public FeatureIdentifierRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);
		registry = viewContext.getService(FeatureDomainRegistry.class);
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		Control control = super.createSWTControl(parent);
		text.setEditable(true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectIdentifier();
			}
		});

		return control;
	}
	
	@Override
	protected String getUnsetText() {
		return IDENTIFIER_EMPTY;
	}

	protected void selectIdentifier() {
		Shell shell = Display.getDefault().getActiveShell();
		FeatureDescriptor initial = null;
		try {
			Object value = getModelValue().getValue();
			if (value instanceof String) {
				String id = (String) value;
				initial = registry.getFeature(id);
			}
		} catch (DatabindingFailedException e) {
			getReportService().report(new DatabindingFailedReport(e));
		}
		FeatureDescriptor descriptor = ProductsUi.selectFeatureDescriptor(shell, getLicensingImages(), registry, initial);
		if (descriptor != null) {
			String identifier = descriptor.getIdentifier();
			if (identifier != null) {
				text.setText(identifier);
			}
		}
	}

}
