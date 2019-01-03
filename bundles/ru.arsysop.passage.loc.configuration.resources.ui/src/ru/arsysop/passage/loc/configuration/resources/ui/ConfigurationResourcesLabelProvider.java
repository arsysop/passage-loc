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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;

class ConfigurationResourcesLabelProvider extends LabelProvider {

	private static final String CONFIGURATION_RESOURCE = "Configuration Resources of %s:"; //$NON-NLS-1$
	private static final String RESOURCE = "Resources %s ."; //$NON-NLS-1$
	private LicensingImages images;

	public ConfigurationResourcesLabelProvider(LicensingImages images) {
		this.images = images;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof EditingDomainRegistry) {
			EditingDomainRegistry editingDomainRegistry = (EditingDomainRegistry) element;
			return String.format(CONFIGURATION_RESOURCE, editingDomainRegistry.getContentClassifier().getName());
		}
		return String.format(RESOURCE, super.getText(element));
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof EditingDomainBasedRegistry) {
			EditingDomainBasedRegistry registry = (EditingDomainBasedRegistry) element;
			return images.getImage(registry.toString());
		}
		return super.getImage(element);
	}

}
