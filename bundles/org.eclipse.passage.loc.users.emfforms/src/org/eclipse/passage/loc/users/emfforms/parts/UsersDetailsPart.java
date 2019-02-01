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
package org.eclipse.passage.loc.users.emfforms.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;
import org.eclipse.passage.lic.model.api.UserOrigin;
import org.eclipse.passage.lic.registry.UsersEvents;
import org.eclipse.passage.loc.workbench.emfforms.parts.DetailsView;

public class UsersDetailsPart extends DetailsView {

	@Inject
	public UsersDetailsPart(MPart part, ESelectionService selectionService) {
		super(part, selectionService);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(UsersEvents.USER_ORIGIN_CREATE) UserOrigin input, IEclipseContext context) {
		show(input, context);
	}
	
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new UsersCreateElementCallback();
	}

}
