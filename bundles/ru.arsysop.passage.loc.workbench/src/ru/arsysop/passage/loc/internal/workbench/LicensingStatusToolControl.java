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
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.base.LicensingEvents;
import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.base.ui.RestrictionVerdictLabels;
import ru.arsysop.passage.lic.inspector.HardwareInspector;
import ru.arsysop.passage.lic.inspector.ui.dialogs.LicensingInspectorDialog;
import ru.arsysop.passage.lic.runtime.ConfigurationRequirement;
import ru.arsysop.passage.lic.runtime.RestrictionVerdict;

public class LicensingStatusToolControl {
	
	private final LicensingImages images;
	private final HardwareInspector hardwareInspector;

	private final List<ConfigurationRequirement> requirements = new ArrayList<>();
	private final List<RestrictionVerdict> verdicts = new ArrayList<>();

	private Button button;

	@Inject
	public LicensingStatusToolControl(LicensingImages images, HardwareInspector hardwareInspector) {
		this.images = images;
		this.hardwareInspector = hardwareInspector;
	}
	
	@Inject
	@Optional
	public void requirementsResolved(@UIEventTopic(LicensingEvents.LicensingLifeCycle.REQUIREMENTS_RESOLVED) Iterable<ConfigurationRequirement> reqs) {
		requirements.clear();
		reqs.forEach(requirements::add);
	}

	@Inject
	@Optional
	public void restrictionsExecuted(@UIEventTopic(LicensingEvents.LicensingLifeCycle.RESTRICTIONS_EXECUTED) Iterable<RestrictionVerdict> actions) {
		verdicts.clear();
		actions.forEach(verdicts::add);
		RestrictionVerdict last = RestrictionVerdictLabels.resolveLastVerdict(verdicts);
		updateButton(last);
	}

	@PostConstruct
	public void createGui(Composite parent) {
		button = new Button(parent, SWT.NONE);
		button.setFont(JFaceResources.getDefaultFont());
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String contacts = "Passage Licensing Integration Components \nhttps://github.com/arsysop/passage-lic";
				Shell activeShell = button.getDisplay().getActiveShell();
				LicensingInspectorDialog dialog = new LicensingInspectorDialog(activeShell, images, contacts);
				dialog.setHardwareInspector(hardwareInspector);
				dialog.updateLicensingStatus(requirements, verdicts);
				dialog.open();
			}
		});
		button.setImage(images.getImage(LicensingImages.IMG_LEVEL_OK));
		button.setText("Undefined");
	}

	protected void updateButton(RestrictionVerdict last) {
		String key = RestrictionVerdictLabels.resolveImageKey(last);
		button.setImage(images.getImage(key));
		button.setText(RestrictionVerdictLabels.resolveLabel(last));
		button.setToolTipText(RestrictionVerdictLabels.resolveSummary(last));
	}

}