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
package ru.arsysop.passage.loc.dashboard;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public class DashboardPart {
	
	private final FeatureDomainRegistry featureRegistry;
	private final ProductDomainRegistry productRegistry;
	private final UserDomainRegistry userRegistry;
	private final LicenseDomainRegistry licenseRegistry;
	private final DashboardAdvisor dashboardAdvisor;
	
	@Inject
	public DashboardPart(IEclipseContext context) {
		this.featureRegistry = context.get(FeatureDomainRegistry.class);
		this.productRegistry = context.get(ProductDomainRegistry.class);
		this.userRegistry = context.get(UserDomainRegistry.class);
		this.licenseRegistry = context.get(LicenseDomainRegistry.class);
		DashboardAdvisor advisor = context.get(DashboardAdvisor.class);
		if (advisor == null) {
			advisor = new DefaultDashboardAdvisor();
		}
		this.dashboardAdvisor = advisor;
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, IEclipseContext context) {
		dashboardAdvisor.init(context);
		Composite area = new Composite(parent, SWT.NONE);
		area.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		area.setLayout(GridLayoutFactory.swtDefaults().create());
		createHeaderInfo(area);
		createFeatureInfo(area);
		createProductInfo(area);
		createUserInfo(area);
		createLicenseInfo(area);
		createFooterInfo(area);
	}
	
	protected void createHeaderInfo(Composite parent) {
		dashboardAdvisor.createHeaderInfo(parent);
	}

	protected void createFeatureInfo(Composite parent) {
		dashboardAdvisor.createFeatureInfo(parent, featureRegistry);
	}

	protected void createProductInfo(Composite parent) {
		dashboardAdvisor.createProductInfo(parent, productRegistry);
	}

	protected void createUserInfo(Composite parent) {
		dashboardAdvisor.createUserInfo(parent, userRegistry);
	}

	protected void createLicenseInfo(Composite parent) {
		dashboardAdvisor.createLicenseInfo(parent, licenseRegistry);
	}

	protected void createFooterInfo(Composite parent) {
		dashboardAdvisor.createFooterInfo(parent);
	}

	@Inject
	@Optional
	public void readFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_READ) FeatureSet input) {
		dashboardAdvisor.updateFeatureInfo(featureRegistry);
	}

	@PreDestroy
	public void preDestroy(IEclipseContext context) {
		dashboardAdvisor.dispose(context);
	}
	
	@Focus
	public void onFocus() {
		
	}
	
	@Persist
	public void save() {
		
	}
	
}