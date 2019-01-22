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
package ru.arsysop.passage.loc.dashboard.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import ru.arsysop.passage.loc.dashboard.ui.DashboardUi;

public class DashboardEditHandler {

	@Execute
	public void execute(IEclipseContext eclipseContext,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_DOMAIN) String domain,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_CLASSIFIER) String classifier,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_PERSPECTIVE) String perspectiveId) {
		DashboardUi.editDomainResource(eclipseContext, domain, classifier, perspectiveId);
	}


	@CanExecute
	public boolean canExecute(@Named(DashboardUi.COMMANDPARAMETER_EDIT_DOMAIN) String domain) {
		return domain != null;
	}

}