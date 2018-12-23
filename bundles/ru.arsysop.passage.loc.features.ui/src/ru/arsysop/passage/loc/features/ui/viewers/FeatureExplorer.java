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
package ru.arsysop.passage.loc.features.ui.viewers;

import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.workbench.viewers.DomainRegistryExplorer;

public class FeatureExplorer extends DomainRegistryExplorer {
	
	@Inject
	public FeatureExplorer(FeatureDomainRegistry registry, ESelectionService selectionService, LicensingImages images) {
		super(registry, selectionService, images);
	}

}
