package ru.arsysop.passage.loc.features.emfforms.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;

import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.loc.workbench.emfforms.parts.DetailsView;

public class FeaturesDetailsPart extends DetailsView {

	@Inject
	public FeaturesDetailsPart(IEclipseContext context, MPart part) {
		super(context, part);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(FeaturesEvents.FEATURE_SET_CREATE) FeatureSet input) {
		show(input);
	}
	
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new FeaturesCreateElementCallback();
	}

}
