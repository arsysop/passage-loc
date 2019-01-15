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
package ru.arsysop.passage.loc.edit.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.lic.registry.LicensesRegistry;
import ru.arsysop.passage.lic.registry.ProductsRegistry;
import ru.arsysop.passage.lic.registry.UsersRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class DomainRegistryCreateHandler {

	private static final String PERSPECTIVE_DASHBOARD_ID = "ru.arsysop.passage.loc.dashboard.perspective.main";
	private static final String REGISTRY_RESOURCE_CREATE = "ru.arsysop.passage.loc.edit.ui.commandparameter.domain.resource.create";
	private static final String REGISTRY_RESOURCE_CREATE_FEATURE = REGISTRY_RESOURCE_CREATE + ".feature";
	private static final String REGISTRY_RESOURCE_CREATE_PRODUCT = REGISTRY_RESOURCE_CREATE + ".product";
	private static final String REGISTRY_RESOURCE_CREATE_USER = REGISTRY_RESOURCE_CREATE + ".user";
	private static final String REGISTRY_RESOURCE_CREATE_LICENSE = REGISTRY_RESOURCE_CREATE + ".license";
	private String domain = "";
	private String perspectiveId = "";

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MApplication application,
			EPartService partService, IEclipseContext context,
			@Named(REGISTRY_RESOURCE_CREATE) String domainRegistryId) {

		switch (domainRegistryId) {
		case REGISTRY_RESOURCE_CREATE_FEATURE:
			domain = FeaturesRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_PRODUCT:
			domain = ProductsRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_USER:
			domain = UsersRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_LICENSE:
			domain = LicensesRegistry.DOMAIN_NAME;
			break;
		}

		if (domainRegistryId != null && perspectiveId != null) {
			LocWokbench.createDomainResource(context, domain, PERSPECTIVE_DASHBOARD_ID);
		}
	}
}
