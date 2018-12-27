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

import java.util.stream.StreamSupport;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public class DefaultDashboardAdvisor implements DashboardAdvisor {

	private LicensingImages licensingImages;

	private Text featureSetText;
	private ControlDecoration featureSetDecoration;

	private Text featureText;
	private ControlDecoration featureDecoration;

	private Text featureVersionText;
	private ControlDecoration featureVersionDecoration;

	private Text productLinesText;
	private ControlDecoration productLinesDecoration;
	private Text productsText;
	private ControlDecoration productsDecoration;
	private Text productVersionsText;
	private ControlDecoration productVersionsDecoration;

	private Text userOriginsText;
	private ControlDecoration userOriginsDecoration;
	private Text userText;
	private ControlDecoration userDecoration;

	private DashboardBlock licensePacks;

	@Override
	public void init(IEclipseContext context) {
		licensingImages = context.get(LicensingImages.class);
	}

	@Override
	public void createHeaderInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data overview");
	}

	@Override
	public void createFeatureInfo(Composite parent, FeatureDomainRegistry featureRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		group.setText("Features");

		featureSetText = createDashBoardTextItem(group, "Feature Sets:", LicPackage.eINSTANCE.getFeatureSet());
		featureSetDecoration = new ControlDecoration(featureSetText, SWT.TOP | SWT.RIGHT);

		featureText = createDashBoardTextItem(group, "Features:", LicPackage.eINSTANCE.getFeature());
		featureDecoration = new ControlDecoration(featureText, SWT.TOP | SWT.RIGHT);

		featureVersionText = createDashBoardTextItem(group, "Feature Versions:",
				LicPackage.eINSTANCE.getFeatureVersion());
		featureVersionDecoration = new ControlDecoration(featureVersionText, SWT.TOP | SWT.RIGHT);

		updateFeatureInfo(featureRegistry);
	}

	@Override
	public void updateFeatureInfo(FeatureDomainRegistry featureRegistry) {
		long featureSetCount = StreamSupport.stream(featureRegistry.getFeatureSets().spliterator(), false).count();
		featureSetText.setText(String.valueOf(featureSetCount));
		DashboardDecorators.decorateFeatureSets(featureSetCount, featureSetDecoration);

		long featureCount = StreamSupport.stream(featureRegistry.getFeatures().spliterator(), false).count();
		featureText.setText(String.valueOf(featureCount));
		DashboardDecorators.decorateFeatures(featureCount, featureDecoration);

		long featureVersionCount = StreamSupport.stream(featureRegistry.getFeatureVersions().spliterator(), false)
				.count();
		featureVersionText.setText(String.valueOf(featureVersionCount));
		DashboardDecorators.decorateFeatureVersions(featureVersionCount, featureVersionDecoration);
	}

	@Override
	public void createProductInfo(Composite parent, ProductDomainRegistry productRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		group.setText("Products");

		productLinesText = createDashBoardTextItem(group, "Product Lines:", LicPackage.eINSTANCE.getProductLine());
		productLinesDecoration = new ControlDecoration(productLinesText, SWT.TOP | SWT.RIGHT);

		productsText = createDashBoardTextItem(group, "Products:", LicPackage.eINSTANCE.getProduct());
		productsDecoration = new ControlDecoration(productsText, SWT.TOP | SWT.RIGHT);

		productVersionsText = createDashBoardTextItem(group, "Product Versions:",
				LicPackage.eINSTANCE.getProductVersion());
		productVersionsDecoration = new ControlDecoration(productVersionsText, SWT.TOP | SWT.RIGHT);
		updateProductInfo(productRegistry);
	}

	@Override
	public void updateProductInfo(ProductDomainRegistry productRegistry) {
		long productLinesCount = StreamSupport.stream(productRegistry.getProductLines().spliterator(), false).count();
		productLinesText.setText(String.valueOf(productLinesCount));
		DashboardDecorators.decorateProductLines(productLinesCount, productLinesDecoration);

		long productsCount = StreamSupport.stream(productRegistry.getProducts().spliterator(), false).count();
		productsText.setText(String.valueOf(productsCount));
		DashboardDecorators.decorateProducts(productsCount, productsDecoration);

		long productVersionsCount = StreamSupport.stream(productRegistry.getProductVersions().spliterator(), false)
				.count();
		productVersionsText.setText(String.valueOf(productVersionsCount));
		DashboardDecorators.decorateProductVersions(productVersionsCount, productVersionsDecoration);

	}

	@Override
	public void createUserInfo(Composite parent, UserDomainRegistry userRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		group.setText("Users");

		userText = createDashBoardTextItem(group, "Users:", LicPackage.eINSTANCE.getUser());
		userDecoration = new ControlDecoration(userText, SWT.TOP | SWT.RIGHT);

		userOriginsText = createDashBoardTextItem(group, "User origins:", LicPackage.eINSTANCE.getUserOrigin());
		userOriginsDecoration = new ControlDecoration(userOriginsText, SWT.TOP | SWT.RIGHT);

		updateUserInfo(userRegistry);
	}

	@Override
	public void updateUserInfo(UserDomainRegistry userRegistry) {
		long userOriginsCount = StreamSupport.stream(userRegistry.getUserOrigins().spliterator(), false).count();
		userOriginsText.setText(String.valueOf(userOriginsCount));
		DashboardDecorators.decorateUserOrigins(userOriginsCount, userOriginsDecoration);

		long userCount = StreamSupport.stream(userRegistry.getUsers().spliterator(), false).count();
		userText.setText(String.valueOf(userCount));
		DashboardDecorators.decorateUsers(userCount, userDecoration);

	}

	@Override
	public void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		group.setText("Licenses");

		DashboardBlock licensePacks = new DashboardBlock();
		String label = "License pack:";
		Image image = licensingImages.getImage(LicPackage.eINSTANCE.getLicensePack().getName());
		licensePacks.createControl(group, label, image);
		String info = "You have %s License Pack(s) defined.\nUse it define the License Grants";
		String warning = "You have no License Packs defined.\nPlease create or load License Pack definitions";
		licensePacks.setInfo(info);
		licensePacks.setWarning(warning);
		;

		updateLicenseInfo(licenseRegistry);
	}

	@Override
	public void updateLicenseInfo(LicenseDomainRegistry licenseRegistry) {
		long licensePacksCount = StreamSupport.stream(licenseRegistry.getLicensePacks().spliterator(), false).count();
		licensePacks.update(licensePacksCount);
	}

	@Override
	public void createFooterInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data summary");
	}

	protected Text createDashBoardTextItem(Group group, String label, EClass object) {
		Image image = licensingImages.getImage(object.getName());
		return DashboardBlock.createTextBlock(group, label, image);
	}

	@Override
	public void dispose(IEclipseContext context) {
		licensingImages = null;
	}

}
