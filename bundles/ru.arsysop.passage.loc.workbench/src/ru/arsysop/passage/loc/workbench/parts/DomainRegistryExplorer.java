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
package ru.arsysop.passage.loc.workbench.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.workbench.viewers.DomainRegistryLabelProvider;
import ru.arsysop.passage.loc.workbench.viewers.ResourceSetAdapter;
import ru.arsysop.passage.loc.workbench.viewers.StructuredSelectionListener;

public class DomainRegistryExplorer {

	private final EditingDomainRegistry descriptorRegistry;
	private final ESelectionService selectionService;
	private final LicensingImages licensingImages;

	private ISelectionChangedListener selectionChangeListener;
	private TreeViewer viewer;
	private ResourceSetAdapter resourceSetAdapter;

	public DomainRegistryExplorer(EditingDomainRegistry registry, ESelectionService selectionService, LicensingImages images) {
		super();
		this.descriptorRegistry = registry;
		this.selectionService = selectionService;
		this.licensingImages = images;
	}
	
	public EditingDomainRegistry getDescriptorRegistry() {
		return descriptorRegistry;
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(new GridLayout());
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new TreeViewer(base);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		AdapterFactory factory;
		if (descriptorRegistry instanceof EditingDomainBasedRegistry) {
			EditingDomainBasedRegistry registry = (EditingDomainBasedRegistry) descriptorRegistry;
			factory = registry.getComposedAdapterFactory();
		} else {
			factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		}
		viewer.setContentProvider(new AdapterFactoryContentProvider(factory));
		viewer.setLabelProvider(new DomainRegistryLabelProvider(licensingImages, factory));
		selectionChangeListener = new StructuredSelectionListener(selectionService);
		viewer.addSelectionChangedListener(selectionChangeListener);
		ResourceSet resourceSet = descriptorRegistry.getEditingDomain().getResourceSet();
		resourceSetAdapter = new ResourceSetAdapter(viewer);
		resourceSet.eAdapters().add(resourceSetAdapter);
		resetInput();
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@PreDestroy
	public void dispose() {
		descriptorRegistry.getEditingDomain().getResourceSet().eAdapters().remove(resourceSetAdapter);
		viewer.removeSelectionChangedListener(selectionChangeListener);
	}

	protected void resetInput() {
		if (viewer != null && !viewer.getControl().isDisposed()) {
			ResourceSet resourceSet = descriptorRegistry.getEditingDomain().getResourceSet();
			ISelection selection = viewer.getSelection();
			viewer.setInput(resourceSet);
			if (selection.isEmpty()) {
				EList<Resource> resources = resourceSet.getResources();
				if (!resources.isEmpty()) {
					Resource resource = resources.get(0);
					selection = new StructuredSelection(resource);
				}
			}
			viewer.setSelection(selection);
		}
	}
}
