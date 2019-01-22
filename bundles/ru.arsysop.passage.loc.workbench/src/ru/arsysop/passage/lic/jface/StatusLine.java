/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
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
package ru.arsysop.passage.lic.jface;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import ru.arsysop.passage.lic.jface.LicensingImages;

public class StatusLine extends CLabel {
	
	private final LicensingImages images;

	public StatusLine(Composite parent, LicensingImages images) {
		super(parent, SWT.LEFT);
		this.images = images;
	}

    private String computeImageKey(IStatus status) {
    	int severity = status.getSeverity();
    	switch (severity) {
		case IStatus.ERROR:
			return LicensingImages.IMG_LEVEL_ERROR;
		case IStatus.WARNING:
			return LicensingImages.IMG_LEVEL_WARN;
		case IStatus.INFO:
			return LicensingImages.IMG_LEVEL_INFO;
		case IStatus.OK:
			return LicensingImages.IMG_LEVEL_OK;
		default:
			return LicensingImages.IMG_LEVEL_WARN;
		}
    }

    public void setStatus(IStatus status) {
    	if (status == null) {
    		setText(""); //$NON-NLS-1$
    		setImage(null);
    		return;
		}
    	String message = status.getMessage();
    	if (message != null) {
    		setText(message);
    	} else {
    		setText(""); //$NON-NLS-1$
    	}
    	setImage(images.getImage(computeImageKey(status)));
	}
}
