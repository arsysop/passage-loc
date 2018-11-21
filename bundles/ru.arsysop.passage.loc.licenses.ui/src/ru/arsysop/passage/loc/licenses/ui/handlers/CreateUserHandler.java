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
package ru.arsysop.passage.loc.licenses.ui.handlers;

import java.util.Collections;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.loc.licenses.ui.LicensesUi;

public class CreateUserHandler {
	
	private final IEventBroker eventBroker;
	
	@Inject
	public CreateUserHandler(IEventBroker broker) {
		this.eventBroker = broker;
	}
	
	@Execute
	public void execute(UserRegistry registry) {
		User created = LicFactory.eINSTANCE.createUser();
		Iterable<UserDescriptor> descriptors = registry.getDescriptors();
		long size = StreamSupport.stream(descriptors.spliterator(), false).count();
		long postfix = size + 1;
		created.setEmail("identifier" + postfix + "@acme.com");
		created.setFullName("Name" + postfix + " " + "Family" + postfix);
		created.setDescription("");
		try {
			registry.insertDescriptors(Collections.singleton(created));
			eventBroker.post(LicensesUi.TOPIC_USERS_CREATE, created);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
