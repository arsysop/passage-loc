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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class LocEdit {
	
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
}
