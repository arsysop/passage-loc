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
package ru.arsysop.passage.lic.base;

public class LicensingEvents {

	/**
	 * Topic separator character
	 */
	public static final String TOPIC_SEP = "/"; //$NON-NLS-1$

	/**
	 * Wild card character for matching all sub topics
	 */
	public static final String ALL_SUB_TOPICS = "*"; //$NON-NLS-1$

	/**
	 * Base name of all Licensing events
	 */
	public static final String LicensingTopicBase = "ru/arsysop/passage/lic/base"; //$NON-NLS-1$
	
	public class LicensingLifeCycle {
		
		/**
		 * Base name for all Licensing life cycle events
		 */
		public static final String TOPIC = LicensingTopicBase + "/LifeCycle"; //$NON-NLS-1$

		/**
		 * Sent when application startup is complete
		 */
		public static final String RESTRICTION_EXECUTED = TOPIC + TOPIC_SEP + "restrictionExecuted"; //$NON-NLS-1$

	}

}
