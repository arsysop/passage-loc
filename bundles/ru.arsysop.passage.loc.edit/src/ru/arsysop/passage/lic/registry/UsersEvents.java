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

public class UsersEvents extends RegistryEvents {

	/**
	 * Base name of all Users events
	 */
	public static final String USERS_TOPIC_BASE = "ru/arsysop/passage/lic/registry/users"; //$NON-NLS-1$

	/**
	 * Base name of all User Origin events
	 */
	public static final String USER_ORIGIN_TOPIC_BASE = USERS_TOPIC_BASE + TOPIC_SEP + "UserOrigin"; //$NON-NLS-1$

	/**
	 * User Origin <code>create</code> event
	 */
	public static final String USER_ORIGIN_CREATE = USER_ORIGIN_TOPIC_BASE + TOPIC_SEP + CREATE;

	/**
	 * User Origin <code>read</code> event
	 */
	public static final String USER_ORIGIN_READ = USER_ORIGIN_TOPIC_BASE + TOPIC_SEP + READ;

	/**
	 * User Origin <code>update</code> event
	 */
	public static final String USER_ORIGIN_UPDATE = USER_ORIGIN_TOPIC_BASE + TOPIC_SEP + UPDATE;

	/**
	 * User Origin <code>delete</code> event
	 */
	public static final String USER_ORIGIN_DELETE = USER_ORIGIN_TOPIC_BASE + TOPIC_SEP + DELETE;

	/**
	 * Base name of all User events
	 */
	public static final String USER_TOPIC_BASE = USERS_TOPIC_BASE + TOPIC_SEP + "User"; //$NON-NLS-1$

	/**
	 * User <code>create</code> event
	 */
	public static final String USER_CREATE = USER_TOPIC_BASE + TOPIC_SEP + CREATE;

	/**
	 * User <code>read</code> event
	 */
	public static final String USER_READ = USER_TOPIC_BASE + TOPIC_SEP + READ;

	/**
	 * User <code>update</code> event
	 */
	public static final String USER_UPDATE = USER_TOPIC_BASE + TOPIC_SEP + UPDATE;

	/**
	 * User <code>delete</code> event
	 */
	public static final String USER_DELETE = USER_TOPIC_BASE + TOPIC_SEP + DELETE;

}
