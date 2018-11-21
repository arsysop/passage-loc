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
package ru.arsysop.passage.licensing.operator.internal;

import java.util.ArrayList;
import java.util.List;

import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.UserDescriptor;

public class UserDataStub {
	static LicPackage packagePassage;

	public static List<UserDescriptor> createUserStub() {
		LicFactory factory = LicPackage.eINSTANCE.getLicFactory();
		List<UserDescriptor> listUsers = new ArrayList<>();

		User userInfoOne = factory.createUser();
		userInfoOne.setEmail("john.doe@acme.com");
		userInfoOne.setFullName("John Doe");
		userInfoOne.setDescription("The famous John Doe!");

		listUsers.add(userInfoOne);

		User userInfoTwo = factory.createUser();
		userInfoTwo.setEmail("acme@acme.com");
		userInfoTwo.setFullName("ACME Company");
		userInfoTwo.setDescription("The world number one company");

		listUsers.add(userInfoTwo);

		return listUsers;
	}
}
