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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.osgi.service.environment.EnvironmentInfo;

import ru.arsysop.passage.lic.registry.BaseDescriptor;
import ru.arsysop.passage.lic.registry.DescriptorRegistry;

public abstract class EditingDomainBasedRegistry<D extends BaseDescriptor> implements DescriptorRegistry<D>, EditingDomainRegistry {
	
	public static final String LICENSING_REGISTRY_FILE = "licensing.registry.file"; //$NON-NLS-1$
	
	protected EnvironmentInfo environmentInfo;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryEditingDomain editingDomain;

	private final Map<String, D> descriptors = new HashMap<>();

	private final List<String> sources = new ArrayList<>();

	public EditingDomainBasedRegistry() {
		BasicCommandStack commandStack = new BasicCommandStack();
		editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}
	
	protected abstract Class<D> getDescriptorClass();
	
	public void bindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		this.environmentInfo = environmentInfo;
	}

	public void unbindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		this.environmentInfo = null;
	}
	
	public void bindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		this.composedAdapterFactory = factoryProvider.getComposedAdapterFactory();
		editingDomain.setAdapterFactory(composedAdapterFactory);
	}
	
	public void unbindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		this.composedAdapterFactory = null;
		editingDomain.setAdapterFactory(composedAdapterFactory);
	}
	
	public void activate() {
		String sourceDefault = getSourceDefault();
		try {
			File file = new File(sourceDefault);
			if (file.exists()) {
				loadSource(sourceDefault);
			} else {
				ResourceSet resourceSet = editingDomain.getResourceSet();
				URI uri = createURI(sourceDefault);
				Resource resource = resourceSet.createResource(uri);
				resource.save(getSaveOptions());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deactivate() {
		String sourceDefault = getSourceDefault();
		try {
			File file = new File(sourceDefault);
			if (file.exists()) {
				loadSource(sourceDefault);
			} else {
				ResourceSet resourceSet = editingDomain.getResourceSet();
				URI uri = createURI(sourceDefault);
				Resource resource = resourceSet.createResource(uri);
				resource.save(getSaveOptions());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ComposedAdapterFactory getComposedAdapterFactory() {
		return composedAdapterFactory;
	}
	
	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	protected abstract String getSourceDefault();

	protected Map<?, ?> getLoadOptions() {
		return new HashMap<>();
	}

	protected Map<?, ?> getSaveOptions() {
		return new HashMap<>();
	}

	@Override
	public void loadSource(String source) throws Exception {
		URI uri = createURI(source);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.createResource(uri);
		resource.load(getLoadOptions());
		EList<EObject> contents = resource.getContents();
		Class<D> descriptorClass = getDescriptorClass();
		for (EObject eObject : contents) {
			if (descriptorClass.isInstance(eObject)) {
				D descriptor = descriptorClass.cast(eObject);
				String identifier = descriptor.getIdentifier();
				descriptors.put(identifier, descriptor);
			}
		}
	}

	@Override
	public void unloadSource(String source) throws Exception {
		URI uri = createURI(source);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.getResource(uri, false);
		resource.unload();
	}

	@Override
	public D getDescriptor(String identifier) {
		return descriptors.get(identifier);
	}

	@Override
	public Iterable<D> getDescriptors() {
		return descriptors.values();
	}

	protected URI createURI(String source) {
		return URI.createFileURI(source);
	}

	@Override
	public Iterable<D> selectDescriptors(String filter) throws Exception {
		return getDescriptors();
	}

	@Override
	public void insertDescriptors(Iterable<D> descriptors) throws Exception {
	}

	@Override
	public void updateDescriptors(Iterable<D> descriptors) throws Exception {
	}

	@Override
	public void deleteDescriptors(Iterable<D> descriptors) throws Exception {
	}

	protected EList<EObject> extractEObjects(Iterable<D> descriptors) {
		EList<EObject> eObjects = new BasicEList<>();
		for (D d : descriptors) {
			if (d instanceof EObject) {
				EObject eObject = (EObject) d;
				eObjects.add(eObject);
			}
		}
		return eObjects;
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

	protected void addDescriptors(String source, Iterable<D> descriptors) {
		//FIXME: call on load
	}

	protected void removeDescriptors(String source, Iterable<D> descriptors) {
		//FIXME: call on unload
	}
}
