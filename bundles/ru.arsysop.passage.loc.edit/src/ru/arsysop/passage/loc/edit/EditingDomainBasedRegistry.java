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
package ru.arsysop.passage.loc.edit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.base.LicensingPaths;
import ru.arsysop.passage.lic.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.registry.DescriptorRegistry;

public abstract class EditingDomainBasedRegistry implements DescriptorRegistry, EditingDomainRegistry {
	
	protected EnvironmentInfo environmentInfo;

	protected EventAdmin eventAdmin;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryEditingDomain editingDomain;

	private final List<String> sources = new ArrayList<>();

	public EditingDomainBasedRegistry() {
		BasicCommandStack commandStack = new BasicCommandStack();
		editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}
	
	protected void bindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		this.environmentInfo = environmentInfo;
	}

	protected void unbindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		this.environmentInfo = null;
	}
	
	protected void bindEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	protected void unbindEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = null;
	}
	
	protected void bindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		this.composedAdapterFactory = factoryProvider.getComposedAdapterFactory();
		editingDomain.setAdapterFactory(composedAdapterFactory);
	}
	
	protected void unbindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		this.composedAdapterFactory = null;
		editingDomain.setAdapterFactory(composedAdapterFactory);
	}
	
	@Override
	public Path getBasePath() {
		String areaValue = environmentInfo.getProperty("user.home"); //$NON-NLS-1$
		Path passagePath = Paths.get(areaValue, LicensingPaths.FOLDER_LICENSING_BASE);
		try {
			Files.createDirectories(passagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return passagePath;
	}
	
	protected void activate(Map<String, Object> properties) {
	}
	
	protected void deactivate(Map<String, Object> properties) {
	}

	@Override
	public ComposedAdapterFactory getComposedAdapterFactory() {
		return composedAdapterFactory;
	}
	
	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	protected Map<?, ?> getLoadOptions() {
		return new HashMap<>();
	}

	protected Map<?, ?> getSaveOptions() {
		return new HashMap<>();
	}

	public void loadSource(String source) throws Exception {
		URI uri = createURI(source);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.createResource(uri);
		resource.load(getLoadOptions());
		afterLoad(resource.getContents());
	}

	protected abstract void afterLoad(EList<EObject> contents);

	public void unloadSource(String source) throws Exception {
		URI uri = createURI(source);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.getResource(uri, false);
		beforeUnload(resource.getContents());
		resource.unload();
	}

	protected abstract void beforeUnload(EList<EObject> contents);

	protected URI createURI(String source) {
		return URI.createFileURI(source);
	}

	@Override
	public void registerSource(String source) {
		sources.add(source);
		try {
			loadSource(source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void unregisterSource(String source) {
		sources.remove(source);
	}
	
	@Override
	public Iterable<String> getSources() {
		return Collections.unmodifiableList(sources);
	}

}
