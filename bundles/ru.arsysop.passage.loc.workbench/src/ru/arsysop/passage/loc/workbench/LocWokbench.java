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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.model.meta.LicPackage;

public class LocWokbench {
	
	public static final String[] FILE_EXTENSIONS_LIC = new String[] {"*." + LicPackage.eNAME}; //$NON-NLS-1$

	public static String selectSavePath(Shell shell, String[] extensions) {
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterExtensions(extensions);
		String selected = fileDialog.open();
		return selected;
	}

	public static String selectLoadPath(Shell shell) {
		return selectLoadPath(shell, FILE_EXTENSIONS_LIC);
	}

	public static String selectLoadPath(Shell shell, String[] extensions) {
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(extensions);
		String selected = fileDialog.open();
		return selected;
	}

}
