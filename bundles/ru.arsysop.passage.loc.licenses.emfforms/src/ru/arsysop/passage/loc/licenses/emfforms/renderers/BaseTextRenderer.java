package ru.arsysop.passage.loc.licenses.emfforms.renderers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.core.services.label.NoLabelFoundException;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class BaseTextRenderer extends SimpleControlSWTControlSWTRenderer implements ColorValidationSWTRenderer {

	private Text text;

	@Inject
	public BaseTextRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);
	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		if (control instanceof Text) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Binding binding = getDataBindingContext().bindValue(WidgetProperties.text(SWT.Modify).observe(text),
					getModelValue(), withPreSetValidation(new UpdateValueStrategy()), null);
			return new Binding[] { binding };
		}

		return new Binding[] {};
	}

	@Override
	protected void setValidationColor(Control control, Color validationColor) {
		if (control instanceof Text) {
			Text textControl = ((Text) control);
			if (textControl.getText().isEmpty()) {
				control.setBackground(COLOR_VALIDATION_ERROR);
			} else {
				control.setBackground(COLOR_VALIDATION_SUCCESS);
			}
		}
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		text.setLayoutData(data);
		text.setBackground(Display.getDefault().getSystemColor(SWT.BACKGROUND));
		text.setText(getTextMessage());
		text.setEditable(true);
		return text;
	}

	@Override
	protected void dispose() {
		if (text != null) {
			text.dispose();
		}
		super.dispose();
	}

	protected String getTextMessage() {
		try {
			return (String) getEMFFormsLabelProvider()
					.getDisplayName(getVElement().getDomainModelReference(), getViewModelContext().getDomainModel())
					.getValue();
		} catch (final NoLabelFoundException ex) {
			Logger logger = Logger.getLogger(BaseTextRenderer.class.getName());
			logger.log(Level.FINER, ex.getMessage(), ex);
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	protected String getUnsetText() {
		return "";
	}

}
