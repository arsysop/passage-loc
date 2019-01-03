package ru.arsysop.passage.loc.products.emfforms.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;

import ru.arsysop.passage.lic.model.api.ProductLine;
import ru.arsysop.passage.lic.registry.ProductsEvents;
import ru.arsysop.passage.loc.workbench.emfforms.parts.DetailsView;

public class ProductsDetailsPart extends DetailsView {

	@Inject
	public ProductsDetailsPart(IEclipseContext context, MPart part) {
		super(context, part);
	}

	@Inject
	@Optional
	public void showFeatureSet(@UIEventTopic(ProductsEvents.PRODUCT_LINE_CREATE) ProductLine input) {
		show(input);
	}
	
	@Override
	protected CreateElementCallback getCreateElementCallback() {
		return new ProductsCreateElementCallback();
	}

}
