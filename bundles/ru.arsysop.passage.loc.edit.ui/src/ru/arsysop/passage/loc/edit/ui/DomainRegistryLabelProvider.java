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
package ru.arsysop.passage.loc.edit.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureRegistry;
import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.lic.registry.LicenseRegistry;
import ru.arsysop.passage.lic.registry.LicensesRegistry;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.lic.registry.ProductsRegistry;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.lic.registry.UsersRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;

class DomainRegistryLabelProvider extends LabelProvider {

	private LicensingImages images;

	public DomainRegistryLabelProvider(LicensingImages images) {
		this.images = images;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof FeatureRegistry) {
			return "Features";
		}
		if (element instanceof ProductRegistry) {
			return "Products";
		}
		if (element instanceof UserRegistry) {
			return "Users";
		}
		if (element instanceof LicenseRegistry) {
			return "Licenses";
		}
		if (element instanceof Resource) {
			Resource resource = (Resource) element;
			URI uri = resource.getURI();
			return uri.toString();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof FeatureRegistry) {
			return images.getImage(LicPackage.eINSTANCE.getFeatureSet().getName());
		}
		if (element instanceof EditingDomainBasedRegistry) {
			EditingDomainBasedRegistry registry = (EditingDomainBasedRegistry) element;
			return images.getImage(registry.getContentClassifier().getName());
		}
		if (element instanceof Resource) {
			Resource resource = (Resource) element;
			URI uri = resource.getURI();
			return getImageByUri(uri);
		}
		return super.getImage(element);
	}

	private Image getImageByUri(URI uri) {
		if (uri == null) {
			return null;
		}
		String lastSegment = uri.lastSegment();
		if (lastSegment.endsWith(FeaturesRegistry.FILE_EXTENSION_XMI)) {
			return images.getImage(LicPackage.eINSTANCE.getFeatureSet().getName());
		}
		if (lastSegment.endsWith(ProductsRegistry.FILE_EXTENSION_XMI)) {
			return images.getImage(LicPackage.eINSTANCE.getProductLine().getName());
		}
		if (lastSegment.endsWith(UsersRegistry.FILE_EXTENSION_XMI)) {
			return images.getImage(LicPackage.eINSTANCE.getUserOrigin().getName());
		}
		if (lastSegment.endsWith(LicensesRegistry.FILE_EXTENSION_XMI)) {
			return images.getImage(LicPackage.eINSTANCE.getLicensePack().getName());
		}
		return null;
	}

}
