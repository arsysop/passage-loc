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
package ru.arsysop.passage.loc.workbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;

public class LocWokbench {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.workbench"; //$NON-NLS-1$

	public static final String COMMAND_VIEW_PERSPECTIVE = "ru.arsysop.passage.loc.workbench.command.view.perspective"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_VIEW_PERSPECTIVE_ID = "ru.arsysop.passage.loc.workbench.commandparameter.perspective.id"; //$NON-NLS-1$

	public static final String COMMAND_RESOURCE_CREATE = "ru.arsysop.passage.loc.workbench.command.resource.create"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_RESOURCE_CREATE_DOMAIN = "ru.arsysop.passage.loc.workbench.commandparameter.resource.create.domain"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_RESOURCE_CREATE_PERSPECTIVE = "ru.arsysop.passage.loc.workbench.commandparameter.resource.create.perspective"; //$NON-NLS-1$

	public static final String COMMAND_RESOURCE_LOAD = "ru.arsysop.passage.loc.workbench.command.resource.load"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_RESOURCE_LOAD_DOMAIN = "ru.arsysop.passage.loc.workbench.commandparameter.resource.load.domain"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_RESOURCE_LOAD_PERSPECTIVE = "ru.arsysop.passage.loc.workbench.commandparameter.resource.load.perspective"; //$NON-NLS-1$

	public static final String COMMAND_RESOURCE_SAVE = "ru.arsysop.passage.loc.workbench.command.resource.save"; //$NON-NLS-1$

	public static final String COMMAND_RESOURCE_DELETE = "ru.arsysop.passage.loc.workbench.command.resource.delete"; //$NON-NLS-1$

	public static String selectSavePath(Shell shell, String extension) {
		String[] array = maskFilters(extension);
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterExtensions(array);
		return fileDialog.open();
	}

	public static String selectLoadPath(Shell shell, String extension, String... others) {
		String[] array = maskFilters(extension, others);
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(array);
		return fileDialog.open();
	}

	private static String[] maskFilters(String extension, String... others) {
		List<String> filters = new ArrayList<>();
		filters.add(maskExtension(extension));
		for (String other : others) {
			filters.add(maskExtension(other));
		}
		String[] array = (String[]) filters.toArray(new String[filters.size()]);
		return array;
	}

	private static String maskExtension(String extension) {
		return "*." + extension; //$NON-NLS-1$
	}

	public static void loadDomainResource(IEclipseContext eclipseContext, String domain, Shell shell, String perspectiveId,
			MWindow window) {
		DomainRegistryAccess access = eclipseContext.get(DomainRegistryAccess.class);
		EditingDomainRegistry registry = access.getDomainRegistry(domain);
		String fileExtension = access.getFileExtension(domain);
		String selected = selectLoadPath(shell, fileExtension);
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

}
