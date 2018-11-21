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
package ru.arsysop.passage.licensing.operator;

public interface LicensingDescriptor {

	String getRequireComponentName();

	String getRequireComponentVersion();

	String getRequireLicenseTokenPath();
	
	String getDecodeProductName();
	
	String getDecodeComponentName();

	String getDecodeComponentVersion();

	String getDecodeDateExpiration();

	String getDecodeUserName();

	String getDecodeUserEmail();
	
	String getDecodeFloatingCount();

	void setDecodedComponentName(String decodedComponentName);

	void setDecodedDateExpiration(String decodedDateExpiration);

	void setDecodedComponentVersion(String decodedComponentVersion);

	void setDecodedUserName(String decodedUserName);

	void setDecodedUserEmail(String decodedUserEmail);

	void setDecodedProductName(String decodedComponentName);

	void setDecodedFloatingCount(String decodeFloatingCount);
	
}
