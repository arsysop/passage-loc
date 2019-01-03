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
package ru.arsysop.passage.loc.users.ui;

import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class UsersUi {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.users.ui"; //$NON-NLS-1$

	public static final String PERSPECTIVE_MAIN = BUNDLE_SYMBOLIC_NAME + '.' + "perspective.main"; //$NON-NLS-1$

	public static UserDescriptor selectUserDescriptor(Shell shell, LicensingImages images, UserDomainRegistry registry,
			UserDescriptor initial) {
		String classifier = LicPackage.eINSTANCE.getUser().getName();
		String title = "Select User";
		Iterable<UserDescriptor> input = registry.getUsers();
		Class<UserDescriptor> clazz = UserDescriptor.class;
		return LocWokbench.selectClassifier(shell, images, registry, classifier, title, input, initial, clazz);
	}

}
