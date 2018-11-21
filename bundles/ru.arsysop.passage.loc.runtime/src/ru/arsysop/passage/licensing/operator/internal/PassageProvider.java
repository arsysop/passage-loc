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
package ru.arsysop.passage.licensing.operator.internal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ru.arsysop.passage.lic.model.meta.LicFactory;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.ProductVersionDescriptor;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.licensing.operator.LicenseDescriptor;
import ru.arsysop.passage.licensing.operator.LicenseDescriptorValidator;
import ru.arsysop.passage.licensing.operator.LicenseService;

public class PassageProvider implements LicenseService {

	public static final String COMPONENT_NAME_ATTRIBUTE = "component";
	public static final String COMPONENT_VERSION_ATTRIBUTE = "componentVersion";

	public static final String DATE_ATTRIBUTE = "dateExp";
	public static final String FLOATING_COUNT = "floatingCount";

	public static final String USER_ATTRIBUTE = "user";
	public static final String USER_EMAIL_ATTRIBUTE = "email";


	public static final String PRODUCT_NAME_ATTRIBUTE = "product";


	private static final String SEPARATOR = " : ";
	private static final String N_LINE = "\r\n";
	private static final String BUNDLE_ID = "ru.arsysop.passage.rt";

	LicFactory factory = LicPackage.eINSTANCE.getLicFactory();

	List<LicenseDescriptor> licenses = new ArrayList<>();

	public List<LicenseDescriptor> getLicenses() {
		return licenses;
	}

	public LicenseDescriptor createLicenseData(ProductVersionDescriptor productVersion, FeatureVersionDescriptor featureVersion, UserDescriptor user,
			File licenseCrypted, int dateExp, int monthExp, int yearExp, int floatingCount) {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateExp);
		instance.set(Calendar.MONTH, monthExp);
		instance.set(Calendar.YEAR, yearExp);

		//FIXME:AF:rework
		LicenseDescriptorImpl license = new LicenseDescriptorImpl();
//		license.setId(String.valueOf(licenses.size() + 1));
		license.setFeatureVersion(featureVersion);
//		license.setUserInfo((User) user);
		license.setProductVersion(productVersion);
//		license.setToken(licenseCrypted.getAbsolutePath());
//		license.setExpireDate(instance.getTime());
		license.setFloatingCount(floatingCount);
		licenses.add(license);
		return license;

	}

	public File createLicenseFile(ProductVersionDescriptor productVersion, FeatureVersionDescriptor featureVersion, UserDescriptor user, int date,
			int month, int year, int floatingCount) throws CoreException {

		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, date);
		instance.set(Calendar.MONTH, month);
		instance.set(Calendar.YEAR, year);
		String dateString = LicenseDescriptorValidator.reductionDate(instance.getTime());

		File licenseFolder = new File(productVersion.getProduct().getIdentifier() + File.separator + productVersion.getVersion()
				+ File.separator + featureVersion.getFeature().getIdentifier() + File.separator + "license");
		if (licenseFolder != null && !licenseFolder.exists()) {
			licenseFolder.mkdirs();
		}

		if (!licenseFolder.exists()) {
			IStatus status = new Status(IStatus.ERROR, BUNDLE_ID,
					String.format("Folder %s does not exist", licenseFolder));
			throw new CoreException(status);
		}

		Path licensePath = Paths.get(licenseFolder.getAbsolutePath(), "license.dat");
		File licenseFile = licensePath.toFile();
		if (!licenseFile.exists()) {
			try {
				licenseFile.createNewFile();
			} catch (IOException e) {
				IStatus status = new Status(IStatus.ERROR, BUNDLE_ID, e.getMessage(), e);
				throw new CoreException(status);
			}
		}
		if (!licenseFile.exists()) {
			IStatus status = new Status(IStatus.ERROR, BUNDLE_ID, String.format("File %s does not exist", licenseFile));
			throw new CoreException(status);
		}
		StringBuilder builder = new StringBuilder();

		builder.append(PRODUCT_NAME_ATTRIBUTE + SEPARATOR + productVersion.getProduct().getIdentifier() + N_LINE);
		builder.append(COMPONENT_NAME_ATTRIBUTE + SEPARATOR + featureVersion.getFeature().getIdentifier() + N_LINE);
		builder.append(COMPONENT_VERSION_ATTRIBUTE + SEPARATOR + featureVersion.getVersion() + N_LINE);
		builder.append(USER_ATTRIBUTE + SEPARATOR + user.getFullName() + N_LINE);
		builder.append(USER_EMAIL_ATTRIBUTE + SEPARATOR + user.getEmail() + N_LINE);
		builder.append(DATE_ATTRIBUTE + SEPARATOR + dateString + N_LINE);
		builder.append(FLOATING_COUNT + SEPARATOR + floatingCount + N_LINE);

		FileOutputStream fileStream = null;
		BufferedOutputStream buffStream = null;
		try {
			fileStream = new FileOutputStream(licenseFile);
			buffStream = new BufferedOutputStream(fileStream);
			buffStream.write(builder.toString().getBytes());
			buffStream.flush();

		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, BUNDLE_ID, e.getMessage(), e);
			throw new CoreException(status);
		} finally {
			try {
				buffStream.close();
				fileStream.close();
			} catch (Exception exc) {
				IStatus status = new Status(IStatus.ERROR, BUNDLE_ID, exc.getMessage(), exc);
				throw new CoreException(status);
			}
		}

		return licenseFile;
	}

}
