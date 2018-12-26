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
package ru.arsysop.passage.loc.edit;

import ru.arsysop.passage.lic.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.FeatureVersion;
import ru.arsysop.passage.lic.registry.DescriptorRegistry;
import ru.arsysop.passage.lic.registry.FeatureRegistry;

public interface FeatureDomainRegistry extends FeatureRegistry, EditingDomainRegistry, DescriptorRegistry {

	void registerFeatureSet(FeatureSet featureSet);

	void unregisterFeatureSet(String featureSetId);
	
	void registerFeature(Feature feature);

	void unregisterFeature(String featureId);
	
	void registerFeatureVersion(Feature feature, FeatureVersion featureVersion);

	void unregisterFeatureVersion(String featureId, String version);

}
