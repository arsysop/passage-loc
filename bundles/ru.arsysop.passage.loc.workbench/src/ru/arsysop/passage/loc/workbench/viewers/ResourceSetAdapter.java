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
package ru.arsysop.passage.loc.workbench.viewers;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.viewers.StructuredViewer;

public class ResourceSetAdapter extends EContentAdapter {
	
	private final StructuredViewer viewer;

	public ResourceSetAdapter(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		//FIXME: removing calls to super for now, revisit later
		if (viewer.getControl().isDisposed()) {
			return;
		}
		viewer.refresh();
	}

}
