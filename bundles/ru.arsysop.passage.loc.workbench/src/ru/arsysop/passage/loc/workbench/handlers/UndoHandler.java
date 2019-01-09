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
package ru.arsysop.passage.loc.workbench.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.osgi.util.NLS;

import ru.arsysop.passage.loc.edit.LocEdit;

public class UndoHandler {

	@Execute
	public void execute(IEclipseContext context) {
		EditingDomain editingDomain = LocEdit.extractEditingDomain(context);
		editingDomain.getCommandStack().undo();
	}
	
	
	@CanExecute
	public boolean canExecute(IEclipseContext context, MHandledMenuItem item) {
		EditingDomain editingDomain = LocEdit.extractEditingDomain(context);
		if (editingDomain == null) {
			return false;
		}
		CommandStack commandStack = editingDomain.getCommandStack();
		if (commandStack == null) {
			return false;
		}
		return commandStack.canUndo();
	}


	protected void updateLabel(IEclipseContext context, MHandledMenuItem item, CommandStack commandStack) {
		String base = "Undo";
		Command undoCommand = commandStack.getUndoCommand();
		if (undoCommand == null) {
			return;
		}
		String label = undoCommand.getLabel();
		if (label == null) {
			return;
		}
		String bind = NLS.bind("{0} {1}", base, label);
		item.setLabel(bind);
	}
		
}
