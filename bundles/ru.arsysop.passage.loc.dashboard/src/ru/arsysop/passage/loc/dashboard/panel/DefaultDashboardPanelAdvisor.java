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
package ru.arsysop.passage.loc.dashboard.panel;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;
import ru.arsysop.passage.loc.features.ui.FeaturesUi;
import ru.arsysop.passage.loc.products.ui.ProductsUi;

public class DefaultDashboardPanelAdvisor implements DashboardPanelAdvisor {

	private LicensingImages licensingImages;

	private DashboardPanelBlock featureSets;
	private DashboardPanelBlock features;
	private DashboardPanelBlock featureVersions;

	private DashboardPanelBlock productLines;
	private DashboardPanelBlock products;
	private DashboardPanelBlock productVersions;
	private DashboardPanelBlock productVersionFeatures;

	private DashboardPanelBlock userOrigins;
	private DashboardPanelBlock users;

	private DashboardPanelBlock licensePacks;

	@Override
	public void init(IEclipseContext context) {
		licensingImages = context.get(LicensingImages.class);
	}

	@Override
	public void createHeaderInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(
				GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.TOP).indent(0, 10).grab(true, false).create());
		label.setFont(JFaceResources.getBannerFont());
		label.setText("Licensing data overview");
	}

	@Override
	public void createFeatureInfo(Composite parent, FeatureDomainRegistry featureRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());
		group.setText("Features");

		createLinks(group);

		featureSets = createFeatureSetBlock(group);
		features = createFeatureBlock(group, featureRegistry);
		featureVersions = createFeatureVersionBlock(group);

		updateFeatureInfo(featureRegistry);
	}

	protected DashboardPanelBlock createFeatureSetBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Feature Sets:";
		Image image = getImage(LicPackage.eINSTANCE.getFeatureSet());
		block.createControl(parent, label, image);
		String info = "You have %s Feature Set(s) defined.\nUse it define the Features";
		String warning = "You have no Feature Sets defined.\nPlease create or load Feature Set definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardPanelBlock createFeatureBlock(Composite parent, FeatureDomainRegistry registry) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Features:";
		Image image = getImage(LicPackage.eINSTANCE.getFeature());
		block.createControl(parent, label, image);
		String info = "You have %s Feature(s) defined.\nUse it define the Feature Version(s)";
		String warning = "You have no Features defined.\nPlease create it for the Feature Set(s)";
		block.setInfo(info);
		block.setWarning(warning);
		block.configureEdit("Select Feature to edit", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell activeShell = e.widget.getDisplay().getActiveShell();
				FeatureDescriptor descriptor = FeaturesUi.selectFeatureDescriptor(activeShell, licensingImages, registry, null);
				if (descriptor != null) {
					//switch perspective
					//select product
				}
			}
		});
		return block;
	}

	protected DashboardPanelBlock createFeatureVersionBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Feature Versions:";
		Image image = getImage(LicPackage.eINSTANCE.getFeatureVersion());
		block.createControl(parent, label, image);
		String info = "You have %s Feature Version(s) defined.\nUse it define the Product Version(s)";
		String warning = "You have no Feature Versions defined.\nPlease create it for the Feature(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateFeatureInfo(FeatureDomainRegistry featureRegistry) {
		featureSets.update(featureRegistry.getFeatureSets());
		features.update(featureRegistry.getFeatures());
		featureVersions.update(featureRegistry.getFeatureVersions());
	}

	@Override
	public void createProductInfo(Composite parent, ProductDomainRegistry productRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());
		group.setText("Products");

		createLinks(group);

		productLines = createProductLineBlock(group);
		products = createProductBlock(group, productRegistry);
		productVersions = createProductVersionBlock(group);
		productVersionFeatures = createProductVersionFeatureBlock(group);

		updateProductInfo(productRegistry);
	}

	protected DashboardPanelBlock createProductLineBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Product Lines:";
		Image image = getImage(LicPackage.eINSTANCE.getProductLine());
		block.createControl(parent, label, image);
		String info = "You have %s Product Line(s) defined.\nUse it define the Products";
		String warning = "You have no Product Lines defined.\nPlease create or load Product Line definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardPanelBlock createProductBlock(Composite parent, ProductDomainRegistry registry) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Products:";
		Image image = getImage(LicPackage.eINSTANCE.getProduct());
		block.createControl(parent, label, image);
		String info = "You have %s Product(s) defined.\nUse it define the Product Versions";
		String warning = "You have no Products defined.\nPlease create it for the Product Line(s)";
		block.setInfo(info);
		block.setWarning(warning);
		block.configureEdit("Select Product to edit", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell activeShell = e.widget.getDisplay().getActiveShell();
				ProductDescriptor descriptor = ProductsUi.selectProductDescriptor(activeShell, licensingImages, registry, null);
				if (descriptor != null) {
					//switch perspective
					//select product
				}
			}
		});
		return block;
	}

	protected DashboardPanelBlock createProductVersionBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Product Versions:";
		Image image = getImage(LicPackage.eINSTANCE.getProductVersion());
		block.createControl(parent, label, image);
		String info = "You have %s Product Version (s) defined.\nUse it define the Product Version Features";
		String warning = "You have no Product Versions defined.\nPlease create it for the Product(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardPanelBlock createProductVersionFeatureBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Product Features:";
		Image image = getImage(LicPackage.eINSTANCE.getProductVersionFeature());
		block.createControl(parent, label, image);
		String info = "You have %s Product Version Feature(s) defined.\nUse it define License Grants";
		String warning = "You have no Product Version Features defined.\nPlease create it for the Product Verion(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateProductInfo(ProductDomainRegistry productRegistry) {
		productLines.update(productRegistry.getProductLines());
		products.update(productRegistry.getProducts());
		productVersions.update(productRegistry.getProductVersions());
		productVersionFeatures.update(productRegistry.getProductVersionFeatures());
	}

	@Override
	public void createUserInfo(Composite parent, UserDomainRegistry userRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());
		group.setText("Users");

		createLinks(group);

		userOrigins = createUserOriginBlock(group);
		users = createUserBlock(group);

		updateUserInfo(userRegistry);
	}

	protected DashboardPanelBlock createUserOriginBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "User Origins:";
		Image image = getImage(LicPackage.eINSTANCE.getUserOrigin());
		block.createControl(parent, label, image);
		String info = "You have %s User Origin(s) defined.\nUse it define the Users";
		String warning = "You have no User Origins defined.\nPlease create or load User Origin definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardPanelBlock createUserBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "Users:";
		Image image = getImage(LicPackage.eINSTANCE.getUser());
		block.createControl(parent, label, image);
		String info = "You have %s User(s) defined.\nUse it define the License Packs";
		String warning = "You have no Users defined.\nPlease create it for the User Origin(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateUserInfo(UserDomainRegistry userRegistry) {
		userOrigins.update(userRegistry.getUserOrigins());
		users.update(userRegistry.getUsers());
	}

	@Override
	public void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());
		group.setText("Licenses");

		createLinks(group);

		licensePacks = createLicensePackBlock(group);
		updateLicenseInfo(licenseRegistry);
	}

	protected DashboardPanelBlock createLicensePackBlock(Composite parent) {
		DashboardPanelBlock block = new DashboardPanelBlock();
		String label = "License Packs:";
		Image image = getImage(LicPackage.eINSTANCE.getLicensePack());
		block.createControl(parent, label, image);
		String info = "You have %s License Pack(s) defined.\nUse it define the License Grants";
		String warning = "You have no License Packs defined.\nPlease create or load License Pack definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateLicenseInfo(LicenseDomainRegistry licenseRegistry) {
		licensePacks.update(licenseRegistry.getLicensePacks());
	}

	@Override
	public void createFooterInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(
				GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.TOP).indent(0, 10).grab(true, false).create());
		label.setText("Licensing data summary");
	}

	protected void createLinks(Group group) {
		Link create = new Link(group, SWT.NONE);
		create.setText("<a>Create</a>");
		create.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).span(2, 1).create());
		Link open = new Link(group, SWT.NONE);
		open.setText("<a>Load</a>");
		open.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).span(2, 1).create());
	}

	protected Image getImage(EClass eClass) {
		return licensingImages.getImage(eClass.getName());
	}

	@Override
	public void dispose(IEclipseContext context) {
		licensingImages = null;
	}

}
