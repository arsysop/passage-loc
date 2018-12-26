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
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
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

	private Text userText;
	private ControlDecoration userDecoration;

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

		Label featureSetImage = new Label(group, SWT.NONE);
		featureSetImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getFeatureSet().getName()));
		Label featureSetLabel = new Label(group, SWT.NONE);
		featureSetLabel.setText("Feature Sets:");
		featureSetText = new Text(group, SWT.READ_ONLY);
		featureSetDecoration = new ControlDecoration(featureSetText, SWT.TOP | SWT.RIGHT);

		Label featureImage = new Label(group, SWT.NONE);
		featureImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getFeature().getName()));
		Label featureLabel = new Label(group, SWT.NONE);
		featureLabel.setText("Features:");
		featureText = new Text(group, SWT.READ_ONLY);
		featureDecoration = new ControlDecoration(featureText, SWT.TOP | SWT.RIGHT);

		Label featureVersionImage = new Label(group, SWT.NONE);
		featureVersionImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getFeatureVersion().getName()));
		Label featureVersionLabel = new Label(group, SWT.NONE);
		featureVersionLabel.setText("Feature Versions:");
		featureVersionText = new Text(group, SWT.READ_ONLY);
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

		Label productLinesImage = new Label(group, SWT.NONE);
		productLinesImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getProductLine().getName()));
		Label productLinesLabel = new Label(group, SWT.NONE);
		productLinesLabel.setText("Product Lines:");
		productLinesText = new Text(group, SWT.READ_ONLY);
		productLinesDecoration = new ControlDecoration(productLinesText, SWT.TOP | SWT.RIGHT);

		Label productsImage = new Label(group, SWT.NONE);
		productsImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getProduct().getName()));
		Label productLabel = new Label(group, SWT.NONE);
		productLabel.setText("Products:");
		productsText = new Text(group, SWT.READ_ONLY);
		productsDecoration = new ControlDecoration(productsText, SWT.TOP | SWT.RIGHT);

		Label productVersionsImage = new Label(group, SWT.NONE);
		productVersionsImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getProductVersion().getName()));
		Label productVersionsLabel = new Label(group, SWT.NONE);
		productVersionsLabel.setText("Product Versions:");
		productVersionsText = new Text(group, SWT.READ_ONLY);
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

		Label userImage = new Label(group, SWT.NONE);
		userImage.setImage(licensingImages.getImage(LicPackage.eINSTANCE.getUser().getName()));
		Label userLabel = new Label(group, SWT.NONE);
		userLabel.setText("Users:");
		userText = new Text(group, SWT.READ_ONLY);
		userDecoration = new ControlDecoration(featureSetText, SWT.TOP | SWT.RIGHT);

		updateUserInfo(userRegistry);
	}

	@Override
	public void updateUserInfo(UserDomainRegistry userRegistry) {
		long usersCount = StreamSupport.stream(userRegistry.getUsers().spliterator(), false).count();
		userText.setText(String.valueOf(usersCount));
		DashboardDecorators.decorateFeatureSets(usersCount, userDecoration);
	
	}

	@Override
	public void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText("Licenses");
	}

	@Override
	public void updateLicenseInfo(LicenseDomainRegistry licenseRegistry) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void createFooterInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data summary");
	}

	@Override
	public void dispose(IEclipseContext context) {
		licensingImages = null;
	}

}
