package ru.arsysop.passage.loc.licenses.emfforms.renderers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConditionExpresisonDialog extends Dialog {
	List lstItems;
	String expressionValues;
	Text txtEditItem;
	Button btnAddItem;
	String resultExpressionValues;

	protected ConditionExpresisonDialog(Shell parentShell, String values) {
		super(parentShell);
		this.expressionValues = values;
	}

	public String getResultValues() {
		return resultExpressionValues;
	}

	private String prepareResultValues() {
		StringBuilder builder = new StringBuilder();
		for (String item : lstItems.getItems()) {
			if (builder.length() > 0) {
				builder.append(";");
			}
			builder.append(item);
		}
		return builder.toString();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == Dialog.OK) {
			resultExpressionValues = prepareResultValues();
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayout(new GridLayout(1, false));
		Composite base = new Composite(parent, SWT.NONE);
		base.setLayout(new GridLayout(3, false));
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Label lblTitle = new Label(base, SWT.NONE);
		lblTitle.setText("Condition Expession values:");
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		lstItems = new List(base, SWT.BORDER);
		lstItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		GridData text = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Text txtExpressionItem = new Text(base, SWT.BORDER);
		txtExpressionItem.setLayoutData(text);
		txtExpressionItem.setText("");
		GridData btnGridLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		btnGridLayout.heightHint = 30;
		btnGridLayout.widthHint = 70;

		Button btnConditionExpressionEdit = new Button(base, SWT.PUSH);
		btnConditionExpressionEdit.setText("Add");
		btnConditionExpressionEdit.setLayoutData(btnGridLayout);
		btnConditionExpressionEdit.getFont().getFontData()[0].setHeight(10);
		btnConditionExpressionEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textExpression = txtExpressionItem.getText();
				if (!textExpression.isEmpty()) {
					lstItems.add(textExpression);
					txtExpressionItem.setText("");
				}
			}
		});
		Button btnConditionExpressionRemove = new Button(base, SWT.PUSH);
		btnConditionExpressionRemove.setText("Remove");
		btnConditionExpressionRemove.setLayoutData(btnGridLayout);
		btnConditionExpressionRemove.getFont().getFontData()[0].setHeight(10);
		btnConditionExpressionRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = lstItems.getSelectionIndex();
				if (selectionIndex > -1) {
					lstItems.remove(selectionIndex);
					txtExpressionItem.setText("");
				}
			}
		});

		if (expressionValues != null && !expressionValues.isEmpty()) {
			String[] splitedVAlues = expressionValues.split(";");
			lstItems.setItems(splitedVAlues);
		}

		lstItems.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedValue = lstItems.getItems()[lstItems.getSelectionIndex()];
				txtExpressionItem.setText(selectedValue);
			}
		});

		return parent;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(400, 500);
	}
}
