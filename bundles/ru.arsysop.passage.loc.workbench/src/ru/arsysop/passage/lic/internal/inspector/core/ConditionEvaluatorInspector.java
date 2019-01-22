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
package ru.arsysop.passage.lic.internal.inspector.core;

import static ru.arsysop.passage.lic.base.LicensingProperties.LICENSING_CONDITION_TYPE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import ru.arsysop.passage.lic.inspector.ConditionInpector;
import ru.arsysop.passage.lic.runtime.ConditionEvaluator;

@Component
public class ConditionEvaluatorInspector implements ConditionInpector {
	
	private final Map<String, ConditionEvaluator> conditionEvaluators = new HashMap<>();
	
	@Reference(cardinality=ReferenceCardinality.MULTIPLE)
	public void bindConditionEvaluator(ConditionEvaluator conditionEvaluator, Map<String, Object> properties) {
		Object conditionType = properties.get(LICENSING_CONDITION_TYPE);
		String type = String.valueOf(conditionType);
		conditionEvaluators.put(type, conditionEvaluator);
	}

	public void unbindConditionEvaluator(ConditionEvaluator conditionEvaluator, Map<String, Object> properties) {
		Object conditionType = properties.get(LICENSING_CONDITION_TYPE);
		String type = String.valueOf(conditionType);
		conditionEvaluators.remove(type);
	}

	@Override
	public Iterable<String> getSupportedTypes() {
		return Collections.unmodifiableSet(conditionEvaluators.keySet());
	}

	@Override
	public String getDefaultType() {
		// TODO configure via property?
		return "hardware";
	}

}
