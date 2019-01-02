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
package ru.arsysop.passage.loc.features.ui.viewers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class FeatureSearchFilter extends ViewerSearchFilter<Feature> {

	public FeatureSearchFilter() {
		super(Feature.class);
	}

	@Override
	protected boolean selectElement(Viewer viewer, Object parentElement, Feature feature, String searchText) {
		String name = feature.getName() == null ? "" : feature.getName();
		String identifier = feature.getIdentifier() == null ? "" : feature.getIdentifier();
		Pattern pattern = getSearchPattern();

		Matcher matcherByName = pattern.matcher(name);
		Matcher matcherById = pattern.matcher(identifier);
		if (matcherByName.matches() || matcherById.matches()) {
			return true;
		}
		return false;
	}

}
