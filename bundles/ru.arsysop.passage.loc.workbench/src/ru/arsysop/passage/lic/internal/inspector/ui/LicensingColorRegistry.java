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
package ru.arsysop.passage.lic.internal.inspector.ui;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import ru.arsysop.passage.lic.base.ui.LicensingColors;

@Component
public class LicensingColorRegistry implements LicensingColors {

	private ColorRegistry colorRegistry;

	@Activate
	public void activate() {
		colorRegistry = new ColorRegistry();
		addValidationColors();
	}

	protected void addValidationColors() {
		register(COLOR_VALIDATION_OK, new RGB(250, 250, 250));
		register(COLOR_VALIDATION_ERROR, new RGB(250, 194, 180));
	}

	protected void register(String identifier, RGB rgb) {
		colorRegistry.put(identifier, rgb);
	}

	@Deactivate
	public void deactivate() {
		colorRegistry = null;
	}

	@Override
	public Color getColor(String identifier) {
		return colorRegistry.get(identifier);
	}

}
