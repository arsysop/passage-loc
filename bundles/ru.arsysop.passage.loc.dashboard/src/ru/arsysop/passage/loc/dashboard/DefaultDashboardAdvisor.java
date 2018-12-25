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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public class DefaultDashboardAdvisor implements DashboardAdvisor {

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
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText("Features");
	}

	@Override
	public void createProductInfo(Composite parent, ProductDomainRegistry productRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText("Products");
	}

	@Override
	public void createUserInfo(Composite parent, UserDomainRegistry userRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText("Users");
	}

	@Override
	public void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText("Licenses");
	}

	@Override
	public void createFooterInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data summary");
	}

}
