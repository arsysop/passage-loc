/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
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
package org.eclipse.passage.loc.edit;

import org.eclipse.passage.lic.emf.edit.EditingDomainRegistry;
import org.eclipse.passage.lic.registry.FeatureDescriptor;
import org.eclipse.passage.lic.registry.FeatureRegistry;
import org.eclipse.passage.lic.registry.FeatureSetDescriptor;
import org.eclipse.passage.lic.registry.FeatureVersionDescriptor;

public interface FeatureDomainRegistry extends FeatureRegistry, EditingDomainRegistry {

	void registerFeatureSet(FeatureSetDescriptor featureSet);

	void unregisterFeatureSet(String featureSetId);
	
	void registerFeature(FeatureDescriptor feature);

	void unregisterFeature(String featureId);
	
	void registerFeatureVersion(FeatureDescriptor feature, FeatureVersionDescriptor featureVersion);

	void unregisterFeatureVersion(String featureId, String version);

}
