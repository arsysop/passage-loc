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
package ru.arsysop.passage.loc.workbench.viewers;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.registry.BaseDescriptor;
import ru.arsysop.passage.lic.registry.BaseDescriptorRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;

public class DescriptorRegistryExplorer<D extends BaseDescriptor> {

	private final BaseDescriptorRegistry<D> descriptorRegistry;
	private final ESelectionService selectionService;

	private ISelectionChangedListener selectionChangeListener;
	private TreeViewer viewer;

	public DescriptorRegistryExplorer(BaseDescriptorRegistry<D> registry, ESelectionService selectionService) {
		super();
		this.descriptorRegistry = registry;
		this.selectionService = selectionService;
	}
	
	public BaseDescriptorRegistry<D> getDescriptorRegistry() {
		return descriptorRegistry;
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(new GridLayout());
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new TreeViewer(base);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new DescriptorRegistryContentProvider());
		AdapterFactory factory;
		if (descriptorRegistry instanceof EditingDomainBasedRegistry<?>) {
			EditingDomainBasedRegistry<?> registry = (EditingDomainBasedRegistry<?>) descriptorRegistry;
			factory = registry.getComposedAdapterFactory();
		} else {
			factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		}
		
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(factory));
		selectionChangeListener = new StructuredSelectionListener(selectionService);
		viewer.addSelectionChangedListener(selectionChangeListener);
		resetInput();
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@PreDestroy
	public void dispose() {
		viewer.removeSelectionChangedListener(selectionChangeListener);
	}

	protected void resetInput() {
		if (viewer != null && !viewer.getControl().isDisposed()) {
			ISelection selection = viewer.getSelection();
			viewer.setInput(descriptorRegistry);
			if (selection.isEmpty()) {
				Iterable<D> products = descriptorRegistry.getDescriptors();
				Iterator<D> iterator = products.iterator();
				if (iterator.hasNext()) {
					D next = iterator.next();
					selection = new StructuredSelection(next);
				}
			}
			viewer.setSelection(selection);
		}
	}
}
