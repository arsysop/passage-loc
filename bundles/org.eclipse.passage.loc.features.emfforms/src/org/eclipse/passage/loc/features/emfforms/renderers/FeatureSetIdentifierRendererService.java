/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
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
package org.eclipse.passage.loc.features.emfforms.renderers;

import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.swt.core.di.EMFFormsDIRendererService;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.loc.workbench.emfforms.renderers.StructuredFeatureRendererService;
import org.eclipse.passage.loc.workbench.emfforms.renderers.ValidatedTextRenderer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class FeatureSetIdentifierRendererService extends StructuredFeatureRendererService implements EMFFormsDIRendererService<VControl> {

	public FeatureSetIdentifierRendererService() {
		super(ValidatedTextRenderer.class, LicPackage.eINSTANCE.getFeatureSet_Identifier());
	}

	@Reference
	@Override
	public void bindEMFFormsDatabinding(EMFFormsDatabinding databindingService) {
		super.bindEMFFormsDatabinding(databindingService);
	}
	
	@Override
	public void unbindEMFFormsDatabinding(EMFFormsDatabinding databindingService) {
		super.unbindEMFFormsDatabinding(databindingService);
	}
	
	@Reference
	@Override
	public void bindReportService(ReportService reportService) {
		super.bindReportService(reportService);
	}
	
	@Override
	public void unbindReportService(ReportService reportService) {
		super.unbindReportService(reportService);
	}

}
