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

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;

public class ComponentStub {
	static LicPackage packagePassage;

	static List<FeatureDescriptor> createComponentStub() {
		LicFactory factory = LicPackage.eINSTANCE.getLicFactory();

		List<FeatureDescriptor> listOfComponent = new ArrayList<>();

		Feature featureOne = factory.createFeature();
		featureOne.setIdentifier("org.pgcase.xobot.ide.component");
		featureOne.setName("Xobot IDE Component");
//		featureOne.setVersion("1.0.0");
		featureOne.setDescription("Xobot IDE Component");

		listOfComponent.add(featureOne);

		return listOfComponent;

	}

}
