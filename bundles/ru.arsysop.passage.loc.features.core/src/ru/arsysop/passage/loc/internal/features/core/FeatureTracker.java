package ru.arsysop.passage.loc.internal.features.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;

@Component
public class FeatureTracker extends EContentAdapter {
	
	private EventAdmin eventAdmin;
	private FeatureDomainRegistry featureDomainRegistry;
	
	@Reference
	public void bindEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	public void unbindEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = null;
	}

	@Reference
	public void bindFeatureDomainRegistry(FeatureDomainRegistry featureDomainRegistry) {
		this.featureDomainRegistry = featureDomainRegistry;
	}

	public void unbindFeatureDomainRegistry(FeatureDomainRegistry featureDomainRegistry) {
		this.featureDomainRegistry = null;
	}

	@Activate
	public void activate() {
		ResourceSet resourceSet = featureDomainRegistry.getEditingDomain().getResourceSet();
		resourceSet.eAdapters().add(this);
	}
	
	@Deactivate
	public void deactivate() {
		ResourceSet resourceSet = featureDomainRegistry.getEditingDomain().getResourceSet();
		resourceSet.eAdapters().remove(this);
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		if (Resource.RESOURCE__CONTENTS == notification.getFeatureID(Resource.class)) {
			Object newValue = notification.getNewValue();
			if (newValue instanceof FeatureSet) {
				eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_SET_READ, newValue));
			}
			return;
		}
		super.notifyChanged(notification);
	}
	
	//@see org.eclipse.e4.core.services.events.IEventBroker.DATA
	static final String PROPERTY_DATA = "org.eclipse.e4.data"; //$NON-NLS-1$

	static Event createEvent(String topic, Object data) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(PROPERTY_DATA, data);
		Event event = new Event(topic, properties);
		return event;
	}

}
