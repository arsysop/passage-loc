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
package ru.arsysop.passage.loc.workbench.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class ResourceLoadHandler {
	
	@Execute
	public void execute(Shell shell, DomainRegistryAccess access, @Named(LocWokbench.COMMANDPARAMETER_RESOURCE_LOAD_DOMAIN) String domain) {
		EditingDomainRegistry registry = access.getDomainRegistry(domain);
		if (domain != null) {
			String fileExtension = access.getFileExtension(domain);
			String selected = LocWokbench.selectLoadPath(shell, fileExtension);
			if (selected != null) {
				registry.registerSource(selected);
			}
		}
	}
		
}