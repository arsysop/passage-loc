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
package ru.arsysop.passage.loc.jface;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public interface LocImages {

	String IMG_TOOL_ADD = "IMG_TOOL_ADD"; //$NON-NLS-1$
	String IMG_TOOL_EDIT = "IMG_TOOL_EDIT"; //$NON-NLS-1$
	String IMG_TOOL_REMOVE = "IMG_TOOL_REMOVE"; //$NON-NLS-1$
	
	public Image getImage(String identifier);
	public ImageDescriptor getImageDescriptor(String identifier);

}
