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

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public interface DashboardAdvisor {

	void init(IEclipseContext context);

	void createHeaderInfo(Composite parent);

	void createFeatureInfo(Composite parent, FeatureDomainRegistry featureRegistry);

	void updateFeatureInfo(FeatureDomainRegistry featureRegistry);

	void updateProductInfo(ProductDomainRegistry productRegistry);

	void createProductInfo(Composite parent, ProductDomainRegistry productRegistry);

	void createUserInfo(Composite parent, UserDomainRegistry userRegistry);

	void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry);

	void createFooterInfo(Composite parent);

	void dispose(IEclipseContext context);

}
