package ru.arsysop.passage.lic.emf.edit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;

import ru.arsysop.passage.lic.registry.Identified;

public abstract class DomainContentAdapter<R extends EditingDomainRegistry> extends EContentAdapter {
	
	protected final R registry;
	
	protected DomainContentAdapter(R registry) {
		this.registry = registry;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof ResourceSet) {
			ResourceSet resourceSet = (ResourceSet) notifier;
			switch (notification.getFeatureID(ResourceSet.class)) {
			case ResourceSet.RESOURCE_SET__RESOURCES:
				processResourceSetResources(resourceSet, notification);
				break;
			default:
				break;
			}
		} else if (notifier instanceof Resource) {
			Resource resource = (Resource) notifier;
			switch (notification.getFeatureID(Resource.class)) {
			case Resource.RESOURCE__CONTENTS:
				processResourceContents(resource, notification);
			case Resource.RESOURCE__IS_LOADED:
				processResourceIsLoaded(resource, notification);
				break;
			default:
				break;
			}
		}
		super.notifyChanged(notification);
	}

	protected void processResourceSetResources(ResourceSet resourceSet, Notification notification) {
		//nothing by default
	}
	
	protected void processResourceContents(Resource resource, Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		switch (notification.getEventType()) {
		case Notification.ADD:
			processResourceContentsAdded(resource, newValue);
			break;
		case Notification.REMOVE:
			processResourceContentsRemoved(resource, oldValue);
			break;
	
		default:
			break;
		}
	}

	protected void processResourceContentsAdded(Resource resource, Object newValue) {
		if (newValue instanceof Identified) {
			Identified identified = (Identified) newValue;
			registry.registerContent(identified);
		}
	}

	protected void processResourceContentsRemoved(Resource resource, Object oldValue) {
		if (oldValue instanceof Identified) {
			Identified identified = (Identified) oldValue;
			registry.unregisterContent(identified.getIdentifier());
		}
	}

	protected void processResourceIsLoaded(Resource resource, Notification notification) {
		//nothing by default
	}
	
}
