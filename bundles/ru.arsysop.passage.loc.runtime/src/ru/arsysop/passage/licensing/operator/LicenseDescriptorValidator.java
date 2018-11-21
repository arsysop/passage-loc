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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

public class LicenseDescriptorValidator {

	public static IStatus validateLicenseName(LicensingDescriptor descriptor) {
		if (descriptor.getRequireComponentName() == null) {
			String msg = NLS.bind("Licensing component defined with empty name", null);
			return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg);
		}
		return Status.OK_STATUS;
	}

	public static IStatus validateLicenseVersion(LicensingDescriptor descriptor) {
		if (descriptor.getRequireComponentVersion() == null) {
			String msg = NLS.bind("Licensing component {0} defined with empty version",
					descriptor.getRequireComponentName());
			return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg);
		}
		return Status.OK_STATUS;
	}

	public static IStatus validateDecodedLicenseName(LicensingDescriptor licenseDescriptor) {

		if (!licenseDescriptor.getRequireComponentName().equals(licenseDescriptor.getDecodeComponentName())) {
			String msg = NLS.bind("Decode component name {0} not valid.", licenseDescriptor.getDecodeComponentName());
			return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg);
		}
		return Status.OK_STATUS;
	}

	public static IStatus validateDecodedLicenseVersion(LicensingDescriptor licenseDescriptor) {
		if (!licenseDescriptor.getRequireComponentVersion().equals(licenseDescriptor.getDecodeComponentVersion())) {
			String msg = NLS.bind("Decode component vesion {0} not valid.",
					licenseDescriptor.getDecodeComponentVersion());
			return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg);
		}
		return Status.OK_STATUS;
	}

	public static IStatus validateDecodedLicenseDateExpiration(LicensingDescriptor licenseDescriptor) {
		try {
			Date expiryDate = LicenseDescriptorValidator.formatToDate(licenseDescriptor.getDecodeDateExpiration());
			final GregorianCalendar today = new GregorianCalendar();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);

			if (today.getTime().before(expiryDate)) {
				String msg = NLS.bind("Decode component date expiration not valid.", null);
				return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, msg);
			}
		} catch (final Exception e) {
			return new Status(IStatus.ERROR, LicensingAdmin.BUNDLE_SYMBOLIC_NAME, e.getMessage(), e);
		}

		return Status.OK_STATUS;
	}

	public static Date formatToDate(String date) throws ParseException {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(LicenseDescriptorValidator.DATE_FORMAT).parse(date);
	}

	public static String reductionDate(Date time) {
		if (time == null) {
			return "";
		}
		return new SimpleDateFormat(LicenseDescriptorValidator.DATE_FORMAT).format(time);
	}

	public static final String DATE_FORMAT = "dd/MM/yyyy";

}
