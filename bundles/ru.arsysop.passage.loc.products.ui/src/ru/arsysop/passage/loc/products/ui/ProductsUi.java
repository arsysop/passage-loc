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
package ru.arsysop.passage.loc.products.ui;

import org.eclipse.passage.lic.jface.LicensingImages;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.lic.registry.ProductDescriptor;
import org.eclipse.passage.lic.registry.ProductVersionDescriptor;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class ProductsUi {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.products.ui"; //$NON-NLS-1$

	public static final String PERSPECTIVE_MAIN = BUNDLE_SYMBOLIC_NAME + '.' + "perspective.main"; //$NON-NLS-1$

	public static ProductDescriptor selectProductDescriptor(Shell shell, LicensingImages images, ProductDomainRegistry registry, ProductDescriptor initial) {
		String classifier = LicPackage.eINSTANCE.getProduct().getName();
		String title = "Select Product";
		Iterable<ProductDescriptor> input = registry.getProducts();
		Class<ProductDescriptor> clazz = ProductDescriptor.class;
		return LocWokbench.selectClassifier(shell, images, registry, classifier, title, input, initial, clazz);
	}

	public static ProductVersionDescriptor selectProductVersionDescriptor(Shell shell, LicensingImages images,
			ProductDomainRegistry registry, ProductVersionDescriptor initial) {
		String classifier = LicPackage.eINSTANCE.getProductVersion().getName();
		String title = "Select Product Version";
		Iterable<ProductVersionDescriptor> input = registry.getProductVersions();
		Class<ProductVersionDescriptor> clazz = ProductVersionDescriptor.class;
		return LocWokbench.selectClassifier(shell, images, registry, classifier, title, input, initial, clazz);
	}

}
