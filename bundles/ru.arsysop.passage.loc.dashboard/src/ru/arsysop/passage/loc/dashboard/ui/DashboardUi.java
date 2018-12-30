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
package ru.arsysop.passage.loc.dashboard.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;

@SuppressWarnings("restriction")
public class DashboardUi {

	public static Object executeCommand(IEclipseContext context, String commandId, String parameterId, String parameterValue) {
		Map<Object, Object> parameters = new HashMap<>();
		parameters.put(parameterId, parameterValue);
		return executeCommand(context, commandId, parameters);
	}

	public static Object executeCommand(IEclipseContext context, String commandId, Map<Object, Object> parameters) {
		ECommandService commandService = context.get(ECommandService.class);
		Command command = commandService.getCommand(commandId);
		ParameterizedCommand parametrizedCommand = ParameterizedCommand.generateCommand(command, parameters );
		EHandlerService eHandlerService = context.get(EHandlerService.class);
		return eHandlerService.executeHandler(parametrizedCommand);
	}

}
