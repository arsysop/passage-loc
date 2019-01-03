package ru.arsysop.passage.loc.licenses.emfforms.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;

import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.registry.LicensesEvents;
import ru.arsysop.passage.loc.workbench.emfforms.parts.DetailsView;

public class LicensesDetailsPart extends DetailsView {

	@Inject
	public LicensesDetailsPart(IEclipseContext context, MPart part) {
		super(context, part);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(LicensesEvents.LICENSE_PACK_CREATE) LicensePack input) {
		show(input);
	}
	
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new LicensesCreateElementCallback();
	}

}
