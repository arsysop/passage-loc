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

public class RegistryEvents {

	/**
	 * Topic separator character
	 */
	public static final String TOPIC_SEP = "/"; //$NON-NLS-1$

	/**
	 * Wild card character for matching all sub topics
	 */
	public static final String ALL_SUB_TOPICS = "*"; //$NON-NLS-1$


	/**
	 * Segment for events of type <code>create</code> 
	 */
	public static final String CREATE = "create"; //$NON-NLS-1$

	/**
	 * Segment for events of type <code>read</code> 
	 */
	public static final String READ = "read"; //$NON-NLS-1$

	/**
	 * Segment for events of type <code>update</code> 
	 */
	public static final String UPDATE = "update"; //$NON-NLS-1$

	/**
	 * Segment for events of type <code>delete</code> 
	 */
	public static final String DELETE = "delete"; //$NON-NLS-1$

}
