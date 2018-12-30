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

import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;

import ru.arsysop.passage.loc.workbench.LocWokbench;

public class OpenPerspectiveHandler {

	@Execute
	public void execute(IApplicationContext applicationContext, MWindow window, EPartService partService, @Named(LocWokbench.COMMANDPARAMETER_VIEW_PERSPECTIVE_ID) String perspectiveId) {
		String brandingName = applicationContext.getBrandingName();
		Optional<MPerspective> switched = partService.switchPerspective(perspectiveId);
		if (switched.isPresent()) {
			MPerspective perspective = switched.get();
			String label = perspective.getLocalizedLabel();
			String title = brandingName + ' ' + '-' + ' ' + label;
			window.setLabel(title);
		}
		
	}

	@CanExecute
	public boolean canExecute(MWindow window, EModelService modelService, @Named(LocWokbench.COMMANDPARAMETER_VIEW_PERSPECTIVE_ID) String perspectiveId) {
		MUIElement found = modelService.find(perspectiveId, window);
		if (found instanceof MPerspective) {
			MPerspective active = modelService.getActivePerspective(window);
			if (active == null) {
				return true;
			}
			return !Objects.equals(perspectiveId, active.getElementId());
		}
		return false;
	}

}