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
package ru.arsysop.passage.lic.registry;

public class FeaturesEvents extends RegistryEvents {

	/**
	 * Base name of all Features events
	 */
	public static final String FEATURES_TOPIC_BASE = "ru/arsysop/passage/lic/registry/features"; //$NON-NLS-1$
	
	/**
	 * Base name of all Feature Set events
	 */
	public static final String FEATURE_SET_TOPIC_BASE = FEATURES_TOPIC_BASE + TOPIC_SEP + "FeatureSet"; //$NON-NLS-1$

	/**
	 * Feature Set <code>create</code> event
	 */
	public static final String FEATURE_SET_CREATE = FEATURE_SET_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Feature Set <code>read</code> event
	 */
	public static final String FEATURE_SET_READ = FEATURE_SET_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Feature Set <code>update</code> event
	 */
	public static final String FEATURE_SET_UPDATE = FEATURE_SET_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Feature Set <code>delete</code> event
	 */
	public static final String FEATURE_SET_DELETE = FEATURE_SET_TOPIC_BASE + TOPIC_SEP + DELETE;
	
	/**
	 * Base name of all Feature events
	 */
	public static final String FEATURE_TOPIC_BASE = FEATURES_TOPIC_BASE + TOPIC_SEP + "Feature"; //$NON-NLS-1$

	/**
	 * Feature <code>create</code> event
	 */
	public static final String FEATURE_CREATE = FEATURE_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Feature <code>read</code> event
	 */
	public static final String FEATURE_READ = FEATURE_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Feature <code>update</code> event
	 */
	public static final String FEATURE_UPDATE = FEATURE_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Feature <code>delete</code> event
	 */
	public static final String FEATURE_DELETE = FEATURE_TOPIC_BASE + TOPIC_SEP + DELETE;
	
	/**
	 * Base name of all Feature Version events
	 */
	public static final String FEATURE_VERSION_TOPIC_BASE = FEATURES_TOPIC_BASE + TOPIC_SEP + "FeatureVersion"; //$NON-NLS-1$

	/**
	 * Feature Version <code>create</code> event
	 */
	public static final String FEATURE_VERSION_CREATE = FEATURE_VERSION_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Feature Version <code>read</code> event
	 */
	public static final String FEATURE_VERSION_READ = FEATURE_VERSION_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Feature Version <code>update</code> event
	 */
	public static final String FEATURE_VERSION_UPDATE = FEATURE_VERSION_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Feature Version <code>delete</code> event
	 */
	public static final String FEATURE_VERSION_DELETE = FEATURE_VERSION_TOPIC_BASE + TOPIC_SEP + DELETE;

}
