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
package ru.arsysop.passage.loc.configuration.resources.ui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.registry.FeatureSetDescriptor;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.lic.registry.ProductsEvents;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public class ConfigurationResourcesOverviewPanelPart {

	private List<EditingDomainRegistry> registries = new ArrayList<>();

	private TreeViewer treeView;
	private LicensingImages licensingImages;

	@Inject
	public ConfigurationResourcesOverviewPanelPart(IEclipseContext context) {
		this.registries.add(context.get(FeatureDomainRegistry.class));
		this.registries.add(context.get(ProductDomainRegistry.class));
		this.registries.add(context.get(UserDomainRegistry.class));
		this.registries.add(context.get(LicenseDomainRegistry.class));
		this.licensingImages = context.get(LicensingImages.class);

	}

	@PostConstruct
	public void postConstruct(Composite parent, IEclipseContext context) {

		Composite area = new Composite(parent, SWT.NONE);
		area.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		area.setLayout(new GridLayout(1, false));
		createRegistryTree(area);

	}

	private void createRegistryTree(Composite area) {
		treeView = new TreeViewer(area);
		treeView.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeView.setContentProvider(new ConfigurationResourcesContentProvider());
		treeView.setLabelProvider(new ConfigurationResourcesLabelProvider(licensingImages));
		treeView.setInput(registries);

	}

	@Inject
	@Optional
	public void createFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_CREATE) FeatureSetDescriptor dscriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_DELETE) FeatureSetDescriptor dscriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updateFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_UPDATE) FeatureSetDescriptor dscriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void createProductLine(
			@UIEventTopic(ProductsEvents.PRODUCT_LINE_CREATE) FeatureVersionDescriptor dscriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteProductLine(
			@UIEventTopic(ProductsEvents.PRODUCT_LINE_DELETE) FeatureVersionDescriptor dscriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updatedProductLine(
			@UIEventTopic(ProductsEvents.PRODUCT_LINE_UPDATE) FeatureVersionDescriptor dscriptor) {
		treeView.refresh();
	}
}
