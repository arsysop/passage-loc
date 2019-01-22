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

public class LicensesEvents extends RegistryEvents {

	/**
	 * Base name of all Licenses events
	 */
	public static final String LICENSES_TOPIC_BASE = "ru/arsysop/passage/lic/registry/licenses"; //$NON-NLS-1$

	/**
	 * Base name of all License Pack events
	 */
	public static final String LICENSE_PACK_TOPIC_BASE = LICENSES_TOPIC_BASE + TOPIC_SEP + "LicensePack"; //$NON-NLS-1$

	/**
	 * License Pack <code>create</code> event
	 */
	public static final String LICENSE_PACK_CREATE = LICENSE_PACK_TOPIC_BASE + TOPIC_SEP + CREATE;

	/**
	 * License Pack <code>read</code> event
	 */
	public static final String LICENSE_PACK_READ = LICENSE_PACK_TOPIC_BASE + TOPIC_SEP + READ;

	/**
	 * License Pack <code>update</code> event
	 */
	public static final String LICENSE_PACK_UPDATE = LICENSE_PACK_TOPIC_BASE + TOPIC_SEP + UPDATE;

	/**
	 * License Pack <code>delete</code> event
	 */
	public static final String LICENSE_PACK_DELETE = LICENSE_PACK_TOPIC_BASE + TOPIC_SEP + DELETE;

	/**
	 * Base name of all License Grant events
	 */
	public static final String LICENSE_GRANT_TOPIC_BASE = LICENSES_TOPIC_BASE + TOPIC_SEP + "LicenseGrant"; //$NON-NLS-1$

	/**
	 * License Grant <code>create</code> event
	 */
	public static final String LICENSE_GRANT_CREATE = LICENSE_GRANT_TOPIC_BASE + TOPIC_SEP + CREATE;

	/**
	 * License Grant <code>read</code> event
	 */
	public static final String LICENSE_GRANT_READ = LICENSE_GRANT_TOPIC_BASE + TOPIC_SEP + READ;

	/**
	 * License Grant <code>update</code> event
	 */
	public static final String LICENSE_GRANT_UPDATE = LICENSE_GRANT_TOPIC_BASE + TOPIC_SEP + UPDATE;

	/**
	 * License Grant <code>delete</code> event
	 */
	public static final String LICENSE_GRANT_DELETE = LICENSE_GRANT_TOPIC_BASE + TOPIC_SEP + DELETE;

}
