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
package ru.arsysop.passage.loc.licenses.ui.dialogs;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;
import ru.arsysop.passage.loc.jface.dialogs.FilteredSelectionDialog;
import ru.arsysop.passage.loc.workbench.viewers.DomainRegistryLabelProvider;

public class UserSelectionDialog extends FilteredSelectionDialog {

	public UserSelectionDialog(Shell parent, LicensingImages licensingImages, UserDomainRegistry registry) {
		super(parent, licensingImages, false);
		setTitle("Select User");
		setImage(licensingImages.getImage(LicPackage.eINSTANCE.getUser().getName()));

		ComposedAdapterFactory factory = registry.getComposedAdapterFactory();
		setLabelProvider(new DomainRegistryLabelProvider(licensingImages, factory));

		setInput(registry.getUsers());
	}

}
