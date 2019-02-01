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
package org.eclipse.passage.loc.edit.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.passage.lic.emf.edit.EditingDomainRegistry;
import org.eclipse.passage.lic.jface.LicensingImages;
import org.eclipse.passage.lic.registry.FeatureSetDescriptor;
import org.eclipse.passage.lic.registry.FeaturesEvents;
import org.eclipse.passage.lic.registry.LicensePackDescriptor;
import org.eclipse.passage.lic.registry.LicensesEvents;
import org.eclipse.passage.lic.registry.ProductLineDescriptor;
import org.eclipse.passage.lic.registry.ProductsEvents;
import org.eclipse.passage.lic.registry.UserOriginDescriptor;
import org.eclipse.passage.lic.registry.UsersEvents;
import org.eclipse.passage.loc.edit.FeatureDomainRegistry;
import org.eclipse.passage.loc.edit.LicenseDomainRegistry;
import org.eclipse.passage.loc.edit.ProductDomainRegistry;
import org.eclipse.passage.loc.edit.UserDomainRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class DomainRegistryExplorerPart {

	private List<EditingDomainRegistry> registries = new ArrayList<>();

	private TreeViewer treeView;
	private LicensingImages licensingImages;
	private DomainRegistryContentProvider contentProvider;

	@Inject
	public DomainRegistryExplorerPart(IEclipseContext context) {
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
		treeView.setAutoExpandLevel(2);
		contentProvider = new DomainRegistryContentProvider();
		treeView.setContentProvider(contentProvider);
		treeView.setLabelProvider(new DomainRegistryLabelProvider(licensingImages));
		treeView.setInput(registries);
	}

	public boolean unregisterStructureSelectedItem() {
		if (treeView != null) {
			Object selection = treeView.getStructuredSelection().getFirstElement();
			if (selection instanceof XMIResource) {
				XMIResource resource = (XMIResource) selection;
				for (EditingDomainRegistry registry : registries) {
					URI uri = resource.getURI();
					List<String> collectSources = StreamSupport.stream(registry.getSources().spliterator(), false)
							.collect(Collectors.toList());
					String path = uri.path();
					if (collectSources.contains(path)) {
						registry.unregisterSource(path);
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getUnregisterResourceName() {
		if (treeView != null) {
			Object selection = treeView.getStructuredSelection().getFirstElement();
			if (selection instanceof XMIResource) {
				return ((XMIResource) selection).getURI().toFileString();
			}
		}
		return "";
	}

	@Inject
	@Optional
	public void createFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_CREATE) FeatureSetDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_DELETE) FeatureSetDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updateFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_UPDATE) FeatureSetDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void createProductLine(@UIEventTopic(ProductsEvents.PRODUCT_LINE_CREATE) ProductLineDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteProductLine(@UIEventTopic(ProductsEvents.PRODUCT_LINE_DELETE) ProductLineDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updatedProductLine(@UIEventTopic(ProductsEvents.PRODUCT_LINE_UPDATE) ProductLineDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void createUserOrigin(@UIEventTopic(UsersEvents.USER_ORIGIN_CREATE) UserOriginDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteUserOrigin(@UIEventTopic(UsersEvents.USER_ORIGIN_DELETE) UserOriginDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updateUserOrigin(@UIEventTopic(UsersEvents.USER_ORIGIN_UPDATE) UserOriginDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void createLicensePack(@UIEventTopic(LicensesEvents.LICENSE_PACK_CREATE) LicensePackDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void deleteLicensePack(@UIEventTopic(LicensesEvents.LICENSE_PACK_DELETE) LicensePackDescriptor descriptor) {
		treeView.refresh();
	}

	@Inject
	@Optional
	public void updateLicensePack(@UIEventTopic(LicensesEvents.LICENSE_PACK_UPDATE) LicensePackDescriptor descriptor) {
		treeView.refresh();
	}

	public void updateView() {
		treeView.refresh();
	}

}
