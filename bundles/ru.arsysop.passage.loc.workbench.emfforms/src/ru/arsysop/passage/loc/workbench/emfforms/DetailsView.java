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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecp.common.spi.ChildrenDescriptorCollector;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emfforms.internal.swt.treemasterdetail.defaultprovider.DefaultDeleteActionBuilder;
import org.eclipse.emfforms.spi.swt.treemasterdetail.MenuProvider;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailComposite;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailMenuListener;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailSWTFactory;
import org.eclipse.emfforms.spi.swt.treemasterdetail.actions.ActionCollector;
import org.eclipse.emfforms.spi.swt.treemasterdetail.actions.MasterDetailAction;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.model.api.UserOrigin;
import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.lic.registry.LicensesEvents;
import ru.arsysop.passage.lic.registry.ProductsEvents;
import ru.arsysop.passage.lic.registry.UsersEvents;
import ru.arsysop.passage.loc.edit.LocEdit;

public class DetailsView {

	private final MPart part;

	private Composite content;

	//TreeMasterDetailComposite implies the Resource has root EObject
	private EObject root;

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
	public void setInput(@Named(IServiceConstants.ACTIVE_SELECTION) Notifier input) {
		show(input);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_CREATE) FeatureSet input) {
		show(input);
	}

	@Inject
	@Optional
	public void showProductLine(@UIEventTopic(ProductsEvents.PRODUCT_LINE_CREATE) ProductLine input) {
		show(input);
	}

	@Inject
	@Optional
	public void showUserOrigin(@UIEventTopic(UsersEvents.USER_ORIGIN_CREATE) UserOrigin input) {
		show(input);
	}

	@Inject
	@Optional
	public void showLicensePack(@UIEventTopic(LicensesEvents.LICENSE_PACK_CREATE) LicensePack input) {
		show(input);
	}

	protected void show(Notifier input) {
		if (content == null || content.isDisposed()) {
			return;
		}
		if (input == null) {
			return;
		}
		this.root = LocEdit.extractEObject(input);
		Resource resource = LocEdit.extractResource(input);
		configurePart(resource);
		Control[] children = content.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		if (this.root != null) {
			try {
				TreeMasterDetailComposite rootView = createRootView(content, resource, getCreateElementCallback());
				TreeViewer selectionProvider = rootView.getSelectionProvider();
				selectionProvider.refresh();
				EObject objectToReveal = this.root;
				while (objectToReveal != null) {
					selectionProvider.reveal(objectToReveal);
					if (selectionProvider.testFindItem(objectToReveal) != null) {
						break;
					}
					objectToReveal = objectToReveal.eContainer();
				}
				if (objectToReveal == null) {
					return;
				}

				rootView.setSelection(new StructuredSelection(objectToReveal));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		content.layout();
	}

	protected TreeMasterDetailComposite createRootView(Composite parent, Object editorInput,
			CreateElementCallback createElementCallback) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		composite.setLayout(new FormLayout());

		final FormData treeMasterDetailLayoutData = new FormData();
		treeMasterDetailLayoutData.top = new FormAttachment(0);
		treeMasterDetailLayoutData.left = new FormAttachment(0);
		treeMasterDetailLayoutData.right = new FormAttachment(100);
		treeMasterDetailLayoutData.bottom = new FormAttachment(100);
		final TreeMasterDetailComposite treeMasterDetail = createTreeMasterDetail(composite, editorInput,
				createElementCallback);
		treeMasterDetail.setLayoutData(treeMasterDetailLayoutData);
		return treeMasterDetail;
	}

	protected TreeMasterDetailComposite createTreeMasterDetail(final Composite composite, Object editorInput,
			final CreateElementCallback createElementCallback) {
		final TreeMasterDetailComposite treeMasterDetail = TreeMasterDetailSWTFactory
				.fillDefaults(composite, SWT.NONE, editorInput).customizeCildCreation(createElementCallback)
				.customizeMenu(new MenuProvider() {
					@Override
					public Menu getMenu(TreeViewer treeViewer, EditingDomain editingDomain) {
						final MenuManager menuMgr = new MenuManager();
						menuMgr.setRemoveAllWhenShown(true);
						final List<MasterDetailAction> masterDetailActions = ActionCollector.newList()
								.addCutAction(editingDomain).addCopyAction(editingDomain).addPasteAction(editingDomain)
								.getList();
						menuMgr.addMenuListener(new TreeMasterDetailMenuListener(new ChildrenDescriptorCollector(),
								menuMgr, treeViewer, editingDomain, masterDetailActions, createElementCallback,
								new DefaultDeleteActionBuilder()));
						final Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
						return menu;

					}
				}).create();
		return treeMasterDetail;
	}

	protected void configurePart(Resource resource) {
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(LocEdit.extractEObject(resource));
		if (editingDomain instanceof AdapterFactoryEditingDomain) {
			if (commandStack == null) {
				commandStack = editingDomain.getCommandStack();
				commandStack.addCommandStackListener(dirtyStackListener);
			}
			commandStack.flush();
		}
		if (resource != null) {
			part.setLabel(String.valueOf(resource.getURI()));
		} else {
			part.setLabel("Details");
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
		if (root == null) {
			part.setDirty(false);
			return;
		}
		Resource eResource = root.eResource();
		if (eResource != null) {
			IStatus status = LicModelCore.save(eResource);
			if (status.isOK()) {
				if (commandStack instanceof BasicCommandStack) {
					BasicCommandStack basicCommandStack = (BasicCommandStack) commandStack;
					basicCommandStack.saveIsDone();
					part.setDirty(basicCommandStack.isSaveNeeded());
				}
			}
		}
	}

	protected CreateElementCallback getCreateElementCallback() {
		return null;
	}

}
