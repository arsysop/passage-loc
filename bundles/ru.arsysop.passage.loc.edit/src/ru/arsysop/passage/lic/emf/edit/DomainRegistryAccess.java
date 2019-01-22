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
package ru.arsysop.passage.lic.emf.edit;

public interface DomainRegistryAccess {
	
	String PROPERTY_DOMAIN_NAME = "ru.arsysop.passage.lic.emf.edit.domain.name"; //$NON-NLS-1$
	String PROPERTY_FILE_EXTENSION = "ru.arsysop.passage.lic.emf.edit.file.extension"; //$NON-NLS-1$
	
	EditingDomainRegistry getDomainRegistry(String domain);

	String getFileExtension(String domain);

	ClassifierInitializer getClassifierInitializer(String domain);

	SelectionCommandAdvisor getSelectionCommandAdvisor(String domain);
}
