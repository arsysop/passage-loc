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
package ru.arsysop.passage.loc.products.ui.handlers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.loc.products.core.LocProductsCore;

public class CreateProductHandler {
	
	private final IEventBroker eventBroker;
	
	@Inject
	public CreateProductHandler(IEventBroker broker) {
		this.eventBroker = broker;
	}
	
	@Execute
	public void execute(ProductRegistry productRegistry) {
		Product created = LicFactory.eINSTANCE.createProduct();
		Iterable<ProductDescriptor> descriptors = productRegistry.getDescriptors();
		long size = StreamSupport.stream(descriptors.spliterator(), false).count();
		long postfix = size + 1;
		created.setIdentifier("identifier" + postfix);
		created.setName("Name" + postfix);
//		product.setVersion("1.0.0");
		created.setDescription("");
		try {
			Set<ProductDescriptor> singleton = Collections.singleton(created);
			productRegistry.insertDescriptors(singleton);
			eventBroker.post(LocProductsCore.TOPIC_PRODUCTS_INSERTED, singleton);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
