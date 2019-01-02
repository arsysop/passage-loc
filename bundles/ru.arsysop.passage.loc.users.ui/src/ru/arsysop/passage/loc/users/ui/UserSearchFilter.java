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
package ru.arsysop.passage.loc.users.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class UserSearchFilter extends ViewerSearchFilter<User> {

	public UserSearchFilter() {
		super(User.class);
	}

	@Override
	protected boolean selectElement(Viewer viewer, Object parentElement, User element, String searchText) {
		String name = getNotNullValue(((User) element).getFullName());
		String identifier = getNotNullValue(((User) element).getEmail());
		Pattern pattern = getSearchPattern();
		Matcher matcherByName = pattern.matcher(name);
		Matcher matcherById = pattern.matcher(identifier);
		if (matcherByName.matches() || matcherById.matches()) {
			return true;
		}
		return false;
	}

}
