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

import java.util.Optional;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class ResourceLoadHandler {

	@Execute
	public void execute(Shell shell, IEclipseContext eclipseContext, MWindow window,
			@Named(LocWokbench.COMMANDPARAMETER_RESOURCE_LOAD_DOMAIN) String domain,
			@Named(LocWokbench.COMMANDPARAMETER_RESOURCE_LOAD_PERSPECTIVE) String perspectiveId) {
		DomainRegistryAccess access = eclipseContext.get(DomainRegistryAccess.class);
		EditingDomainRegistry registry = access.getDomainRegistry(domain);
		String fileExtension = access.getFileExtension(domain);
		String selected = LocWokbench.selectLoadPath(shell, fileExtension);
		if (selected == null) {
			return;
		}
		EPartService partService = eclipseContext.get(EPartService.class);
		Optional<MPerspective> switched = partService.switchPerspective(perspectiveId);
		if (switched.isPresent()) {
			MPerspective perspective = switched.get();
			String label = perspective.getLocalizedLabel();
			IApplicationContext applicationContext = eclipseContext.get(IApplicationContext.class);
			String brandingName = applicationContext.getBrandingName();
			String title = brandingName + ' ' + '-' + ' ' + label;
			window.setLabel(title);
		}
		registry.registerSource(selected);
	}


	@CanExecute
	public boolean canExecute(@Named(LocWokbench.COMMANDPARAMETER_RESOURCE_LOAD_DOMAIN) String domain) {
		return domain != null;
	}

}