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
package ru.arsysop.passage.loc.licenses.ui.viewers;

import java.util.stream.StreamSupport;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;

public class UserContentProvider implements ITreeContentProvider {
	
	private UserRegistry registry;
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof UserRegistry) {
			UserRegistry registry = (UserRegistry) parentElement;
			Iterable<UserDescriptor> descriptors = registry.getDescriptors();
			return StreamSupport.stream(descriptors.spliterator(), false).toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof UserDescriptor) {
			return registry;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof UserRegistry) {
			this.registry = (UserRegistry) newInput;
		}
	}

}
