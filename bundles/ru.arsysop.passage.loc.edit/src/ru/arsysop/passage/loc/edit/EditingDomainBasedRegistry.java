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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.base.LicensingPaths;
import ru.arsysop.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.lic.emf.edit.DomainContentAdapter;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.registry.DescriptorRegistry;

public abstract class EditingDomainBasedRegistry implements DescriptorRegistry, EditingDomainRegistry {

	// @see org.eclipse.e4.core.services.events.IEventBroker.DATA
	protected static final String PROPERTY_DATA = "org.eclipse.e4.data"; //$NON-NLS-1$

	protected EnvironmentInfo environmentInfo;

	protected EventAdmin eventAdmin;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryEditingDomain editingDomain;

	private final List<String> sources = new ArrayList<>();

	private EContentAdapter contentAdapter;

	public EditingDomainBasedRegistry() {
		BasicCommandStack commandStack = new BasicCommandStack();
		editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, commandStack,
				new HashMap<Resource, Boolean>());
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
		contentAdapter = createContentAdapter();
		editingDomain.getResourceSet().eAdapters().add(contentAdapter);
	}

	protected abstract DomainContentAdapter<? extends EditingDomainRegistry> createContentAdapter();

	protected void deactivate(Map<String, Object> properties) {
		editingDomain.getResourceSet().eAdapters().remove(contentAdapter);
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
	}

	public void unloadSource(String source) throws Exception {
		URI uri = createURI(source);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.getResource(uri, false);
		resource.unload();
		resourceSet.getResources().remove(resource);
	}

	protected URI createURI(String source) {
		return URI.createFileURI(source);
	}

	@Override
	public void registerSource(String source) {
		sources.add(source);
		try {
			loadSource(source);
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.FINER, e.getMessage(), e);
		}
	}

	@Override
	public void unregisterSource(String source) {
		sources.remove(source);
		try {
			unloadSource(source);
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.FINER, e.getMessage(), e);
		}
	}

	@Override
	public Iterable<String> getSources() {
		return Collections.unmodifiableList(sources);
	}

	protected static Event createEvent(String topic, Object data) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(PROPERTY_DATA, data);
		Event event = new Event(topic, properties);
		return event;
	}

}
