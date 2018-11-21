package ru.arsysop.passage.loc.workbench.emfforms;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DetailsView {

	private Composite content;

	@PostConstruct
	public void createComposite(Composite parent) {
		content = new Composite(parent, SWT.NONE);
		content.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		content.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		content.setLayoutData(GridDataFactory.fillDefaults().create());
	}

	@Inject
	@Optional
	public void setInput(@Named(IServiceConstants.ACTIVE_SELECTION) EObject input) {
		if (input == null) {
			return;
		}
		if (content == null || content.isDisposed()) {
			return;
		}
		try {
			Control[] children = content.getChildren();
			for (Control control : children) {
				control.dispose();
			}
			ECPSWTViewRenderer.INSTANCE.render(content, input);
			content.layout();
		} catch (final ECPRendererException e) {
			e.printStackTrace();
		}
	}

}
