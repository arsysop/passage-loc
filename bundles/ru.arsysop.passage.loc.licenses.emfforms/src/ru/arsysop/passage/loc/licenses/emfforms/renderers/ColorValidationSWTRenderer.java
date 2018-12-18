package ru.arsysop.passage.loc.licenses.emfforms.renderers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public interface ColorValidationSWTRenderer {

	final static Color COLOR_VALIDATION_ERROR = new Color(Display.getDefault(), new RGB(250, 194, 180));
	final static Color COLOR_VALIDATION_SUCCESS = new Color(Display.getDefault(), new RGB(250, 250, 250));

}
