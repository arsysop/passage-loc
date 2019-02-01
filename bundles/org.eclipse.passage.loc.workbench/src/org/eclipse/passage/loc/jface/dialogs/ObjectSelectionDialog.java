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
package org.eclipse.passage.loc.jface.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ObjectSelectionDialog extends TrayDialog {

	private final List<Object> result = new ArrayList<>();

	private final List<Object> initial = new ArrayList<>();

	private String title;
	private Image image;

	private String message = ""; //$NON-NLS-1$

	protected ObjectSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (image != null) {
			shell.setImage(image);
		}
		if (title != null) {
			shell.setText(title);
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Label createMessageArea(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		if (message != null) {
			label.setText(message);
		}
		label.setFont(composite.getFont());
		return label;
	}

	protected List<Object> getInitial() {
		return initial;
	}

	public Object getFirstResult() {
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	protected void setResult(int position, Object element) {
		result.set(position, element);
	}

	public Iterable<Object> getResult() {
		return result;
	}

	public void setInitial(Object... objects) {
		initial.clear();
		for (Object object : objects) {
			initial.add(object);
		}
	}

	public void setInitial(Iterable<?> objects) {
		initial.clear();
		objects.forEach(initial::add);
	}

	protected void setResult(Iterable<?> objects) {
		result.clear();
		objects.forEach(result::add);
	}

	protected void setResult(Object... objects) {
		result.clear();
		for (Object object : objects) {
			result.add(object);
		}
	}

	@Override
	protected boolean isResizable() {
    	return true;
    }

}
