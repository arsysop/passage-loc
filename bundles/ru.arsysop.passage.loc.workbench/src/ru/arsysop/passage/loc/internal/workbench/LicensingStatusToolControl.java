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
package ru.arsysop.passage.loc.internal.workbench;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.LicensingEvents;
import ru.arsysop.passage.lic.inspector.HardwareInspector;
import ru.arsysop.passage.lic.inspector.ui.dialogs.LicensingInspectorDialog;
import ru.arsysop.passage.lic.runtime.RestrictionVerdict;

public class LicensingStatusToolControl {
	
	private final HardwareInspector hardwareInspector;

	private final List<RestrictionVerdict> verdicts = new ArrayList<>();

	private Button button;

	@Inject
	public LicensingStatusToolControl(HardwareInspector hardwareInspector) {
		this.hardwareInspector = hardwareInspector;
	}
	
	@Inject
	@Optional
	public void applicationStarted(@UIEventTopic(LicensingEvents.LicensingLifeCycle.RESTRICTION_EXECUTED) Iterable<RestrictionVerdict> actions) {
		verdicts.clear();
		for (RestrictionVerdict verdict : actions) {
			verdicts.add(verdict);
			button.setImage(JFaceResources.getImage("info"));
			button.setText("Info");
			button.setToolTipText("Licensing status: Warning");
		}
		if (verdicts.isEmpty()) {
			button.setImage(JFaceResources.getImage("info"));
			button.setText("OK");
			button.setToolTipText("Licensing status: OK");
		}
	}

	@PostConstruct
	public void createGui(Composite parent) {
		button = new Button(parent, SWT.NONE);
		button.setFont(JFaceResources.getDefaultFont());
		button.setText("Undefined");
		button.setImage(JFaceResources.getImage("info"));
		button.setToolTipText("Licensing status: Undefined");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell activeShell = button.getDisplay().getActiveShell();
				LicensingInspectorDialog dialog = new LicensingInspectorDialog(activeShell, verdicts);
				dialog.setHardwareInspector(hardwareInspector);
				dialog.open();
			}
		});
	}
}