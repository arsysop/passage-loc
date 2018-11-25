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

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.loc.licenses.ui.LicensesUi;
import ru.arsysop.passage.loc.workbench.viewers.DescriptorRegistryContentProvider;
import ru.arsysop.passage.loc.workbench.viewers.StructuredSelectionListener;

public class UserExplorer {

	@Inject
	private ESelectionService selectionService;

	@Inject
	UserRegistry userService;

	private ISelectionChangedListener selectionChangeListener;
	private TreeViewer viewer;

	@PostConstruct
	public void postConstruct(Composite parent) {
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(new GridLayout());
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new TreeViewer(base);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new DescriptorRegistryContentProvider());
		viewer.setLabelProvider(new UserLabelProvider());
		selectionChangeListener = new StructuredSelectionListener(selectionService);
		viewer.addSelectionChangedListener(selectionChangeListener);
		resetInput();
	}

	@Focus
	void setFocus() {
		viewer.getControl().setFocus();
	}

	@PreDestroy
	public void dispose() {
		viewer.removeSelectionChangedListener(selectionChangeListener);
	}

	@Inject
	@Optional
	private void subscribeTopicProduct(@UIEventTopic(LicensesUi.TOPIC_USERS_CREATE) User user) {
		resetInput();
	}

	private void resetInput() {
		if (viewer != null && !viewer.getControl().isDisposed()) {
			ISelection selection = viewer.getSelection();
			viewer.setInput(userService);
			if (selection.isEmpty()) {
				Iterable<UserDescriptor> descriptors = userService.getDescriptors();
				Iterator<UserDescriptor> iterator = descriptors.iterator();
				if (iterator.hasNext()) {
					UserDescriptor first = iterator.next();
					selection = new StructuredSelection(first);
				}
			}
			viewer.setSelection(selection);
		}
	}
}
