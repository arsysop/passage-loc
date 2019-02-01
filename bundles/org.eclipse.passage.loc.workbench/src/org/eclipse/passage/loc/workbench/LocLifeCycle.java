package org.eclipse.passage.loc.workbench;

import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.passage.lic.jface.ImageFinder;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class LocLifeCycle {

	@PostContextCreate
	void postContextCreate(IApplicationContext context) {
		String windowImages = context.getBrandingProperty("windowImages"); //$NON-NLS-1$
		Bundle brandingBundle = context.getBrandingBundle();
		ImageDescriptor[] descriptors = ImageFinder.getImages(windowImages, brandingBundle);

		Image[] images = new Image[descriptors.length];
		for (int i = 0; i < descriptors.length; ++i) {
			images[i] = descriptors[i].createImage();
		}
		Window.setDefaultImages(images);
	}

}
