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
package ru.arsysop.passage.loc.edit;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;

public class LocEdit {
	
	public static final String EXTENSION_KEY_PRIVATE = ".scr"; //$NON-NLS-1$
	
	public static final String LICENSING_REGISTRY_FILE = "licensing.registry.file"; //$NON-NLS-1$

	public static EditingDomain extractEditingDomain(IEclipseContext context) {
		EditingDomain editingDomain = context.get(EditingDomain.class);
		if (editingDomain != null) {
			return editingDomain;
		}
		IEditingDomainProvider provider = context.get(IEditingDomainProvider.class);
		if (provider != null) {
			return provider.getEditingDomain();
		}
		EditingDomainRegistry registry = context.get(EditingDomainRegistry.class);
		if (registry != null) {
			return registry.getEditingDomain();
		}
		return null;
	}

	public static Resource extractResource(Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			return eObject.eResource();
		}
		if (object instanceof Resource) {
			return (Resource) object;
		}
		return null;
	}

	public static EObject extractEObject(Object object) {
		if (object instanceof EObject) {
			return (EObject) object;
		}
		if (object instanceof Resource) {
			Resource resource = (Resource) object;
			EList<EObject> contents = resource.getContents();
			if (!contents.isEmpty()) {
				return contents.get(0);
			}
		}
		return null;
	}

	public static String extractValidationError(EObject newObject) {
		if (newObject == null) {
			return "Input is invalid";
		}
		final Diagnostic result = Diagnostician.INSTANCE.validate(newObject);
		if (result.getSeverity() == Diagnostic.OK) {
			return null;
		}
		// Get the error count and create an appropriate Error message:
		final int errorCount = result.getChildren().size();
		
		final String header = "%s error(s) occured while analyzing your inputs:";
		final String entry = "%s. %s";
		
		final StringBuilder sb = new StringBuilder();
		sb.append(String.format(header, errorCount));
		sb.append('\n');
	
		int messageCount = 0;
		for (final Diagnostic d : result.getChildren()) {
			sb.append('\n');
			sb.append(String.format(entry, ++messageCount, d.getMessage()));
		}
		
		return sb.toString();
	}
}
