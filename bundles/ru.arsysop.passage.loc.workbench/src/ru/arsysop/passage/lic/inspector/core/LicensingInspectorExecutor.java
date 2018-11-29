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
package ru.arsysop.passage.lic.inspector.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.base.LicensingEvents;
import ru.arsysop.passage.lic.runtime.RestrictionExecutor;
import ru.arsysop.passage.lic.runtime.RestrictionVerdict;

@Component
public class LicensingInspectorExecutor implements RestrictionExecutor {
	
	private EventAdmin eventAdmin;
	
	@Reference
	public void bindEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
	
	public void unbindEventBroker(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
	
	@Override
	public void execute(Iterable<RestrictionVerdict> actions) {
		String topic = LicensingEvents.LicensingLifeCycle.RESTRICTION_EXECUTED;
		Map<String, Object> properties = new HashMap<>();
		properties.put(IEventBroker.DATA, actions);
		Event event = new Event(topic, properties);
		eventAdmin.postEvent(event);
	}

}
