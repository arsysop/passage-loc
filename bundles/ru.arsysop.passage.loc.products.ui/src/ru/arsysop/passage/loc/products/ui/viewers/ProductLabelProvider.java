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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.arsysop.passage.lic.registry.ProductDescriptor;

public class ProductLabelProvider extends LabelProvider {
	
	private static final String PRODUCT_TEXT_PATTERN = "%s (%s)"; //$NON-NLS-1$
	
	private final Image productImage;
	
	public ProductLabelProvider() {
		Image image = null;
		try {
			//FIXME: use EMF Edit
			URL url = new URL("platform:/plugin/ru.arsysop.passage.lic.model.edit/icons/full/obj16/product.png"); //$NON-NLS-1$
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
			image = descriptor.createImage();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.productImage = image;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof ProductDescriptor) {
			ProductDescriptor product = (ProductDescriptor) element;
			String name = product.getName();
			String identifier = product.getIdentifier();
			return String.format(PRODUCT_TEXT_PATTERN, name, identifier);
		}
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof ProductDescriptor) {
			return productImage;
			
		}
		return super.getImage(element);
	}
	
	@Override
	public void dispose() {
		if (productImage != null) {
			productImage.dispose();
		}
		super.dispose();
	}

}
