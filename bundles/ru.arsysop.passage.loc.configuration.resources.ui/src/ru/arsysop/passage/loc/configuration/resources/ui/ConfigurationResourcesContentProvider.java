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
package ru.arsysop.passage.loc.configuration.resources.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;

public class ConfigurationResourcesContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] result = null;
		if (inputElement instanceof List<?>) {
			List<?> lstElemnts = (List<?>) inputElement;
			if (!lstElemnts.isEmpty()) {
				result = lstElemnts.toArray(new Object[] { lstElemnts.size() });
			}
		}
		if (inputElement instanceof String) {
			result = new Object[] { inputElement };
		}
		return (result != null) ? result : new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement != null && parentElement instanceof String) {
			return new Object[] { parentElement };
		}

		List<String> sources = getSourcesElementbyDomain(parentElement);
		return sources.toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		List<String> sources = getSourcesElementbyDomain(element);
		return !sources.isEmpty();
	}

	public List<String> getSourcesElementbyDomain(Object parentElement) {
		List<String> collect = new ArrayList<>();
		if (parentElement instanceof EditingDomainBasedRegistry) {
			EditingDomainBasedRegistry baseRegistry = (EditingDomainBasedRegistry) parentElement;
			collect = StreamSupport.stream(baseRegistry.getSources().spliterator(), false).collect(Collectors.toList());
		}
		return collect;
	}

}