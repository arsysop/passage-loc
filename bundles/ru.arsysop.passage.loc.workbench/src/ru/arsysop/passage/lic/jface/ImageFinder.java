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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

public class ImageFinder {

	private static final ImageDescriptor[] NO_IMAGE_DESCRIPTORS = new ImageDescriptor[0];

	public static URL getUrl(String value, Bundle definingBundle) {
		try {
			if (value != null) {
				return new URL(value);
			}
		} catch (MalformedURLException e) {
			if (definingBundle != null) {
				return FileLocator.find(definingBundle, new Path(value));
			}
		}
		return null;
	}

	public static List<URL> getUrls(String value, Bundle definingBundle, String separator) {
		if (value == null) {
			return Collections.emptyList();
		}

		StringTokenizer tokens = new StringTokenizer(value, separator);
		List<URL> list = new ArrayList<>(10);
		while (tokens.hasMoreTokens()) {
			String segment = tokens.nextToken().trim();
			URL url = getUrl(segment, definingBundle);
			if (url != null) {
				list.add(url);
			}
		}

		return list;
	}

	public static ImageDescriptor getImage(String value, Bundle definingBundle) {
		URL url = getUrl(value, definingBundle);
		return url == null ? null : ImageDescriptor.createFromURL(url);
	}

	public static ImageDescriptor[] getImages(String value, Bundle definingBundle) {
		List<URL> urls = getUrls(value, definingBundle, ","); //$NON-NLS-1$
		if (urls.isEmpty()) {
			return NO_IMAGE_DESCRIPTORS;
		}
		ImageDescriptor[] images = new ImageDescriptor[urls.size()];
		for (int i = 0; i < images.length; ++i) {
			images[i] = ImageDescriptor.createFromURL(urls.get(i));
		}
		return images;
	}

}
