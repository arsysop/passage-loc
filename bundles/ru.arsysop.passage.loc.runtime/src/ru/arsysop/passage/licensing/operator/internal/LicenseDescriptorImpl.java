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

import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.licensing.operator.LicenseDescriptor;

public class LicenseDescriptorImpl implements LicenseDescriptor {
	
	private ProductVersionDescriptor productVersionDescriptor;
	private FeatureVersionDescriptor featureVersionDescriptor;
	private int floatingCount;

	@Override
	public ProductVersionDescriptor getProductVersion() {
		return productVersionDescriptor;
	}

	@Override
	public FeatureVersionDescriptor getFeatureVersion() {
		return featureVersionDescriptor;
	}

	@Override
	public int getFloatingCount() {
		return floatingCount;
	}

	public void setProductVersion(ProductVersionDescriptor productVersion) {
		this.productVersionDescriptor = productVersion;
	}

	public void setFeatureVersion(FeatureVersionDescriptor featureVersion) {
		this.featureVersionDescriptor = featureVersion;
	}

	public void setFloatingCount(int floatingCount) {
		this.floatingCount = floatingCount;
	}

}
