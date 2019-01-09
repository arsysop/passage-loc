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
package ru.arsysop.passage.loc.workbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.emf.edit.ClassifierInitializer;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.loc.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.loc.jface.dialogs.FilteredSelectionDialog;
import ru.arsysop.passage.loc.jface.dialogs.LabelSearchFilter;
import ru.arsysop.passage.loc.workbench.viewers.DomainRegistryLabelProvider;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;

public class LocWokbench {

	public static final String BUNDLE_SYMBOLIC_NAME = "ru.arsysop.passage.loc.workbench"; //$NON-NLS-1$

	public static final String COMMAND_VIEW_PERSPECTIVE = "ru.arsysop.passage.loc.workbench.command.view.perspective"; //$NON-NLS-1$
	public static final String COMMANDPARAMETER_VIEW_PERSPECTIVE_ID = "ru.arsysop.passage.loc.workbench.commandparameter.perspective.id"; //$NON-NLS-1$

	public static final String TOPIC_SHOW = "ru/arsysop/passage/loc/workbench/show"; //$NON-NLS-1$

	public static String selectSavePath(Shell shell, String extension) {
		String[] array = maskFilters(extension);
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterExtensions(array);
		return fileDialog.open();
	}

	public static String selectLoadPath(Shell shell, String extension, String... others) {
		String[] array = maskFilters(extension, others);
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(array);
		return fileDialog.open();
	}

	private static String[] maskFilters(String extension, String... others) {
		List<String> filters = new ArrayList<>();
		filters.add(maskExtension(extension));
		for (String other : others) {
			filters.add(maskExtension(other));
		}
		String[] array = (String[]) filters.toArray(new String[filters.size()]);
		return array;
	}

	private static String maskExtension(String extension) {
		return "*." + extension; //$NON-NLS-1$
	}

	public static void createDomainResource(IEclipseContext context, String domain, String perspectiveId) {
		LicensingImages images = context.get(LicensingImages.class);
		DomainRegistryAccess registryAccess = context.get(DomainRegistryAccess.class);
		ClassifierInitializer initializer = registryAccess.getClassifierInitializer(domain);
		EditingDomainRegistry registry = registryAccess.getDomainRegistry(domain);
		EClass eClass = registry.getContentClassifier();

		Wizard wizard = new CreateFileWizard(context, domain, perspectiveId);
		Shell shell = context.get(Shell.class);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(initializer.newObjectTitle());
		dialog.setMessage(initializer.newFileMessage());

		Shell createdShell = dialog.getShell();
		createdShell.setText(initializer.newObjectMessage());
		createdShell.setImage(images.getImage(eClass.getName()));

		dialog.open();
	}

	public static void loadDomainResource(IEclipseContext eclipseContext, String domain, String perspectiveId) {
		DomainRegistryAccess access = eclipseContext.get(DomainRegistryAccess.class);
		EditingDomainRegistry registry = access.getDomainRegistry(domain);
		String fileExtension = access.getFileExtension(domain);
		Shell shell = eclipseContext.get(Shell.class);
		String selected = selectLoadPath(shell, fileExtension);
		if (selected == null) {
			return;
		}
		switchPerspective(eclipseContext, perspectiveId);
		registry.registerSource(selected);
	}

	public static void switchPerspective(IEclipseContext eclipseContext, String perspectiveId) {
		EPartService partService = eclipseContext.get(EPartService.class);
		Optional<MPerspective> switched = partService.switchPerspective(perspectiveId);
		if (switched.isPresent()) {
			MPerspective perspective = switched.get();
			String label = perspective.getLocalizedLabel();
			IApplicationContext applicationContext = eclipseContext.get(IApplicationContext.class);
			String brandingName = applicationContext.getBrandingName();
			String title = brandingName + ' ' + '-' + ' ' + label;
			MWindow window = eclipseContext.get(MWindow.class);
			window.setLabel(title);
		}
	}

	public static <C> C selectClassifier(IEclipseContext context, String classifier, String title, Iterable<C> input,
			C initial, Class<C> clazz) {
		Object selected = selectClassifier(context, classifier, title, input, initial);
		if (clazz.isInstance(selected)) {
			return clazz.cast(selected);
		}
		return null;
	}

	public static <C> Object selectClassifier(IEclipseContext context, String classifier, String title,
			Iterable<C> input, C initial) {
		Shell shell = context.get(Shell.class);
		LicensingImages images = context.get(LicensingImages.class);
		ComposedAdapterFactoryProvider provider = context.get(ComposedAdapterFactoryProvider.class);
		return selectClassifier(shell, images, provider, classifier, title, input, initial);
	}

	public static <C> C selectClassifier(Shell shell, LicensingImages images, ComposedAdapterFactoryProvider provider,
			String classifier, String title, Iterable<C> input, C initial, Class<C> clazz) {
		Object selected = selectClassifier(shell, images, provider, classifier, title, input, initial);
		if (clazz.isInstance(selected)) {
			return clazz.cast(selected);
		}
		return null;
	}

	public static <C> Object selectClassifier(Shell shell, LicensingImages images,
			ComposedAdapterFactoryProvider provider, String classifier, String title, Iterable<C> input, C initial) {
		if (input == null) {
			return null;
		}
		long count = StreamSupport.stream(input.spliterator(), false).count();
		if (count == 0) {
			return null;
		}
		if (count == 1) {
			return input.iterator().next();
		}
		LabelSearchFilter filter = new LabelSearchFilter();

		FilteredSelectionDialog dialog = new FilteredSelectionDialog(shell, images, false, filter);
		dialog.setTitle(title);
		dialog.setImage(images.getImage(classifier));

		ComposedAdapterFactory factory = provider.getComposedAdapterFactory();
		dialog.setLabelProvider(new DomainRegistryLabelProvider(images, factory));
		dialog.setInput(input);
		if (initial != null) {
			dialog.setInitial(initial);
		}
		if (dialog.open() == Dialog.OK) {
			return dialog.getFirstResult();
		}
		return null;
	}
}
