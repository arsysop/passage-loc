/*******************************************************************************
 * Copyright (c) 2018 ArSysOp
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
package ru.arsysop.passage.loc.jface.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.ui.LicensingImages;

public class FilteredSelectionDialog extends ObjectSelectionStatusDialog {
	
	private final boolean multi;

	private TableViewer list;

	private final List<Object> input = new ArrayList<>();

	private LabelProvider labelProvider = new LabelProvider();

	public FilteredSelectionDialog(Shell parent, LicensingImages licensingImages, boolean multi) {
		super(parent, licensingImages);
		this.multi = multi;
	}
	
	public void setInput(Iterable<?> objects) {
		input.clear();
		objects.forEach(input::add);
	}

	public void setLabelProvider(LabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	@Override
	protected void computeResult() {
		List<?> selectedElements = list.getStructuredSelection().toList();
		setResult(selectedElements);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		content.setLayout(layout);

		list = new TableViewer(content, (multi ? SWT.MULTI : SWT.SINGLE)
				| SWT.BORDER | SWT.V_SCROLL | SWT.VIRTUAL);
		list.setContentProvider(ArrayContentProvider.getInstance());
		list.setLabelProvider(labelProvider);
		list.setInput(input);
		list.setSelection(new StructuredSelection(getInitial().toArray()));

		GridData gd = new GridData(GridData.FILL_BOTH);
		applyDialogFont(list.getTable());
		gd.heightHint= list.getTable().getItemHeight() * 15;
		list.getTable().setLayoutData(gd);

		list.addDoubleClickListener(event -> handleDoubleClick());

		applyDialogFont(content);

		return dialogArea;
	}
	
	protected void handleDoubleClick() {
		okPressed();
	}
}
