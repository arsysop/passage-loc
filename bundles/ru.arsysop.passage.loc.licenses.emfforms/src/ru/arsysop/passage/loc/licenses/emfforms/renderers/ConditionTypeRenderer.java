package ru.arsysop.passage.loc.licenses.emfforms.renderers;

import java.util.Arrays;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import ru.arsysop.passage.lic.model.api.LicenseGrant;

public class ConditionTypeRenderer extends SimpleControlSWTControlSWTRenderer {
	String[] preDefinedValues = new String[] { "hardware", "time" };
	Combo combo;

	@Inject
	public ConditionTypeRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);

	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		final Binding binding = getDataBindingContext().bindValue(WidgetProperties.selection().observe(control),
				getModelValue(), withPreSetValidation(new UpdateValueStrategy()), null);

		return new Binding[] { binding };
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		final GridDataFactory gdf = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).span(2,
				1);
		gdf.applyTo(combo);
		String[] predefinedValues = getPredefinedValues();
		combo.setItems(predefinedValues);
		String currentValue = getCurrentValue();
		if (currentValue == null || currentValue.isEmpty()) {
			combo.select(0);
		} else {
			int curIndex = Arrays.asList(predefinedValues).indexOf(currentValue);
			combo.select(curIndex);
		}

		combo.setBackground(Display.getDefault().getSystemColor(SWT.BACKGROUND));
		return combo;
	}

	@Override
	protected void dispose() {
		super.dispose();
		if (combo != null) {
			combo.dispose();
		}
	}

	protected String[] getPredefinedValues() {
		return preDefinedValues;
	}

	protected String getCurrentValue() {
		String conditionType = "";
		EObject domainModel = getViewModelContext().getDomainModel();
		if (domainModel instanceof LicenseGrant) {
			conditionType = ((LicenseGrant) domainModel).getConditionType();
		}
		return conditionType;
	}

	@Override
	protected String getUnsetText() {
		return "";
	}

}
