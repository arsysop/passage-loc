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
package ru.arsysop.passage.loc.features.ui;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.features.ui.viewers.FeatureSearchFilter;
import ru.arsysop.passage.loc.jface.dialogs.FilteredSelectionDialog;
import ru.arsysop.passage.loc.workbench.viewers.DomainRegistryLabelProvider;

public class FeaturesUi {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.features.ui"; //$NON-NLS-1$

	public static final String PERSPECTIVE_MAIN = BUNDLE_SYMBOLIC_NAME + '.' + "perspective.main"; //$NON-NLS-1$

	public static FeatureDescriptor selectFeatureDescriptor(Shell shell, LicensingImages images,
			FeatureDomainRegistry registry, FeatureDescriptor initial) {

		FilteredSelectionDialog dialog = new FilteredSelectionDialog(shell, images, false, new FeatureSearchFilter());
		dialog.setTitle("Select Feature");
		dialog.setImage(images.getImage(LicPackage.eINSTANCE.getFeature().getName()));

		ComposedAdapterFactory factory = registry.getComposedAdapterFactory();
		dialog.setLabelProvider(new DomainRegistryLabelProvider(images, factory));

		dialog.setInput(registry.getFeatures());
		if (initial != null) {
			dialog.setInitial(initial);
		}
		if (dialog.open() == Dialog.OK) {
			Object firstResult = dialog.getFirstResult();
			if (firstResult instanceof FeatureDescriptor) {
				FeatureDescriptor feature = (FeatureDescriptor) firstResult;
				return feature;
			}
		}
		return null;
	}

}
