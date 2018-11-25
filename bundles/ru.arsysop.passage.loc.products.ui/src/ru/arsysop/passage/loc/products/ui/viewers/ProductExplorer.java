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
package ru.arsysop.passage.loc.products.ui.viewers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import ru.arsysop.passage.lic.registry.ProductDescriptor;
import ru.arsysop.passage.lic.registry.ProductRegistry;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.products.core.LocProductsCore;
import ru.arsysop.passage.loc.workbench.viewers.DescriptorRegistryExplorer;

public class ProductExplorer extends DescriptorRegistryExplorer<ProductDescriptor> {
	
	@Inject
	public ProductExplorer(ProductRegistry registry, ESelectionService selectionService, ComposedAdapterFactoryProvider factoryProvider) {
		super(registry, selectionService);
	}
	

	@Inject
	@Optional
	private void subscribeProductsInserted(@UIEventTopic(LocProductsCore.TOPIC_PRODUCTS_INSERTED) Iterable<ProductDescriptor> products) {
		resetInput();
	}

}
