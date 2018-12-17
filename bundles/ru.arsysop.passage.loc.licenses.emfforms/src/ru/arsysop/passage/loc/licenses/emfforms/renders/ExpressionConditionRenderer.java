package ru.arsysop.passage.loc.licenses.emfforms.renders;

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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.model.api.LicenseGrant;

public class ExpressionConditionRenderer extends SimpleControlSWTControlSWTRenderer {

	ExpresisonConditionDialog dialog;
	Composite base;
	Text txtConditionExpression;

	@Inject
	public ExpressionConditionRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);

	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		if (control instanceof Text) {
			final Binding binding = getDataBindingContext().bindValue(
					WidgetProperties.text(SWT.Modify).observe(txtConditionExpression), getModelValue(),
					withPreSetValidation(new UpdateValueStrategy()), null);
			return new Binding[] { binding };
		}

		return new Binding[] {};
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		base = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		base.setLayout(layout);
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		GridData txtGD = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		txtConditionExpression = new Text(base, SWT.BORDER);
		txtConditionExpression.setLayoutData(txtGD);
		txtConditionExpression.setText(getCurrentValue());
		txtConditionExpression.setEditable(false);

		GridData btnGD = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		btnGD.heightHint = 28;
		btnGD.widthHint = 60;
		Button btnConditionExpressionEdit = new Button(base, SWT.PUSH);
		btnConditionExpressionEdit.setText("Edit");
		btnConditionExpressionEdit.setLayoutData(btnGD);
		btnConditionExpressionEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog = new ExpresisonConditionDialog(Display.getDefault().getActiveShell(), getCurrentValue());
				if (dialog.open() == Dialog.OK) {
					txtConditionExpression.setText(dialog.getResultValues());
				}
			}
		});

		return txtConditionExpression;
	}

	@Override
	protected void dispose() {
		super.dispose();
		if (base != null && !base.isDisposed()) {
			for (Control control : base.getChildren()) {
				if (control != null) {
					control.dispose();
				}
			}
		}
	}

	protected String getCurrentValue() {
		String conditionType = "";
		EObject domainModel = getViewModelContext().getDomainModel();
		if (domainModel instanceof LicenseGrant) {
			conditionType = ((LicenseGrant) domainModel).getConditionExpression();
		}
		if (conditionType == null) {
			conditionType = "";
		}
		return conditionType;
	}

	@Override
	protected String getUnsetText() {
		return "";
	}

}
