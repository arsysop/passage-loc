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
package org.eclipse.passage.loc.jface.dialogs;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;

public class LabelSearchFilter extends ViewerSearchFilter<String> {

	public LabelSearchFilter() {
		super(String.class);
	}
	
	@Override
	protected String convertElement(Viewer viewer, Object parentElement, Object element, Class<String> elementClass) {
		if (viewer instanceof ContentViewer) {
			ContentViewer contentViewer = (ContentViewer) viewer;
			IBaseLabelProvider baseLabelProvider = contentViewer.getLabelProvider();
			if (baseLabelProvider instanceof ILabelProvider) {
				ILabelProvider labelProvider = (ILabelProvider) baseLabelProvider;
				return labelProvider.getText(element);
			}
		}
		return null;
	}

	@Override
	protected boolean selectElement(Viewer viewer, Object parentElement, String element, String searchText) {
		if (getSearchPattern().matcher(element).matches()) {
			return true;
		}
		return false;
	}

}
