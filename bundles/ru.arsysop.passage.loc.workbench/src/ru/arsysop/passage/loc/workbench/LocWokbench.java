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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class LocWokbench {
	
	public static String selectSavePath(Shell shell, String extenion) {
		String[] array = maskFilters(extenion);
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterExtensions(array);
		String selected = fileDialog.open();
		if (selected != null && !selected.endsWith(extenion)) {
			selected = selected + extenion;
		}
		return selected;
	}

	public static String selectLoadPath(Shell shell, String extenion, String... others) {
		String[] array = maskFilters(extenion, others);
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(array);
		String selected = fileDialog.open();
		return selected;
	}

	private static String[] maskFilters(String extenion, String... others) {
		List<String> filters = new ArrayList<>();
		filters.add(maskExtension(extenion));
		for (String other : others) {
			filters.add(maskExtension(other));
		}
		String[] array = (String[]) filters.toArray(new String[filters.size()]);
		return array;
	}
	
	private static String maskExtension(String extension) {
		return "*." + extension; //$NON-NLS-1$
	}

}
