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

import java.io.File;
import java.util.Arrays;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class LocWokbench {

	public static String[] openFilePathDialog(Shell shell, int style, String[] fileExtensionFilters) {
		FileDialog fileDialog = new FileDialog(shell, style);

		fileDialog.setFilterExtensions(fileExtensionFilters);
		fileDialog.open();

		String[] filenames = fileDialog.getFileNames();
		String[] result = new String[filenames.length];
		String path = fileDialog.getFilterPath() + File.separator;
		String extension = null;

		for (int i = 0; i < filenames.length; i++) {
			String filename = path + filenames[i];
			if (extension != null) {
				int dot = filename.lastIndexOf('.');
				if (dot == -1 || !Arrays.asList(fileExtensionFilters).contains("*" + filename.substring(dot))) //$NON-NLS-1$
				{
					filename += extension;
				}
			}
			result[i] = filename;
		}
		return result;
	}

}
