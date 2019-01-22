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

public class ProductsEvents extends RegistryEvents {

	/**
	 * Base name of all Products events
	 */
	public static final String PRODUCTS_TOPIC_BASE = "ru/arsysop/passage/lic/registry/products"; //$NON-NLS-1$
	
	/**
	 * Base name of all Product Line events
	 */
	public static final String PRODUCT_LINE_TOPIC_BASE = PRODUCTS_TOPIC_BASE + TOPIC_SEP + "ProductLine"; //$NON-NLS-1$

	/**
	 * Product Line <code>create</code> event
	 */
	public static final String PRODUCT_LINE_CREATE = PRODUCT_LINE_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Product Line <code>read</code> event
	 */
	public static final String PRODUCT_LINE_READ = PRODUCT_LINE_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Product Line <code>update</code> event
	 */
	public static final String PRODUCT_LINE_UPDATE = PRODUCT_LINE_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Product Line <code>delete</code> event
	 */
	public static final String PRODUCT_LINE_DELETE = PRODUCT_LINE_TOPIC_BASE + TOPIC_SEP + DELETE;
	
	/**
	 * Base name of all Product events
	 */
	public static final String PRODUCT_TOPIC_BASE = PRODUCTS_TOPIC_BASE + TOPIC_SEP + "Product"; //$NON-NLS-1$

	/**
	 * Product <code>create</code> event
	 */
	public static final String PRODUCT_CREATE = PRODUCT_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Product <code>read</code> event
	 */
	public static final String PRODUCT_READ = PRODUCT_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Product <code>update</code> event
	 */
	public static final String PRODUCT_UPDATE = PRODUCT_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Product <code>delete</code> event
	 */
	public static final String PRODUCT_DELETE = PRODUCT_TOPIC_BASE + TOPIC_SEP + DELETE;
	
	/**
	 * Base name of all Product Version events
	 */
	public static final String PRODUCT_VERSION_TOPIC_BASE = PRODUCTS_TOPIC_BASE + TOPIC_SEP + "ProductVersion"; //$NON-NLS-1$

	/**
	 * Product Version <code>create</code> event
	 */
	public static final String PRODUCT_VERSION_CREATE = PRODUCT_VERSION_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Product Version <code>read</code> event
	 */
	public static final String PRODUCT_VERSION_READ = PRODUCT_VERSION_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Product Version <code>update</code> event
	 */
	public static final String PRODUCT_VERSION_UPDATE = PRODUCT_VERSION_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Product Version <code>delete</code> event
	 */
	public static final String PRODUCT_VERSION_DELETE = PRODUCT_VERSION_TOPIC_BASE + TOPIC_SEP + DELETE;

	/**
	 * Base name of all Product Version Feature events
	 */
	public static final String PRODUCT_VERSION_FEATURE_TOPIC_BASE = PRODUCTS_TOPIC_BASE + TOPIC_SEP + "ProductVersionFeature"; //$NON-NLS-1$

	/**
	 * Product Version Feature <code>create</code> event
	 */
	public static final String PRODUCT_VERSION_FEATURE_CREATE = PRODUCT_VERSION_FEATURE_TOPIC_BASE + TOPIC_SEP + CREATE;
	
	/**
	 * Product Version Feature <code>read</code> event
	 */
	public static final String PRODUCT_VERSION_FEATURE_READ = PRODUCT_VERSION_FEATURE_TOPIC_BASE + TOPIC_SEP + READ;
	
	/**
	 * Product Version Feature <code>update</code> event
	 */
	public static final String PRODUCT_VERSION_FEATURE_UPDATE = PRODUCT_VERSION_FEATURE_TOPIC_BASE + TOPIC_SEP + UPDATE;
	
	/**
	 * Product Version Feature <code>delete</code> event
	 */
	public static final String PRODUCT_VERSION_FEATURE_DELETE = PRODUCT_VERSION_FEATURE_TOPIC_BASE + TOPIC_SEP + DELETE;

}
