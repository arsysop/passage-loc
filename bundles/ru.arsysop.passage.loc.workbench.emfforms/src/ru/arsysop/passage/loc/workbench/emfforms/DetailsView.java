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
package ru.arsysop.passage.loc.workbench.emfforms;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emfforms.swt.core.EMFFormsSWTConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DetailsView {

	private final MPart part;

	private Composite content;

	private EObject input;

	private CommandStackListener dirtyStackListener;
	private CommandStack commandStack;

	@Inject
	public DetailsView(MPart part) {
		this.part = part;
		this.dirtyStackListener = e -> {
			part.setDirty(true);
		};
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		content = new Composite(parent, SWT.NONE);
		content.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		content.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		content.setLayoutData(GridDataFactory.fillDefaults().create());
	}

	@Inject
	@Optional
	public void setInput(@Named(IServiceConstants.ACTIVE_SELECTION) EObject input) {
		this.input = input;
		if (input == null) {
			return;
		}
		if (content == null || content.isDisposed()) {
			return;
		}
		configurePart(input);
		try {
			Control[] children = content.getChildren();
			for (Control control : children) {
				control.dispose();
			}
			final VViewModelProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
			properties.addInheritableProperty(EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_KEY,
					EMFFormsSWTConstants.USE_ON_MODIFY_DATABINDING_VALUE);
			ECPSWTViewRenderer.INSTANCE.render(content, input, properties);
			content.layout();
		} catch (final ECPRendererException e) {
			e.printStackTrace();
		}
	}

	protected void configurePart(EObject input) {
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(input);
		if (editingDomain instanceof AdapterFactoryEditingDomain) {
			AdapterFactoryEditingDomain afed = (AdapterFactoryEditingDomain) editingDomain;
			AdapterFactory adapterFactory = afed.getAdapterFactory();
			Adapter adapt = adapterFactory.adapt(input, IItemLabelProvider.class);
			if (adapt instanceof IItemLabelProvider) {
				IItemLabelProvider labelProvider = (IItemLabelProvider) adapt;
				part.setLabel(labelProvider.getText(input));
			}
			if (commandStack == null) {
				commandStack = editingDomain.getCommandStack();
				commandStack.addCommandStackListener(dirtyStackListener);
			}
		}
	}

	@PreDestroy
	public void dispose() {
		if (commandStack != null) {
			commandStack.removeCommandStackListener(dirtyStackListener);
		}
	}
	
	@Persist
	public void save() {
		if (input == null) {
			part.setDirty(false);
			return;
		}
		Resource eResource = input.eResource();
		if (eResource != null) {
			//FIXME: should be extracted to .core to respect save options 
			try {
				eResource.save(new HashMap<>());
				part.setDirty(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
