package ru.arsysop.passage.loc.users.emfforms.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;

import ru.arsysop.passage.lic.model.api.UserOrigin;
import ru.arsysop.passage.lic.registry.UsersEvents;
import ru.arsysop.passage.loc.workbench.emfforms.parts.DetailsView;

public class UsersDetailsPart extends DetailsView {

	@Inject
	public UsersDetailsPart(IEclipseContext context, MPart part) {
		super(context, part);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(UsersEvents.USER_ORIGIN_CREATE) UserOrigin input) {
		show(input);
	}
	
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new UsersCreateElementCallback();
	}

}
