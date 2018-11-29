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
package ru.arsysop.passage.lic.base;

import org.eclipse.osgi.service.environment.EnvironmentInfo;

public class LicensingConfigurations {

	public static String extractProductIdentifier(EnvironmentInfo info) {
		if (info == null) {
			return null;
		}
		String[] nonFrameworkArgs = info.getNonFrameworkArgs();
		String configuration = null;
		for (int i = 0; i < nonFrameworkArgs.length; i++) {
			String arg = nonFrameworkArgs[i];
			if ("-product".equals(arg)) {
				int index = i + 1;
				if (index < nonFrameworkArgs.length) {
					configuration = nonFrameworkArgs[index];
					break;
				}
	
			}
		}
		return configuration;
	}

}
